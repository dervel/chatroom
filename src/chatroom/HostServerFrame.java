package chatroom;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import server.AlreadyRunningException;
import server.NotRunningException;

import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

public class HostServerFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField portField;
	private JSeparator separator;
	private JTextArea hostServerLog;
	private JTextField nameField;

	/**
	 * Create the frame.
	 */
	public HostServerFrame() {
		setTitle("Host Server Settings");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(10, 10, 24, 14);
		contentPane.add(lblPort);
		
		portField = new JTextField();
		portField.setBounds(38, 7, 86, 20);
		portField.setColumns(10);
		portField.setText(String.valueOf(Config.SERVER_PORT));
		contentPane.add(portField);
		
		separator = new JSeparator();
		separator.setBounds(0, 144, 444, 2);
		contentPane.add(separator);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				start();
			}
		});
		btnNewButton.setBounds(10, 110, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		btnStop.setBounds(335, 110, 89, 23);
		contentPane.add(btnStop);
		
		hostServerLog = new JTextArea();
		hostServerLog.setBounds(10, 155, 414, 95);
		final JScrollPane scroll = new JScrollPane(hostServerLog);
		scroll.setBounds(10, 155, 414, 95);
		contentPane.add(scroll);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hostServerLog.setText("");
			}
		});
		btnClear.setBounds(335, 257, 89, 23);
		contentPane.add(btnClear);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(134, 10, 31, 14);
		contentPane.add(lblName);
		
		nameField = new JTextField();
		nameField.setText(Config.SERVER_NAME);
		nameField.setBounds(175, 7, 86, 20);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JCheckBox chckbxAutoStart = new JCheckBox("Auto Start");
		chckbxAutoStart.setSelected(Config.SERVER_AUTOSTART);
		chckbxAutoStart.setBounds(10, 34, 97, 23);
		contentPane.add(chckbxAutoStart);
	}
	
	public void start(){
		try{
			if(portField.getText().length() == 0){
				hostServerLog.append("\n\nPort Field can't be empty");
				return;
			}
			int port = Integer.parseInt(portField.getText());
			if(port < 0 || port > 65535){
				hostServerLog.append("\n\nPort number out of range 1 - 65535");
				return;
			}
			
			if(nameField.getText().length() == 0){
				hostServerLog.append("Server name cannot be empty");
				return;
			}
			
			Config.SERVER_NAME = nameField.getText();
			Config.SERVER_PORT = Integer.parseInt(portField.getText());
			ChatRoom.getController().getLocalServer().start();
			hostServerLog.append("\n\nServer started successfully");
		}catch (NumberFormatException e){
			hostServerLog.append("\n\nCould not parse port number.");
		} catch (IOException e) {
			hostServerLog.append("\n\nPort number is already in use.\n"+e.getMessage()+"");
		} catch (AlreadyRunningException e) {
			hostServerLog.append("\n\nServer is already running.");
		}
		
	}
	
	public void stop(){
		try {
			ChatRoom.getController().getLocalServer().stop();
			hostServerLog.append("\n\nServer stopped successfully");
		} catch (IOException e) {
			hostServerLog.append("\n\nOne of the sockets was already closed.");
		} catch (NotRunningException e) {
			hostServerLog.append("\n\nServer is not running.");
		}
	}
}
