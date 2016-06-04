package net.packet.client;

import java.io.IOException;

import client.ServerConnection;
import packets.ReadablePacket;

public class InitTV extends ClientTV{
	String serverName;
	
	@Override
	public void read(ReadablePacket controller) {
		serverName = controller.readString();
		
	}

	@Override
	public void run(ServerConnection parent) {
		parent.setServerName(serverName);
		
		if(parent.isAutoconnect()){
			try {
				parent.sendAuthenticationPacket();
			} catch (IOException e) {
				e.printStackTrace();
				parent.restartConnection();
			}
		}
		
	}

}
