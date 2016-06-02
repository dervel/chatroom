package server;

import net.GenericNetClient;
import net.PacketFactory;

public class ServerPacketFactory extends PacketFactory{
	
	public ServerPacketFactory(GenericNetClient parent) {
		super(parent);
	}

	public void appendInitPacket(String serverName){
		packet.writeByte(0x00);
		packet.writeString(serverName);
	}
	
	public void appendSendMessage(Message msg){
		packet.writeByte(0x30);
		packet.writeLong(msg.senderID);
		packet.writeString(msg.msg);
		
	}
	
	
}
