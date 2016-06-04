package server;

import net.GenericNetClient;
import net.PacketFactory;

public class ServerPacketFactory extends PacketFactory{
	
	public ServerPacketFactory(GenericNetClient parent) {
		super(parent);
	}

	public void appendInitTV(String serverName){
		packet.writeByte(0x00);
		packet.writeString(serverName);
	}
	
	public void appendRegisterSucessTV(){
		packet.writeByte(0x01);
	}
	
	public void appendRegisterFailedTV(String reason){
		packet.writeByte(0x02);
		packet.writeString(reason);
	}
	
	public void appendSendMessageTV(Message msg){
		packet.writeByte(0x30);
		packet.writeLong(msg.senderID);
		packet.writeString(msg.msg);
		
	}
	
	
}
