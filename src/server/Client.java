package server;

import java.io.IOException;
import java.net.Socket;

import chatroom.ChatRoom;
import net.GenericNetClient;
import net.PacketFactory;
import net.PacketListenerThread;

public class Client implements GenericNetClient{
	
	private Socket s;
	private boolean isAlive;
	private int id;
	
	private boolean authenticated = false;
	private String name = null;
	
	private PacketListenerThread packetListener;
	private ServerPacketFactory serverPacketFactory;
	private ServerPacketController packetController;
	
	public Client(Socket s){
		isAlive = true;
		this.s = s;
		serverPacketFactory = new ServerPacketFactory(this);
		packetListener = new PacketListenerThread(this);
		packetController = new ServerPacketController(this);
		packetListener.start();
		
		try{
			sendInitPacket();
		}catch(IOException e){
			ChatRoom.getController().getLocalServer().getServerLog().log(
					"Error trying to sent initial packet."+Utils.reportIP(s)
			);
		}
	}
	
	public void sendInitPacket() throws IOException{
		serverPacketFactory.appendInitTV(ChatRoom.getController().getLocalServer().getServerName());
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
		packetController.handlePacket(packet_data);
		try {
			serverPacketFactory.sendPacket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
