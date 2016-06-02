package chatroom;

import java.util.ArrayList;
import java.util.List;

import client.ServerConnection;
import server.Server;

public class MainController {
	
	private Server localServer = null;
	
	private List<ServerConnection> joinedServers = new ArrayList<ServerConnection>();
	
	
	public Server getLocalServer(){
		if(localServer == null)
			localServer = new Server();
		return localServer;
	}
	
	public void addServerConnection(ServerConnection newConnection){
		joinedServers.add(newConnection);
	}
	
	public ServerConnection getElementAt(int index){
		return joinedServers.get(index);
	}
	
	public int getConecctedServersCount(){
		return joinedServers.size();
	}
	
}
