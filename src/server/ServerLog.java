package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chatroom.Config;

public class ServerLog {
	
	private List<String> messages = new ArrayList<String>(Config.SERVER_LOG_SIZE);
	private int counter=0;
	
	public void log(String msg){
		if(messages.size() > Config.SERVER_LOG_SIZE)
			messages.remove(0);
		messages.add(msg);
		counter++;
		
	}
	
	public Iterator<String> getAllMessages(){
		return messages.iterator();
	}
	
	public String getMessageAt(int i){
		return messages.get(i);
	}
	
	public int getCounter(){
		return counter;
	}
}
