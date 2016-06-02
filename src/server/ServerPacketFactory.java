package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import packets.OutgoingPacket;

public class ServerPacketFactory {
	
	private OutgoingPacket packet = null;
	
	public void createNewPacket(){
		packet = new OutgoingPacket();
		packet.writeShort(0); //Reserve for packet length - overwrite during sending
	}
	
	public boolean sendPacket(Socket s) throws IOException{
		if(packet == null)
			return false;
		
		packet.writePacketLength();
		OutputStream os = s.getOutputStream();
		os.write(packet.getData(), 0, packet.getPacketLength());
		os.flush();
		
		return true;
	}
	
	public void appendSendMessage(Message msg){
		createPacketIfNeeded();
		
		packet.writeByte(0x30);
		packet.writeLong(msg.senderID);
		packet.writeString(msg.msg);
		
	}
	
	private void createPacketIfNeeded(){
		if(packet == null)
			createNewPacket();
	}
}
