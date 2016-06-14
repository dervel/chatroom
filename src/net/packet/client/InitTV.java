package net.packet.client;

import client.ServerConnection;
import packets.ReadablePacket;

public class InitTV extends ClientTV{
	String serverName;
	byte[] publicKeyBytes;
	
	@Override
	public void read(ReadablePacket controller) {
		this.opcode = 0x00;
		serverName = controller.readString();
		publicKeyBytes = controller.readArray();
		
	}

	@Override
	public void run(ServerConnection parent) {
		parent.setServerName(serverName);
		parent.getCrypt().setRemotePublicKey(publicKeyBytes);
		
		parent.getClientPacketFactory().appendInitResponseTV(parent.getCrypt().getPublicKeyAsArray());
		
	}

}
