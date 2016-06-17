package net.packet.server;

import chatroom.ChatRoom;
import net.packet.Packet;
import server.Client;
import server.ServerLog;

public abstract class ServerTV extends Packet<Client>{
	public ServerLog log = ChatRoom.getController().getLocalServer().getServerLog();
}
