package server;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import chatroom.Config;

public class Server {
	private ConnectionController cc = null;
	private List<Client> clients = new ArrayList<Client>(Config.CLIENT_SIZE_ARRAY);
	private Hashtable<Long,Room> rooms = new Hashtable<Long,Room>(Config.INITIAL_MEMBERS_PROOM);
	private ServerLog log = new ServerLog();
	
	public void start(int port) throws IOException, AlreadyRunningException{
		if(cc == null)
			cc = new ConnectionController(port,this);
		cc.startAcceptingConnections();
		log.log("Server Started running at port "+port);
	}
	
	public void stop() throws IOException, NotRunningException{
		if(cc == null) return;
		cc.stopAcceptingConnections();
		log.log("Server has stopped");
	}
	
	public void newConnection(Socket s){
		Client newClient = new Client(s);
		
		
		
		clients.add(newClient);
	}
	
	public boolean isAcceptingConnections(){
		return cc.isRunning();
	}
	
	public int getConnectedPeopleNumber(){
		return clients.size();
	}
	
	public ServerLog getServerLog(){
		return log;
	}
	
	public boolean createNewRoom(String roomName, Client creator){
		Room newRoom = new Room();
		newRoom.setName(roomName);
		newRoom.setFounder_ID(creator.getID());
		
		Connection con;
		try {
			con = DriverManager.getConnection("127.0.0.1", Config.DB_USER, Config.DB_PASSWORD);
			PreparedStatement st = con.prepareStatement("INSERT INTO ROOMS (id,roomname) VALUES (?,?);");
			st.setNull(1, java.sql.Types.NULL);
			st.setString(2, roomName);
			st.executeQuery();
			
			st = con.prepareStatement("SELECT LAST_INSERT_ID();");
			ResultSet rs = st.executeQuery();
			long room_id = rs.getLong(1);
			
			rooms.put(room_id, newRoom);
			return true;
		} catch (SQLException e) {
			log.log("Could not create new room. Reason:"+e.getMessage());
			return false;
		}
		
		
	}
}
