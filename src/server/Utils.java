package server;

import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import chatroom.Config;

public class Utils {
	public static String reportIP(Socket s){
		return new String("(IP:"+s.getInetAddress().toString()+")");
	}
	
	public static String reportName(String name){
		return new String("(Name:"+name+")");
	}
	
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	public static Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Config.DB_URL, Config.DB_USER, Config.DB_PASSWORD);
			return con;
		} catch (SQLException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
