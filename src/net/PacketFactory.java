package net;

import java.io.IOException;
import java.io.OutputStream;

import packets.OutgoingPacket;
import server.Utils;

public class PacketFactory {
	private GenericNetClient parent;
	protected OutgoingPacket packet = null;
	
	public PacketFactory(GenericNetClient parent){
		this.parent = parent;
		createNewPacket();
	}
	
	private void createNewPacket(){
		packet = new OutgoingPacket();
		packet.writeShort(0); //Reserve for packet length - overwrite during sending
	}
	
	public boolean sendPacket() throws IOException{
		if(packet == null)
			return false;
		
		packet.writePacketLength();
		
		System.out.println("PacketLength:"+packet.getPacketLength());
		OutputStream os = parent.getSocket().getOutputStream();
		os.write(packet.getData(), 0, packet.getPacketLength());
		os.flush();
		
		System.out.println("Packet Out "+Utils.reportIP(parent.getSocket())+parent.getSocket().getPort());
		createNewPacket();
		return true;
	}
}
