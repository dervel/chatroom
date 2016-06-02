package chatroom;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import server.NotRunningException;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private HostServerFrame hostServerFrame = null;
	private JoinServerFrame joinServerFrame = null;
	private ServerLogFrame serverLogFrame = null;
	private RegisterUserFrame registerUserFrame = null;
	

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exit();
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnServer = new JMenu("Server");
		menuBar.add(mnServer);
		
		JMenuItem mntmHostServer = new JMenuItem("Host Server");
		mntmHostServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hostServerFrame == null)
					hostServerFrame = new HostServerFrame();
				hostServerFrame.setVisible(true);
			}
		});
		mnServer.add(mntmHostServer);
		
		JMenuItem mntmJoinServer = new JMenuItem("Join Server");
		mntmJoinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(joinServerFrame == null)
					joinServerFrame = new JoinServerFrame();
				joinServerFrame.setVisible(true);
			}
		});
		mnServer.add(mntmJoinServer);
		
		JMenuItem mntmServerLog = new JMenuItem("Server Log");
		mntmServerLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(serverLogFrame == null)
					serverLogFrame = new ServerLogFrame();
				serverLogFrame.setVisible(true);
			}
		});
		mnServer.add(mntmServerLog);
		
		JMenu mnMore = new JMenu("More");
		menuBar.add(mnMore);
		
		JMenuItem mntmRegisteratserver = new JMenuItem("RegisterAtServer");
		mntmRegisteratserver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(registerUserFrame == null)
					registerUserFrame = new RegisterUserFrame();
				registerUserFrame.setVisible(true);
			}
		});
		mnMore.add(mntmRegisteratserver);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				shutDownSubparts();
			}
			
		});
	}
	
	private void shutDownSubparts(){
		if(hostServerFrame != null)
			hostServerFrame.dispose();
		if(joinServerFrame != null)
			joinServerFrame.dispose();
		if(serverLogFrame != null)
			serverLogFrame.dispose();
		if(registerUserFrame != null)
			registerUserFrame.dispose();
		
		//Close Server if hosting
		try {
			ChatRoom.getController().getLocalServer().stop();
		} catch (IOException | NotRunningException e) {
			
		}
	}
	
	private void exit(){
		this.setVisible(false);
		
		shutDownSubparts();
		
		this.dispose();
	}

}
