package packets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chatroom.ChatRoom;
import server.Client;
import server.ServerLog;
import server.Utils;

public class ServerPacketController extends IncomingPacket{

	private Client parent;
	private ServerLog log = ChatRoom.getController().getLocalServer().getServerLog();
	
	public ServerPacketController(byte[] data, Client parent) {
		super(data);
		this.parent = parent;
	}

	@Override
	public void implement() {
		
	}

	@Override
	public void run() {
		while(position < data.length){
			//TV - Type Value
			short type = readByte();
			switch(type){
			case 0x00:
				Authentication();
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
		
		Connection con = null;
		try {
			con = Utils.getConnection();
			if(con == null){
				log.log("Could not create connection"+Utils.reportIP(parent.getSocket()));
				return;
			}
			PreparedStatement ps = con.prepareStatement("SELECT id,password FROM users WHERE name =?");
			ps.setString(1, name);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			if(!rs.isLast()){
				log.log("Duplicate Account");
				con.close();
				return;
			}
			String password = rs.getString(2);
			if(!password.equals(pass)){
				log.log("Wrong Password");
				con.close();
				return;
			}
			
			parent.setName(name);
			parent.setAsAuthenticated();
			log.log("New user named "+parent.getName()+" connected. IP:"+
			parent.getSocket().getInetAddress().toString());
			
			con.close();
			
		} catch (SQLException e) {
			log.log("Smth went wrong. Error:"+e.getMessage());
			try {
				con.close();
			} catch (SQLException e1) {}
			return;
		}
		
	}
	
	private void CreateRoom(){
		String roomName = readString();
		
		if(!parent.isAuthenticated()){
			log.log("Unauthenticated User tried to create a room"+reportIP()+".");
			return;
		}
		
		if(roomName.length() == 0){
			log.log("Can't create a room with no name. By - "+reportName()+reportIP());
			return;
		}
		
		ChatRoom.getController().getLocalServer().createNewRoom(roomName, parent);
		
	}
	
	private void JoinRoom(){
		
	}
	
	private void RenameRoom(){
		
	}

	public String reportIP(){
		return new String("(IP:"+parent.getSocket().getInetAddress().toString()+")");
	}
	public String reportName(){
		return new String("(Name:"+parent.getName()+")");
	}
}
