package server;

import net.packet.TV;
import net.packet.server.AuthenticationTV;
import net.packet.server.CreateRoomTV;
import net.packet.server.JoinRoomTV;
import net.packet.server.RegisterNewUserTV;
import net.packet.server.RenameRoomTV;
import packets.IncomingPacketController;

public class ServerPacketController extends IncomingPacketController<Client>{

	private Client parent;
	
	public ServerPacketController(Client parent) {
		this.parent = parent;
	}
	
	@Override
	protected void read() {
		while(position < data.length){
			//TV - Type Value
			byte type = readByte();
			System.out.println("Client Packet Type:"+type);
			TV<Client> tv= null;
			switch(type){
			case 0x01:
				tv = new AuthenticationTV();
				break;
			case 0x02:
				tv = new RegisterNewUserTV();
				break;
			case 0x20:
				tv = new CreateRoomTV();
				break;
			case 0x21:
				tv = new JoinRoomTV();
				break;
			case 0x22:
				tv = new RenameRoomTV();
				break;
			}
			
			tv.read(this);
			this.packet.data.add(tv);
		}
		
	}
	
	@Override
	public void run() {
		for(TV<Client> tv : packet.data){
			tv.run(parent);
		}
	}
}
