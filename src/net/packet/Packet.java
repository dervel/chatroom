package net.packet;

import packets.ReadablePacket;

public abstract class Packet<E> {
	public byte opcode;
	//TypeValue
	
	public abstract void read(ReadablePacket controller);
	public abstract void run(E parent);
	
}
