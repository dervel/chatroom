package server;

import java.util.ArrayList;
import java.util.List;

import chatroom.ChatRoom;
import chatroom.Config;

public class Room {
	private List<Client> members = new ArrayList<Client>(Config.INITIAL_MEMBERS_PROOM);
	private ServerLog log = ChatRoom.getController().getLocalServer().getServerLog();
	
	private int founder_id;
	private String name;
	
	private List<Message> messages = new ArrayList<Message>(Config.ROOM_MSG_ARRAYSIZE);
	
	public void setFounder_ID(int founder_id) {
		this.founder_id = founder_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void addMember(Client newClient){
		members.add(newClient);
	}
	
	public void relayMessage(Message msg){
		if(messages.size() > Config.ROOM_MSG_ARRAYSIZE)
			messages.remove(0);
		messages.add(msg);
		
		for(Client c : members){
			try {
				c.getPacketFactory().appendSendMessageTV(msg);
				c.getPacketFactory().sendPacket();
			} catch (Exception e) {
				log.log("Could not send message to "+Utils.reportIP(c.getSocket()));
			}
		}
	}
}
