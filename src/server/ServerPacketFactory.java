package server;

import chatroom.StatusReturnMessages;
import net.GenericNetClient;
import net.PacketFactory;

public class ServerPacketFactory extends PacketFactory{
	
	public ServerPacketFactory(GenericNetClient parent) {
		super(parent);
	}

	public void appendInitTV(String serverName,byte[] publicKeyBytes){
		packet.writeByte(0x00);
		packet.writeString(serverName);
		packet.writeArray(publicKeyBytes);
	}
	
	public void appendReturnStatusTV(StatusReturnMessages status){
		packet.writeByte(0x01);
		packet.writeByte(status.getID());
	}
	
	public void appendCryptStartTV(){
		packet.writeByte(0x02);
	}
	
	public void appendSendMessageTV(Message msg){
		packet.writeByte(0x30);
		packet.writeLong(msg.senderID);
		packet.writeString(msg.msg);
		
	}
	
	
}
