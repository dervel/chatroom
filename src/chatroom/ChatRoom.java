package chatroom;

public class ChatRoom {
	
	//topkek
	
	public static MainController controller;
	
	public static void main(String args[]){
		
		MainFrame main = new MainFrame();
		main.setVisible(true);
	}
	
	public static MainController getController(){
		if(controller == null)
			controller = new MainController();
		return controller;
	}
}
