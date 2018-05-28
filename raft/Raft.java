package raft;

import raft.net.ssl.SSLChannel;
import raft.util.Serialization;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Raft<T extends Serializable> {
    enum ServerState {
        INITIALIZING, READYING, RUNNING, TERMINATING;
    }

    enum ClusterState {
        INITIALIZING, RUNNING, TERMINATING;
    }

    AtomicReference<ServerState> serverState = new AtomicReference<>(ServerState.INITIALIZING);
//	AtomicReference<ClusterState> clusterState = new AtomicReference<>(ClusterState.INITIALIZING);

    UUID ID = UUID.randomUUID();
    Integer port;

    ConcurrentHashMap<UUID, RaftCommunication> cluster = new ConcurrentHashMap<>();
    ReentrantLock clusterLock = new ReentrantLock();
    AtomicReference<RaftState> state = new AtomicReference<>(RaftState.FOLLOWER);

    LinkedTransferQueue<RaftLog<T>> clientRequests = new LinkedTransferQueue<>();

    ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private Timer timer = new Timer();
    TimerTask followerTimerTask;
    TimerTask candidateTimerTask;
    TimerTask leaderTimerTask;

    private TimerTask followerTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                lock.lock();
                changeStateToCandidate();
                condition.signal();
                lock.unlock();
            }
        };
    }

    private TimerTask candidateTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                lock.lock();
                if (votes.get() > (cluster.size() + 1) / 2) {
                    changeStateToLeader();
                }
                condition.signal();
                lock.unlock();
            }
        };
    }


    public void changeStateToFollower() {
        if (RaftState.FOLLOWER != state.get()) {
            state.set(RaftState.FOLLOWER);
            System.out.println("Changing State to Follower");
        }
        else {
            System.out.println("Staying Follower");
        }
        printState();
    }

    private void changeStateToCandidate() {
        System.out.println("Follower Timetout");
        state.set(RaftState.CANDIDATE);
        System.out.println("Changing State to Candidate");
        votedFor.set(ID);
        currentTerm.getAndAdd(1);
        votes.set(1);
        printState();
    }

    public void changeStateToLeader() {
        if (RaftState.LEADER != state.get()) {
            System.out.println("Change state to Leader ");
            votes.set(0);
            votedFor.set(null);
            state.set(RaftState.LEADER);
            leaderID = ID;
            printState();
            cancelCandidateTimeout();

        }
    }

    private TimerTask leaderTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                lock.lock();
                condition.signal();
                lock.unlock();
            }
        };
    }

    private void cancelCandidateTimeout() {
        System.out.println("Cancel Candidate Timeout\n");
        candidateTimerTask.cancel();
        leaderTimeout(); //TODO: @Shinuzi isto é preciso?
    }

    void restartFollowerTimeout() {
        System.out.println("Restart Timeout");
        followerTimerTask.cancel();
        followerTimeout(); //TODO: @Shinuzi isto é preciso?
    }

    private void followerTimeout() {
        System.out.println("Starting Timeout");
        int delay = ThreadLocalRandom.current().nextInt(RaftProtocol.maxElectionTimeout - RaftProtocol.minElectionTimeout) + RaftProtocol.minElectionTimeout;
        System.out.println(delay);
        timer.schedule(followerTimerTask = followerTimerTask(), delay);
    }

    private void candidateTimeout() {
        int delay = ThreadLocalRandom.current().nextInt(RaftProtocol.maxElectionTimeout - RaftProtocol.minElectionTimeout) + RaftProtocol.minElectionTimeout;
        System.out.println(delay);
        timer.schedule(candidateTimerTask = candidateTimerTask(), delay);
    }

    private void leaderTimeout() {
        timer.schedule(leaderTimerTask = leaderTimerTask(), RaftProtocol.HeartbeatPeriod);
    }

    //	Persistent state (save this to stable storage)
    AtomicInteger currentTerm = new AtomicInteger(1);
    AtomicReference<UUID> votedFor = new AtomicReference<>(null);
    AtomicInteger votes = new AtomicInteger(0);
    ArrayList<RaftLog<T>> log = new ArrayList<>();

    //	Volatile state
    UUID leaderID;
    Integer commitIndex = 0;
    Integer lastApplied = 0;

    AtomicBoolean synchronize = new AtomicBoolean(false);

    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

	/*
		Constructors
	 */

    public Raft(Integer port, InetSocketAddress cluster) {
        this.port = port;
        log.add(new RaftLog<T>(null, 0));

        // Connect to known cluster
        {
            SSLChannel channel = new SSLChannel(cluster);
            if (channel.connect()) {
                this.pool.execute(new RaftDiscover(this, channel));
            } else {
                System.out.println("Connection failed!"); // DEBUG
                return; // Show better error message
            }
        }

        // Listen for new connections
        this.pool.execute(() -> {
            while (serverState.get() != ServerState.TERMINATING) {
                SSLChannel channel = new SSLChannel(port);
                if (channel.accept()) {
                    pool.execute(new RaftServer<T>(this, channel));
                }
            }
        });

        printState();
    }

    public Raft(Integer port) {
        this.port = port;
        leaderID = ID;
        log.add(new RaftLog<T>(null, 0));

        // Listen for new connections
        this.pool.execute(() -> {
            while (serverState.get() != ServerState.TERMINATING) {
                SSLChannel channel = new SSLChannel(port);
                if (channel.accept()) {
                    pool.execute(new RaftServer<T>(this, channel));
                }
            }
        });

        printState();
    }

	/*
		Public methods
	 */

    public void run() {
        pool.execute(() -> {
            lock.lock();
			/*if (leaderID == null) {
				serverState.set(ServerState.READYING);
				condition.awaitUninterruptibly();
			} */
            serverState.set(ServerState.RUNNING);

            while (serverState.get() != ServerState.TERMINATING) {
                switch (state.get()) {
                    case FOLLOWER:
                        followerTimeout();
                        condition.awaitUninterruptibly();
                        break;
                    case CANDIDATE:
                        synchronize.set(true);
                        System.out.println("Send Vote Requests{");
                        for (RaftCommunication node : cluster.values()) {
                            node.queue.put(RPC.callRequestVoteRPC);
                        }
                        synchronize.set(false);
                        candidateTimeout();
                        condition.awaitUninterruptibly();
                        break;
                    case LEADER:
                        synchronize.set(true);
                        System.out.println("Send HeartBeats{");
                        for (RaftCommunication node : cluster.values()) {
                            node.queue.put(RPC.callAppendEntriesRPC);
                        }
                        synchronize.set(false);
                        leaderTimeout();
                        condition.awaitUninterruptibly();
                        RaftLog<T> request = null;
                        try {
                            request = clientRequests.poll();
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }
                        if (request != null) {
                            log.add(request);
                        }
                        break;
                }
            }
            lock.unlock();
        });
    }

    public void stop() {
        serverState.set(ServerState.TERMINATING);
    }

    public T get() {
        T obj;
        if (state.get() != RaftState.LEADER) {
            SSLChannel channel = connectToLeader();

            if (channel == null) {
                return null;
            }

            channel.send(RPC.callGetValue());

            String message = channel.receiveString();

            obj = Serialization.deserialize(message.split("\n")[1].getBytes());
        } else {
            obj = getValue();
        }
        return obj;
    }

    public boolean set(T var) {
        SSLChannel channel = connectToLeader();

        if (channel == null) {
            return false;
        }

        String serObj = new String(Serialization.serialize(var));

        channel.send(RPC.callSetValue(serObj));

        String message = channel.receiveString();

        return message.equals(RPC.retSetValue(true));
    }

    public boolean delete() {
        SSLChannel channel = connectToLeader();

        if (channel == null) {
            return false;
        }

        channel.send(RPC.callDeleteValue());

        String message = channel.receiveString();

        return message.equals(RPC.retDeleteValue(true));
    }

	/*
		Private methods
	 */

    SSLChannel connectToLeader() {
        if (this.leaderID == null) {
            return null;
        }

        RaftCommunication leader = this.cluster.get(this.leaderID);

        if (leader == null) {
            return null;
        }

        SSLChannel channel = new SSLChannel(leader.address);

        if (!channel.connect()) {
            System.out.println("Connection failed!"); // DEBUG
            return null; // Show better error message
        }

        return channel;
    }

    T getValue() {
        return log.get((int) (commitIndex - 1)).entry;
    }

    boolean setValue(T object) {
        RaftLog<T> result = new RaftLog<>(object, currentTerm.get());
        clientRequests.put(result);
        return result.get();
    }

    boolean deleteValue() {
        RaftLog<T> result = new RaftLog<>(null, currentTerm.get());
        clientRequests.put(result);
        return result.get();
    }

    public void printState() {
        System.out.println("\n");
        if (this.state.get() == RaftState.FOLLOWER) {
            System.out.println("Follower State");
        }
        if (this.state.get() == RaftState.CANDIDATE) {
            System.out.println("Candidate State");
        }
        if (this.state.get() == RaftState.LEADER) {
            System.out.println("Leader State");
        }

        System.out.println("Term: " + currentTerm);
        System.out.println("VotedFor: " + votedFor);
        System.out.println("Votes: " + votes);
        System.out.println("Log Index: " + Integer.toString(log.size() - 1));
        if (log.size() - 1 != 0)
            System.out.println("Last Log Term: " + log.get(log.size() - 1));
        else
            System.out.println("Last Log Term: " + 0);
        System.out.println("Old Logs");
        for (RaftLog entry : log) {
            System.out.print(entry.term + " ");
        }
        System.out.println("\n");
    }
}
