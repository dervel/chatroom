package server;

import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import chatroom.ChatRoom;
import net.GenericNetClient;
import net.PacketListenerThread;
import security.CryptController;

public class Client implements GenericNetClient{
	
	private Socket s;
	private boolean isAlive;
	private int id;
	
	private boolean authenticated = false;
	private String name = null;
	
	private PacketListenerThread packetListener;
	private ServerPacketFactory serverPacketFactory;
	private ServerPacketController packetController;
	private CryptController crypt;
	
	public Client(Socket s){
		isAlive = true;
		this.s = s;
		serverPacketFactory = new ServerPacketFactory(this);
		packetListener = new PacketListenerThread(this);
		packetController = new ServerPacketController(this);
		packetListener.start();
		
		try {
			crypt = new CryptController();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
			sendInitPacket();
		}catch(Exception e){
			ChatRoom.getController().getLocalServer().getServerLog().log(
					"Error trying to sent initial packet."+Utils.reportIP(s)
			);
			e.printStackTrace();
		}
	}
	
	public void sendInitPacket() throws Exception{
		String serverName = ChatRoom.getController().getLocalServer().getServerName();
		byte[] public_key = crypt.generateKey();
		serverPacketFactory.appendInitTV(serverName,public_key);
		serverPacketFactory.sendPacket();
	}
	
	public Socket getSocket(){
		return s;
	}
	
	public boolean isAlive(){
		return isAlive;
	}
	
	public void restartConnection(){
		
	}

	public void handle_packet(byte[] packet_data) {
		byte[] buffer = new byte[packet_data.length];
		try{
			if(crypt.cryptReady()){
				System.out.println("Encrypted:"+new String(packet_data)+" L:"+packet_data.length);
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
			//Send any TV
			serverPacketFactory.sendPacket();
		} catch (Exception e) {
			e.printStackTrace();
			restartConnection();
		}
	}

	public String getName() {
		return name;
	}

	public boolean setName(String name) {
		if(isAuthenticated())
			return false;
		this.name = name;
		return true;
	}
	
	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAsAuthenticated() {
		authenticated = true;
	}
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public ServerPacketFactory getPacketFactory(){
		return serverPacketFactory;
	}
	
	public CryptController getCrypt(){
		return crypt;
	}
}
