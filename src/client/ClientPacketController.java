package client;

import net.packet.TV;
import net.packet.client.InitTV;
import net.packet.client.ReturnStatusTV;
import packets.IncomingPacketController;

public class ClientPacketController extends IncomingPacketController<ServerConnection>{
	
	private ServerConnection parent;
	
	public ClientPacketController(ServerConnection parent) {
		this.parent = parent;
	}
	
	@Override
	protected void read() {
		while(position < data.length){
			//TV - Type Value
			byte type = readByte();
			System.out.println("Client Caught Packet Type:"+type);
			TV<ServerConnection> tv= null;
			switch(type){
			case 0x00:
				tv = new InitTV();
				break;
			case 0x01:
				tv = new ReturnStatusTV();
				break;
			}
			
			tv.read(this);
			this.packet.data.add(tv);
		}
		
	}

	@Override
	public void run() {
		for(TV<ServerConnection> tv : packet.data){
			tv.run(parent);
		}
		packet.data.clear();
	}
}
