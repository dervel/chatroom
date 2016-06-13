package net.packet;

import java.util.ArrayList;
import java.util.List;

import net.packet.client.ReturnStatusTV;

public class PacketData<E> {
	public List<TV<E>> data = new ArrayList<TV<E>>();
	
	public boolean contains(int tvID){
		for(TV<E> tv : data){
			if(tv.opcode == tvID)
				return true;
		}
		return false;
	}
	
	public int getStatusID(){
		for(TV<E> tv : data){
			if(tv.opcode == 1){
				ReturnStatusTV status = (ReturnStatusTV)tv;
				return status.statusID;
			}
		}
		return -1;
	}
}
