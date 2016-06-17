package server;

import net.packet.Packet;
import net.packet.server.AuthenticationTV;
import net.packet.server.CreateRoomTV;
import net.packet.server.InitResponseTV;
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
		//TV - Type Value
		byte type = readByte();
		System.out.println("Server Caught Packet Type:"+type);
		Packet<Client> tv= null;
		switch(type){
		case 0x00:
			tv = new InitResponseTV();
			break;
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
		this.packet = tv;
		
	}
	
	@Override
	public void run() {
		packet.run(parent);
	}
}
