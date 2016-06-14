package net.packet.client;

import client.ServerConnection;
import packets.ReadablePacket;

public class ReturnStatusTV extends ClientTV{
	
	public int statusID;

	@Override
	public void read(ReadablePacket controller) {
		this.opcode = 0x01;
		statusID = controller.readByte();
		
	}

	@Override
	public void run(ServerConnection parent) {
		/*
		 * Nothing to do here
		 * This is catch by the packet listeners
		 */
		
	}

}
