package packets;

import java.nio.charset.StandardCharsets;

public abstract class IncomingPacket {
	
	protected byte[] data;
	protected int position = 2;
	
	public IncomingPacket(byte[] data){
		this.data = data;
	}
	
	protected String readString(){
		int length = readShort();
		
		byte[] temp = new byte[length*2];
		System.arraycopy(data, position, temp, 0, length*2);
		
		position += 2 * length;
		
		return new String(temp, StandardCharsets.UTF_16LE);
	}
	
	protected int readShort(){
		int length;
		length = data[position++];
		length += (data[position++] << 8);

		return length;
	}
	
	protected byte readByte(){
		byte length;
		length = data[position++];

		return length;
	}
	
	public abstract void implement();
	public abstract void run();
}
