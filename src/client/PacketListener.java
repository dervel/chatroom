package client;

import net.packet.Packet;

public interface PacketListener<E> {
	public void catchPacket(Packet<E> packet);
}
