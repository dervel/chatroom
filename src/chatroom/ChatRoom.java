package chatroom;

import java.io.IOException;

import server.AlreadyRunningException;

public class ChatRoom {
	
	public static MainController controller;
	
	public static void main(String args[]){
		
		//Load server configuration and start server if it should
		try {
			if(Config.loadServerConfig()){
				if(Config.SERVER_AUTOSTART)
					ChatRoom.getController().getLocalServer().start();
			}
			
		} catch (IOException e) {
			controller.getLocalServer().getServerLog().log(e.getMessage());
		} catch (AlreadyRunningException e) {
			controller.getLocalServer().getServerLog().log("Somehow you managed to have a running server"+
			" during startup. Congratulations, you just leveled up");
		}
		
		MainFrame main = new MainFrame();
		main.setVisible(true);
	}
	
	public static MainController getController(){
		if(controller == null)
			controller = new MainController();
		return controller;
	}
}
