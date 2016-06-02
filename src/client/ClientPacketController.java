package client;

import java.io.IOException;

import packets.IncomingPacket;

public class ClientPacketController extends IncomingPacket{
	
	private ServerConnection parent;
	
	public ClientPacketController(byte[] data, ServerConnection parent) {
		super(data);
		this.parent = parent;
	}

	@Override
	public void implement() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
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
