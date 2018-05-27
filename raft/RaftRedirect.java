package raft;

import java.io.Serializable;
import java.util.Arrays;

import raft.net.ssl.SSLChannel;

public class RaftRedirect<T extends Serializable> implements Runnable{ //this class is used by the leader when a client requests an action
	private Raft raft;
	private SSLChannel channel;
	private String obj;
	private RaftCommand command;
	
	public RaftRedirect(Raft raft, SSLChannel channel, String obj, RaftCommand command) {
		this.raft = raft;
		this.channel = channel;
		this.obj = obj;
		this.command = command;
	}

	@Override
	public void run() {
		T object = Raft.deserialize(obj.getBytes());
		//TODO
		
		channel.send(RPC.retSetValue(true));
	}
}
