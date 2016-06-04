package client;

import net.packet.PacketData;

public interface PacketListener<E> {
	public void catchPacket(PacketData<E> packet);
}
