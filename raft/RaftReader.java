package raft;

import java.util.Collection;
import java.util.UUID;

import raft.Raft.ServerState;

public class RaftReader implements Runnable{
	private RaftCommunication raftComm;
	
	public RaftReader(RaftCommunication raftComm) {
		this.raftComm = raftComm;
	}

	@Override
	public void run() {
		while (raftComm.raft.serverState.get() != ServerState.TERMINATING) {
			String message = raftComm.channel.receiveString();
			
			if(message == null) {
				continue;
			}


			String[] messageArray = message.split("\n");
			String reply = null;
			int term;
			
			switch(messageArray[0]) {
			case RPC.callAppendEntriesRPC:

                System.out.println("Receive Append Entry " + message );

                //1
				term = Integer.parseInt(messageArray[1]);
                raftComm.raft.followerTimerTask.cancel();

				if(term > raftComm.raft.currentTerm.get()){
				    if(raftComm.raft.state.get()!= RaftState.FOLLOWER) {
                        raftComm.raft.state.set(RaftState.FOLLOWER);
                        System.out.println("Change State to Follower");
                    }
                }
				if(term < raftComm.raft.currentTerm.get()) {
					reply = RPC.retAppendEntries(raftComm.raft, false);
					break;
				}
				
				//2
				int prevLogIndex = Integer.parseInt(messageArray[3]);
				long prevLogTerm = Long.parseLong(messageArray[4]);
				
				if(prevLogIndex >= raftComm.raft.log.size()) {
					reply = RPC.retAppendEntries(raftComm.raft, false);
					break;
				}
				
				if(((RaftLog)raftComm.raft.log.get(prevLogIndex)).term != prevLogTerm) {
					reply = RPC.retAppendEntries(raftComm.raft, false);
					break;
				}

				//3
				//TODO
				break;
			case RPC.callRequestVoteRPC:
				//1
				term = Integer.parseInt(messageArray[1]);



                if(term < raftComm.raft.currentTerm.get()) {
					reply = RPC.retRequestVote(raftComm.raft, false);
					break;
				}

				UUID candidateID = UUID.fromString(messageArray[2]);

				int lastLogIndex = Integer.parseInt(messageArray[3]);
				int lastLogTerm = Integer.parseInt(messageArray[4]);

				//2
				if (raftComm.raft.votedFor.get() == null || raftComm.raft.votedFor.get() == candidateID) {
					int lastReceiverLogTerm = ((RaftLog) raftComm.raft.log.get(raftComm.raft.log.size() - 1)).term;


					if(lastReceiverLogTerm != lastLogTerm) {
						if(lastReceiverLogTerm < lastLogTerm){
							raftComm.raft.votedFor.set(candidateID);
							reply = RPC.retRequestVote(raftComm.raft, true);
						}
						else {
							reply = RPC.retRequestVote(raftComm.raft, false);
						}

						break;
					}

                    System.out.println(raftComm.raft.log.size()-1);
                    System.out.println(lastLogIndex);
					if(raftComm.raft.log.size()-1 <= lastLogIndex) {
						raftComm.raft.votedFor.set(candidateID);
						reply = RPC.retRequestVote(raftComm.raft, true);
						break;
					}
				}
                System.out.println("Nada");
				reply = RPC.retRequestVote(raftComm.raft, false);

				break;
			case RPC.retAppendEntriesRPC:
			    reply = " ";
				break;
			case RPC.retRequestVoteRPC:
				term = Integer.parseInt(messageArray[1]);
				boolean gotAVote = Boolean.parseBoolean(messageArray[2]);
				
				if (term > raftComm.raft.currentTerm.get()) {
					raftComm.raft.candidateTimerTask.cancel();
					raftComm.raft.state.set(RaftState.FOLLOWER);
                    System.out.println("Change State to Follower");
					break;
				}
				
				if(gotAVote) {
					raftComm.raft.votes.incrementAndGet();
				}
				
				if (raftComm.raft.votes.get() > (raftComm.raft.cluster.size() + 1) / 2) {
					raftComm.raft.candidateTimerTask.cancel();
					raftComm.raft.state.set(RaftState.LEADER);
                    System.out.println("Change state to Leader ");
					raftComm.raft.leaderID = raftComm.raft.ID;
					raftComm.raft.synchronize.set(true);
					for (RaftCommunication node : (Collection<RaftCommunication>) raftComm.raft.cluster.values()) {
						node.nextIndex = raftComm.raft.log.size();
						node.matchIndex = 0;
					}
					raftComm.raft.synchronize.set(false);
					break;
				}
				
				break;
			}
			
			if(reply != null) {
				raftComm.raft.followerTimerTask.cancel(); // TODO temporary
				raftComm.queue.put(reply);
			}
		}
	}

}
