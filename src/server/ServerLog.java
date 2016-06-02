package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerLog {
	
	private List<String> messages = new ArrayList<String>(110);
	private int counter=0;
	
	public void log(String msg){
		if(messages.size() > 100)
			messages.remove(0);
		messages.add(msg);
		counter++;
		
	}
	
	public Iterator<String> getAllMessages(){
		return messages.iterator();
	}
	
	public int getCounter(){
		return counter;
	}
}
