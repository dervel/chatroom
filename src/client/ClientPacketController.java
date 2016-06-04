package client;

import java.io.IOException;

import net.packet.client.ClientTV;
import packets.IncomingPacketController;

public class ClientPacketController extends IncomingPacketController<ClientTV>{
	
	private ServerConnection parent;
	
	public ClientPacketController(ServerConnection parent) {
		this.parent = parent;
	}
	
	@Override
	protected void read() {
		while(position < data.length){
			//TV - Type Value
			short type = readByte();
			System.out.println("Client Packet Type:"+type);
			switch(type){
			case 0x00:
				Init();
				break;
			}
		}
		
	}

	@Override
	public void run() {
		for(ClientTV tv : packet.data){
			tv.run(parent);
		}
		while(position < data.length){
			//TV - Type Value
			short type = readByte();
			System.out.println("Client Packet Type:"+type);
			switch(type){
			case 0x00:
				Init();
				break;
			}
		}
		
	}
	
	private void Init(){
		String serverName = readString();
		
		parent.setServerName(serverName);
		
		if(parent.isAutoconnect()){
			try {
				parent.sendAuthenticationPacket();
			} catch (IOException e) {
				e.printStackTrace();
				parent.restartConnection();
			}
		}
	}
}
