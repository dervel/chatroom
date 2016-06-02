package server;

import java.net.Socket;

import net.GenericNetClient;
import net.PacketController;
import packets.ServerPacketController;

public class Client implements GenericNetClient{
	
	private Socket s;
	private boolean isAlive;
	private int id;
	
	private boolean authenticated = false;
	private String name = null;
	
	private PacketController packetController;
	private ServerPacketFactory serverPacketFactory;
	
	public Client(Socket s){
		isAlive = true;
		this.s = s;
		serverPacketFactory = new ServerPacketFactory();
		packetController = new PacketController(this);
		packetController.start();
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
		ServerPacketController p = new ServerPacketController(packet_data,this);
		p.run();
		
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
