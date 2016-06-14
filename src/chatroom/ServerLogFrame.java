package chatroom;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import server.ServerLog;

import javax.swing.JTextArea;

public class ServerLogFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Thread refresh;
	private JTextArea textArea;
	private int counter=0;

	/**
	 * Create the frame.
	 */
	public ServerLogFrame() {
		setTitle("Server Log");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		final JScrollPane scroll = new JScrollPane(textArea);
		contentPane.add(scroll, BorderLayout.CENTER);
		
		refresh = new Thread( new Runnable(){

			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(Config.LOG_REFRESH_TIME);
						generateLogText();
					} catch (InterruptedException e) {
					}
					
				}
				
			}
			
		});
		refresh.start();
	}
	
	private void generateLogText(){
		ServerLog log = ChatRoom.getController().getLocalServer().getServerLog();
		
		int logCounter = log.getCounter();
		
		if(logCounter <= Config.SERVER_LOG_SIZE){
			for(int i=counter; i<logCounter;i++){
				textArea.append(log.getMessageAt(i)+"\n");
			}
		}else{
			int newMessages = logCounter - counter;
			
			for(int i=Config.SERVER_LOG_SIZE - newMessages; i< Config.SERVER_LOG_SIZE;i++){
				textArea.append(log.getMessageAt(i)+"\n");
			}
		}
		
		counter = logCounter;
	}

}
