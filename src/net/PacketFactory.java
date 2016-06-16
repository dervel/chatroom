package net;

import java.io.OutputStream;

import packets.OutgoingPacket;

public class PacketFactory {
	private GenericNetClient parent;
	protected OutgoingPacket packet = new OutgoingPacket();
	
	public PacketFactory(GenericNetClient parent){
		this.parent = parent;
	}
	
	public boolean sendPacket() throws Exception{
		if(packet == null || packet.getPacketLength()<=2)
			return false;
		
		packet.writePacketLength();
		
		byte[] buffer = null;
		int length = 0;
		
		if(parent.getCrypt().cryptReady()){
			buffer = parent.getCrypt().encrypt(packet.getData(),packet.getPacketLength());
			length = buffer.length;
			System.out.println("Encrypted:"+new String(buffer)+" L:"+buffer.length);
		}else{
			buffer = packet.getData();
			length = packet.getPacketLength();
		}
		
		OutputStream os = parent.getSocket().getOutputStream();
		//Overhaul needed, ecrypt packet data but not the length
		//Add packet to start encrypting/decrypting
		
		os.write(buffer, 0, length);
		os.flush();
		
		packet.resetPacket();
		return true;
	}
}
