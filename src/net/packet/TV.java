package net.packet;

public abstract class TV<E> {
	public byte opcode;
	//TypeValue
	
	public abstract void run(E parent);
}
