package net.packet.server;

import chatroom.ChatRoom;
import packets.ReadablePacket;
import server.Client;
import server.Utils;

public class CreateRoomTV extends ServerTV {

	String roomName;
	
	@Override
	public void read(ReadablePacket controller) {
		roomName = controller.readString();

	}

	@Override
	public void run(Client parent) {
		if(!parent.isAuthenticated()){
			log.log("Unauthenticated User tried to create a room"+Utils.reportIP(parent.getSocket())+".");
			return;
		}
		
		if(roomName.length() == 0){
			log.log("Can't create a room with no name. By - "+Utils.reportName(parent.getName())+Utils.reportIP(parent.getSocket()));
			return;
		}
		
		ChatRoom.getController().getLocalServer().createNewRoom(roomName, parent);

	}

}
