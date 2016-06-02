package client;

import net.GenericNetClient;
import net.PacketFactory;

public class ClientPacketFactory extends PacketFactory{
	
	public ClientPacketFactory(GenericNetClient parent) {
		super(parent);
	}
	
	public void appendAuthenticationTV(String name, String password){
		packet.writeByte(0x01);
		packet.writeString(name);
		packet.writeString(password);
	}
}
