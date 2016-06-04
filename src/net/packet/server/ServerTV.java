package net.packet.server;

import chatroom.ChatRoom;
import net.packet.TV;
import server.Client;
import server.ServerLog;

public abstract class ServerTV extends TV<Client>{
	public ServerLog log = ChatRoom.getController().getLocalServer().getServerLog();
}
