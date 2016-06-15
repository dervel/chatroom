package net;

import java.net.Socket;

import security.CryptController;

public interface GenericNetClient {
	public Socket getSocket();
	public boolean isAlive();
	public void handle_packet(byte[] data);
	public void restartConnection();
	public CryptController getCrypt();
}
