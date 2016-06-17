package client;

import net.packet.Packet;
import net.packet.client.InitTV;
import net.packet.client.ReturnStatusTV;
import net.packet.client.StartCryptTV;
import packets.IncomingPacketController;

public class ClientPacketController extends IncomingPacketController<ServerConnection>{
	
	private ServerConnection parent;
	
	public ClientPacketController(ServerConnection parent) {
		this.parent = parent;
	}
	
	@Override
	protected void read() {
		//TV - Type Value
		byte type = readByte();
		System.out.println("Client Caught Packet Type:"+type);
		Packet<ServerConnection> tv= null;
		switch(type){
		case 0x00:
			tv = new InitTV();
			break;
		case 0x01:
			tv = new ReturnStatusTV();
			break;
		case 0x02:
			tv = new StartCryptTV();
			break;
		}
	
		tv.read(this);
		this.packet = tv;
		
	}

	@Override
	public void run() {
		packet.run(parent);
	}
}
