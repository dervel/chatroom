package server;

public class Message {
	
	public long senderID;
	public long messageID;
	public String msg;
	public Message(long senderID, long messageID, String msg) {
		this.senderID = senderID;
		this.messageID = messageID;
		this.msg = msg;
	}
}
