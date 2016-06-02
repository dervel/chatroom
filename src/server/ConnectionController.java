package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionController {

	private ServerSocket ss;
	private Thread connectionAcceptThread;
	private Server parent;
	private boolean running = false;
	private boolean isRunning = false;
	private int port;

	public ConnectionController(int port,Server parent){
		this.parent = parent;
		this.port = port;
		running = true;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void startAcceptingConnections() throws IOException,AlreadyRunningException{
		
		if(isRunning)
			throw new AlreadyRunningException();
		
		ss = new ServerSocket(port);
		
		Runnable temp = new Runnable(){

			@Override
			public void run() {
				while(running){
					try {
						Socket newClientSocket = ss.accept();
						System.out.println("New:"+newClientSocket.getLocalPort()+":"+newClientSocket.getPort());
						parent.newConnection(newClientSocket);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		};
		connectionAcceptThread = new Thread(temp);
		connectionAcceptThread.start();
		isRunning = true;
		
	}
	
	public void stopAcceptingConnections() throws IOException, NotRunningException{
		
		if(!isRunning)
			throw new NotRunningException();
		
		running = false;
		
		try {
		//Unblock ServerSocket by making it accept a socket
			Socket s = new Socket();
			s.connect(new InetSocketAddress("127.0.0.1", ss.getLocalPort()));
		
			s.close();
		} catch (IOException e1) {
			//Do nothing
		}finally{
			try{
				ss.close();
			}catch(IOException e){
				throw e;
			}
			isRunning = false;
		}
		
		
		
	}
}
