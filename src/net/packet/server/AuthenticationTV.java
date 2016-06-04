package net.packet.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import packets.ReadablePacket;
import server.Client;
import server.Utils;

public class AuthenticationTV extends ServerTV {

	String name;
	String pass;
	
	@Override
	public void read(ReadablePacket controller) {
		name = controller.readString();
		pass = controller.readString();

	}

	@Override
	public void run(Client parent) {
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

}
