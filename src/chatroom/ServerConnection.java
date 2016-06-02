package chatroom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import net.GenericNetClient;
import net.PacketController;
import packets.ClientPacketFactory;

public class ServerConnection implements GenericNetClient{
	private Socket server = null;
	private PacketController packetListener;
	private ClientPacketFactory packetFactory;
	private String serverIP;
	private int serverPort;
	private boolean isAlive;
	private String name;
	private String pass;
	
	public ServerConnection(String ip, int port,String name, String pass){
		this.serverIP = ip;
		this.serverPort = port;
		this.name = name;
		this.pass = pass;
		packetFactory = new ClientPacketFactory();
	}
	
	public void connect() throws IOException{
		if(server != null){
			try{
				server.close();
			}catch(IOException e){
				
			}
		}
		
		isAlive = true;
		server = new Socket();
		server.connect(new InetSocketAddress(serverIP,serverPort), Config.CONNECT_WAITOUT);
		packetListener = new PacketController(this);
		packetListener.start();
		
		packetFactory.createNewPacket();
		packetFactory.appendAuthenticationTV(name,pass);
		packetFactory.sendPacket(server);
		
	}

	@Override
	public Socket getSocket() {
		return server;
	}

	@Override
	public boolean isAlive() {
		return isAlive;
	}

	@Override
	public void handle_packet(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restartConnection() {
		// TODO Auto-generated method stub
		
	}
}
