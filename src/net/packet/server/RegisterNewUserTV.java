package net.packet.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import chatroom.Config;
import packets.ReadablePacket;
import server.Client;
import server.Utils;

public class RegisterNewUserTV extends ServerTV {
	
	String name;
	String pass;

	@Override
	public void read(ReadablePacket controller) {
		name = controller.readString();
		pass = controller.readString();

	}
	
	@Override
	public void run(Client parent) {
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
			
			String hashed_pass = BCrypt.hashpw(pass, BCrypt.gensalt(Config.GENSALT_WORKLOAD));
			PreparedStatement ps1 = con.prepareStatement("INSERT INTO users (name,password) VALUES"+
			" (?,?);");
			ps1.setString(1, name);
			ps1.setString(2, hashed_pass);
			ps1.executeUpdate();
			
			con.close();
			
		} catch (SQLException e) {
			log.log("Smth went horribly wrong. Error:"+e.getMessage());
			try {
				con.close();
			} catch (SQLException e1) {}
			return;
		}

	}

}
