package net;

import java.io.IOException;
import java.io.InputStream;

import chatroom.Config;

public class PacketListenerThread {

	private Thread packetListener;
	private InputStream is;
	private GenericNetClient parent;

	public PacketListenerThread(GenericNetClient parent) {
		this.parent = parent;
	}

	public void start() {
		packetListener = new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] data = new byte[Config.PACKET_BUFFER_SIZE];
				int position = 0;
				int packet_length = 0;
				
				try{
					is = parent.getSocket().getInputStream();
					
					while (parent.isAlive()) {
						try {
							Thread.sleep(Config.INPUTSTREAM_WAIT);
						} catch (InterruptedException e) {
							//Do nothing
						}
						
						if (is.available() == 0)
							continue;
	
						int data_length = is.read(data, position, is.available());
						position += data_length;
						
						if(packet_length == 0 && position > 2){
							packet_length = (data[1] << 8) & 0xFF;
							packet_length += data[0] & 0xFF;
						}
						
						if(packet_length!=0 && position >= data_length){
							byte[] temp = new byte[packet_length];
							System.arraycopy(data, 0, temp, 0, packet_length);
							
							if(position > packet_length){
								System.arraycopy(data, packet_length, data, 0, position - packet_length);
								position = position - packet_length;
								packet_length = 0;
							}else{
								position = 0;
								packet_length = 0;
							}
							
							System.out.println("Packet Complete");
							parent.handle_packet(temp);
						}
					}
				}catch(IOException e){
					e.printStackTrace();
					parent.restartConnection();
				}
			}
		});
		packetListener.start();
	}
}
