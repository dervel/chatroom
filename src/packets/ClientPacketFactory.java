package packets;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientPacketFactory {
	
	private OutgoingPacket packet = null;
	
	public void createNewPacket(){
		packet = new OutgoingPacket();
		packet.writeShort(0); //Reserve for packet length - overwrite during sending
	}
	
	public boolean sendPacket(Socket s) throws IOException{
		if(packet == null)
			return false;
		
		packet.writePacketLength();
		OutputStream os = s.getOutputStream();
		os.write(packet.getData(), 0, packet.getPacketLength());
		os.flush();
		
		return true;
	}
	
	public void appendAuthenticationTV(String name, String password){
		packet.writeByte(0x00);
		packet.writeString(name);
		packet.writeString(password);
	}
}
