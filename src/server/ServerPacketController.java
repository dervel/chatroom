package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import chatroom.ChatRoom;
import net.packet.server.ServerTV;
import packets.IncomingPacketController;

public class ServerPacketController extends IncomingPacketController<ServerTV>{

	private Client parent;
	private ServerLog log = ChatRoom.getController().getLocalServer().getServerLog();
	
	public ServerPacketController(Client parent) {
		this.parent = parent;
	}
	
	@Override
	protected void read() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		while(position < data.length){
			//TV - Type Value
			short type = readByte();
			System.out.println("Server Packet Type:"+type);
			switch(type){
			case 0x01:
				Authentication();
				break;
			case 0x02:
				RegisterNewUser();
				break;
			case 0x20:
				CreateRoom();
				break;
			case 0x21:
				JoinRoom();
				break;
			case 0x22:
				RenameRoom();
				break;
				
			
			}
		}
		
	}
	
	private void Authentication(){
		String name = readString();
		String pass = readString();
		
		if(parent.isAuthenticated()){
			log.log("User "+name+" tried to authenticate twice");
			return;
		}
		
		Connection con = Utils.getConnection();
		if(con == null){
			log.log("Could not create connection"+Utils.reportIP(parent.getSocket()));
			return;
		}
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id,password FROM users WHERE name =?");
			ps.setString(1, name);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			String stored_hash = rs.getString(2);
			con.close();
			
			if(!BCrypt.checkpw(pass, stored_hash)){
				log.log("Wrong Password by "+Utils.reportIP(parent.getSocket()));
				return;
			}
			
			parent.setName(name);
			parent.setAsAuthenticated();
			log.log("New user named "+parent.getName()+" connected. IP:"+
			parent.getSocket().getInetAddress().toString());
			
		} catch (SQLException e) {
			log.log("Smth went horribly wrong. Error:"+e.getMessage());
			try {
				con.close();
			} catch (SQLException e1) {}
			return;
		}
		
	}
	
	private void RegisterNewUser(){
		String name = readString();
		String hashed_pass = readString();
		
		Connection con = Utils.getConnection();
		if(con == null){
			log.log("Could not create connection"+Utils.reportIP(parent.getSocket()));
			return;
		}
		
		
		try {
			PreparedStatement ps = con.prepareStatement("SELECT count(*) FROM users WHERE name = ?");
			ps.setString(1, name);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			boolean nameAvailable = (rs.getInt(1) == 0 ? true : false );
			if(!nameAvailable){
				log.log("Name already taken.");
				con.close();
				return;
			}
			
			PreparedStatement ps1 = con.prepareStatement("INSERT INTO users (name,pass) VALUES"+
			" (?,?);");
			ps1.setString(1, name);
			ps1.setString(2, hashed_pass);
			ps1.executeQuery();
			
			con.close();
			
		} catch (SQLException e) {
			log.log("Smth went horribly wrong. Error:"+e.getMessage());
			try {
				con.close();
			} catch (SQLException e1) {}
			return;
		}
	}
	
	private void CreateRoom(){
		String roomName = readString();
		
		if(!parent.isAuthenticated()){
			log.log("Unauthenticated User tried to create a room"+Utils.reportIP(parent.getSocket())+".");
			return;
		}
		
		if(roomName.length() == 0){
			log.log("Can't create a room with no name. By - "+Utils.reportName(parent.getName())+Utils.reportIP(parent.getSocket()));
			return;
		}
		
		ChatRoom.getController().getLocalServer().createNewRoom(roomName, parent);
		
	}
	
	private void JoinRoom(){
		
	}
	
	private void RenameRoom(){
		
	}
}
