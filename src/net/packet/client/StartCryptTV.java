package net.packet.client;

import client.ServerConnection;
import packets.ReadablePacket;

public class StartCryptTV extends ClientTV{

	@Override
	public void read(ReadablePacket controller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(ServerConnection parent) {
		parent.getCrypt().setReady(true);
		
	}

}
