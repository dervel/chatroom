package chatroom;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import client.ClientPacketFactory;
import client.PacketListener;
import client.ServerConnection;
import net.packet.PacketData;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;

public class JoinServerFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField ipField;
	private JTextField portField;
	private JTextField nameField;
	private JTextArea joinServerLog;
	private JPasswordField passField;
	private JTextField regNameField;
	private JPasswordField regPassField;
	private JPasswordField regCnfField;
	private JTabbedPane tabbedPane;
	
	private ServerConnection currentConnection;

	/**
	 * Create the frame.
	 */
	public JoinServerFrame() {
		setTitle("Join Server");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 342);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("IP:");
		lblNewLabel.setBounds(10, 11, 14, 14);
		contentPane.add(lblNewLabel);
		
		ipField = new JTextField();
		ipField.setBounds(51, 8, 115, 20);
		contentPane.add(ipField);
		ipField.setColumns(10);
		ipField.setText(String.valueOf("127.0.0.1"));
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(176, 11, 24, 14);
		contentPane.add(lblPort);
		
		portField = new JTextField();
		portField.setBounds(210, 8, 86, 20);
		contentPane.add(portField);
		portField.setColumns(10);
		portField.setText(String.valueOf(Config.DEFAULT_PORT));
		
		JButton btnJoin = new JButton("Connect");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect();
			}
		});
		btnJoin.setBounds(335, 7, 89, 23);
		contentPane.add(btnJoin);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 162, 434, 3);
		contentPane.add(separator);
		
		joinServerLog = new JTextArea();
		joinServerLog.setBounds(10, 50, 414, 116);
		final JScrollPane scroll = new JScrollPane(joinServerLog);
		scroll.setBounds(10, 176, 414, 116);
		contentPane.add(scroll);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 36, 414, 115);
		tabbedPane.setVisible(false);
		contentPane.add(tabbedPane);
		
		JPanel loginPanel = new JPanel();
		tabbedPane.addTab("Login", null, loginPanel, null);
		loginPanel.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 11, 31, 14);
		loginPanel.add(lblName);
		
		nameField = new JTextField();
		nameField.setBounds(51, 8, 115, 20);
		loginPanel.add(nameField);
		nameField.setText("dervel");
		nameField.setColumns(10);
		
		JLabel lblPass = new JLabel("Pass:");
		lblPass.setBounds(176, 11, 31, 14);
		loginPanel.add(lblPass);
		
		passField = new JPasswordField();
		passField.setBounds(217, 8, 109, 20);
		loginPanel.add(passField);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setBounds(310, 53, 89, 23);
		loginPanel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		
		JPanel registerPanel = new JPanel();
		tabbedPane.addTab("Register", null, registerPanel, null);
		registerPanel.setLayout(null);
		
		JLabel label = new JLabel("Name:");
		label.setBounds(10, 11, 31, 14);
		registerPanel.add(label);
		
		regNameField = new JTextField();
		regNameField.setColumns(10);
		regNameField.setBounds(110, 8, 132, 20);
		registerPanel.add(regNameField);
		
		JLabel label_1 = new JLabel("Password:");
		label_1.setBounds(10, 36, 50, 14);
		registerPanel.add(label_1);
		
		regPassField = new JPasswordField();
		regPassField.setBounds(110, 33, 132, 20);
		registerPanel.add(regPassField);
		
		JLabel label_2 = new JLabel("Confirm Password:");
		label_2.setBounds(10, 61, 90, 14);
		registerPanel.add(label_2);
		
		regCnfField = new JPasswordField();
		regCnfField.setBounds(110, 58, 132, 20);
		registerPanel.add(regCnfField);
		
		JButton button = new JButton("Register");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				register();
			}
		});
		button.setBounds(310, 57, 89, 23);
		registerPanel.add(button);
	}
	
	private void register(){
		//Validate Data
		String pass = new String(regPassField.getPassword());
		String pass1 = new String(regCnfField.getPassword());
		if(!pass.equals(pass1)){
			joinServerLog.append("\n\n Passwords do not match.");
			return;
		}
		
		String username = regNameField.getText();
		if(username.length() == 0){
			joinServerLog.append("\n\nUsername can't be empty");
			return;
		}
		
		//Try to register
		try {
			
			currentConnection.addPacketListener( new PacketListener<ServerConnection>(){

				@Override
				public void catchPacket(PacketData<ServerConnection> packet) {
					if(packet.contains(1)){
						System.out.println("TiggerFIRED");
						joinServerLog.append("\n\n"+StatusReturnMessages.getMessage(packet.getStatusID()));
					}
					
				}
				
			});
			
			ClientPacketFactory factory = currentConnection.getClientPacketFactory();
			factory.appendRegisterTV(username,pass);
			factory.sendPacket();
			//TODO: report status of registration
		} catch (Exception e) {
			joinServerLog.append("\n\n Error sending packet "+e.getMessage());
		}
	}
	
	private void connect(){
		try {
			//Validate Data
			if(portField.getText().length() == 0){
				joinServerLog.append("\n\nPort Field can't be empty");
				return;
			}
			int port = Integer.parseInt(portField.getText());
			if(port < 0 || port > 65535){
				joinServerLog.append("\n\nPort number out of range 1 - 65535");
				return;
			}
			
			//Create the connection
			currentConnection = new ServerConnection(
					ipField.getText(), 
					Integer.parseInt(portField.getText())
			);
			joinServerLog.append("\n\nAttempting to connect to server.");
			
			//Add the Listener
			currentConnection.addPacketListener( new PacketListener<ServerConnection>(){

				@Override
				public void catchPacket(PacketData<ServerConnection> packet) {
					if(packet.contains(0x00)){
						joinServerLog.append("\n\nConnected to server sucessfully.");
						tabbedPane.setVisible(true);
					}
					
				}
				
			});
			
			//Try to connect
			currentConnection.connect();
			
			//Add the server list
			ChatRoom.getController().addServerConnection(currentConnection);
		} catch (IOException e) {
			joinServerLog.append("\n\nCould connect to server("+e.getMessage()+").");
		}catch (NumberFormatException e){
			joinServerLog.append("\n\nCould not parse port number.");
		}
		
	}
	
	private void login(){
		try {
			//Add the Listener
			currentConnection.addPacketListener( new PacketListener<ServerConnection>(){

				@Override
				public void catchPacket(PacketData<ServerConnection> packet) {
					if(packet.contains(0x01)){
						joinServerLog.append("\n\n"+StatusReturnMessages.getMessage(packet.getStatusID()));
					}
					
				}
				
			});
			currentConnection.setCredentials(nameField.getText(), new String(passField.getPassword()));
			currentConnection.sendAuthenticationPacket();
		} catch (Exception e) {
			joinServerLog.append("\n\nCould login to server("+e.getMessage()+").");
		}
	}
}
