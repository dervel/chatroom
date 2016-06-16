package net.packet.server;

import packets.ReadablePacket;
import server.Client;

public class InitResponseTV extends ServerTV{

	public byte[] publicKeyBytes;
	
	@Override
	public void read(ReadablePacket controller) {
		publicKeyBytes = controller.readArray();
		
	}

	@Override
	public void run(Client parent) {
		try{
			parent.getCrypt().autoDecryptSymmetricalKey(publicKeyBytes);
			parent.getPacketFactory().appendCryptStartTV();
			
			//Send the packet now and then start encrypting
			try{
				parent.getPacketFactory().sendPacket();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			parent.getCrypt().setReady(true);
		}catch(Exception e){
			parent.restartConnection();
			e.printStackTrace();
		}
		
	}

}
