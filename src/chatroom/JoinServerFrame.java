package chatroom;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

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

	/**
	 * Create the frame.
	 */
	public JoinServerFrame() {
		setTitle("Join Server");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 242);
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
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				join();
			}
		});
		btnJoin.setBounds(335, 7, 89, 23);
		contentPane.add(btnJoin);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 61, 434, 3);
		contentPane.add(separator);
		
		joinServerLog = new JTextArea();
		joinServerLog.setBounds(10, 50, 414, 116);
		final JScrollPane scroll = new JScrollPane(joinServerLog);
		scroll.setBounds(10, 75, 414, 116);
		contentPane.add(scroll);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 36, 31, 14);
		contentPane.add(lblName);
		
		nameField = new JTextField();
		nameField.setText("dervel");
		nameField.setBounds(51, 33, 115, 20);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblPass = new JLabel("Pass:");
		lblPass.setBounds(176, 36, 31, 14);
		contentPane.add(lblPass);
		
		passField = new JPasswordField();
		passField.setBounds(210, 33, 109, 20);
		contentPane.add(passField);
	}
	
	public void join(){
		try {
			if(portField.getText().length() == 0){
				joinServerLog.append("\n\nPort Field can't be empty");
				return;
			}
			int port = Integer.parseInt(portField.getText());
			if(port < 0 || port > 65535){
				joinServerLog.append("\n\nPort number out of range 1 - 65535");
				return;
			}
			
			ServerConnection newServerConnection = new ServerConnection(
					ipField.getText(), 
					Integer.parseInt(portField.getText()),
					nameField.getText(),
					new String(passField.getPassword())
			);
			joinServerLog.append("\n\nAttempting to connect to server.");
			newServerConnection.connect();
			joinServerLog.append("\n\nConnected to server sucessfully.");
		} catch (IOException e) {
			joinServerLog.append("\n\nGenertic Error:"+e.getMessage());
		}catch (NumberFormatException e){
			joinServerLog.append("\n\nCould not parse port number.");
		}
		
	}
}
