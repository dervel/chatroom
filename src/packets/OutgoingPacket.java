package packets;

import java.nio.charset.StandardCharsets;

import chatroom.Config;

public class OutgoingPacket {
	
	private byte[] data = new byte[Config.PACKET_BUFFER_SIZE];
	private int position = 2;
	
	public byte[] getData(){
		return data;
	}
	
	public void writeByte(int b){
		data[position++] = (byte)b;
	}
	
	public void writeShort(int s){
		data[position++] = (byte) (s & 0x00FF);
		data[position++] = (byte)((s & 0xFF00) >> 8);
	}
	
	public void writeLong(long l){
		data[position++] = (byte) (l & 0x00000000000000FFL);
		data[position++] = (byte)((l & 0x000000000000FF00L) >> 8);
		data[position++] = (byte)((l & 0x0000000000FF0000L) >> 16);
		data[position++] = (byte)((l & 0x00000000FF000000L) >> 24);
		data[position++] = (byte)((l & 0x000000FF00000000L) >> 32);
		data[position++] = (byte)((l & 0x0000FF0000000000L) >> 40);
		data[position++] = (byte)((l & 0x00FF000000000000L) >> 48);
		data[position++] = (byte)((l & 0xFF00000000000000L) >> 56);
	}
	
	public void writeString(String s){
		writeShort((short)s.length());
		byte[] temp = s.getBytes(StandardCharsets.UTF_16LE);
		System.arraycopy(temp, 0, data, position, (s.length())*2);
		position += (s.length())*2;
	}
	
	public void writePacketLength(){
		data[0] = (byte) (position & 0x00FF);
		data[1] = (byte)((position & 0xFF00) >> 8);
	}
	
	public int getPacketLength(){
		return position;
	}
	
	public void resetPacket(){
		position = 2;
	}
}
