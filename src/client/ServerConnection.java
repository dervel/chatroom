package client;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import net.GenericNetClient;
import net.PacketListenerThread;
import security.CryptController;

public class ServerConnection implements GenericNetClient{
	private Socket server = null;
	private PacketListenerThread packetListener;
	private ClientPacketFactory packetFactory;
	private ClientPacketController packetController;
	private CryptController crypt;
	
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
		
		try {
			crypt = new CryptController();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	public void handle_packet(byte[] packet_data) {
		byte[] buffer = new byte[packet_data.length];
		try{
			if(crypt.canEncrypt()){
				buffer = crypt.decrypt(packet_data);
			}else{
				System.arraycopy(packet_data, 0, buffer, 0, packet_data.length);
			}
		}catch(Exception e){
			System.arraycopy(packet_data, 0, buffer, 0, packet_data.length);
			e.printStackTrace();
		}
		
		packetController.handlePacket(buffer);
		try {
			this.packetFactory.sendPacket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public CryptController getCrypt(){
		return crypt;
	}
	
	
}
