package packets;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import client.PacketListener;
import net.packet.PacketData;

public abstract class IncomingPacketController<TV> implements ReadablePacket{
	
	protected byte[] data;
	protected int position = 2;
	
	private List<PacketListener<TV>> listeners = new ArrayList<PacketListener<TV>>();
	protected PacketData<TV> packet;
	
	public String readString(){
		int length = readShort();
		
		byte[] temp = new byte[length*2];
		System.arraycopy(data, position, temp, 0, length*2);
		
		position += 2 * length;
		
		return new String(temp, StandardCharsets.UTF_16LE);
	}
	
	public byte[] readArray(){
		int length = readInt();
		byte[] buffer = new byte[length];
		System.arraycopy(data, position, buffer, 0, length);
		position += length;
		return buffer;
	}
	
	public int readShort(){
		int length;
		length = data[position++];
		length += (data[position++] << 8);

		return length;
	}
	
	public int readInt(){
		int length;
		length = data[position++] & 0xFF;
		length += (data[position++] << 8) & 0xFF;
		length += (data[position++] << 16)& 0xFF;
		length += (data[position++] << 24)& 0xFF;

		return length;
	}
	
	public byte readByte(){
		byte length;
		length = data[position++];

		return length;
	}
	
	public void handlePacket(byte[] data) {
		this.data = data;
		position = 2;
		
		packet = new PacketData<TV>();
		read();
		
		for(PacketListener<TV> l : listeners){
			l.catchPacket(packet);
		}
		
		run();
	}
	
	protected abstract void read();
	protected abstract void run();
	
	public void addPacketListener(PacketListener<TV> pl){
		listeners.add(pl);
	}
	
	public void removePacketListener(PacketListener<TV> pl){
		listeners.remove(pl);
	}
}
