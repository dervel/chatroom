package net;

import java.io.IOException;
import java.io.OutputStream;

import packets.OutgoingPacket;

public class PacketFactory {
	private GenericNetClient parent;
	protected OutgoingPacket packet = new OutgoingPacket();
	
	public PacketFactory(GenericNetClient parent){
		this.parent = parent;
	}
	
	public boolean sendPacket() throws IOException{
		if(packet == null || packet.getPacketLength()<=2)
			return false;
		
		packet.writePacketLength();
		
		OutputStream os = parent.getSocket().getOutputStream();
		//Overhaul needed, ecrypt packet data but not the length
		//Add packet to start encrypting/decrypting
		
		os.write(packet.getData(), 0, packet.getPacketLength());
		os.flush();
		
		packet.resetPacket();
		return true;
	}
}
