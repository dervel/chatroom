package client;

import java.io.IOException;
import java.net.Socket;

import net.GenericNetClient;
import net.PacketController;

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
		packetFactory = new ClientPacketFactory(this);
	}
	
	public void sendAuthenticationPacket() throws IOException{
		packetFactory.appendAuthenticationTV(name,pass);
		packetFactory.sendPacket();
	}
	
	public void connect() throws IOException{
		if(server != null){
			try{
				server.close();
			}catch(IOException e){
				
			}
		}
		
		isAlive = true;
		server = new Socket(serverIP,serverPort);
		//server.connect(new InetSocketAddress(serverIP,serverPort), Config.CONNECT_WAITOUT);
		packetListener = new PacketController(this);
		packetListener.start();
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
		ClientPacketController newPacket = new ClientPacketController(data, this);
		newPacket.run();	
	}

	@Override
	public void restartConnection() {
		
	}
	
	public ClientPacketFactory getClientPacketFactory(){
		return packetFactory;
	}
	
}
