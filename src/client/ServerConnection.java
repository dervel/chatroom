package client;

import java.io.IOException;
import java.net.Socket;

import net.GenericNetClient;
import net.PacketListenerThread;

public class ServerConnection implements GenericNetClient{
	private Socket server = null;
	private PacketListenerThread packetListener;
	private ClientPacketFactory packetFactory;
	private ClientPacketController packetController;
	private String serverIP;
	private int serverPort;
	private boolean isAlive;
	private String name;
	private String pass;
	private String serverName;
	private boolean hasConnected = false;
	
	public ServerConnection(String ip, int port){
		this.serverIP = ip;
		this.serverPort = port;
		packetFactory = new ClientPacketFactory(this);
		packetController = new ClientPacketController(this);
	}
	
	public void setCredentials(String name,String password){
		this.name = name;
		this.pass = password;
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
		packetListener = new PacketListenerThread(this);
		packetListener.start();
		hasConnected = true;
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
		packetController.handlePacket(data);
	}

	@Override
	public void restartConnection() {
		
	}
	
	public ClientPacketFactory getClientPacketFactory(){
		return packetFactory;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public boolean hasConnected() {
		return hasConnected;
	}
	
	public void addPacketListener(PacketListener<ServerConnection> pl){
		packetController.addPacketListener(pl);
	}
	
	public void removePacketListener(PacketListener<ServerConnection> pl){
		packetController.removePacketListener(pl);
	}
	
	
	
}
