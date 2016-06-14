package packets;

public interface ReadablePacket {
	public byte readByte();
	public int readShort();
	public String readString();
	public int readInt();
	public byte[] readArray();
}
