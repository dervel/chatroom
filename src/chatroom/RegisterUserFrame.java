package chatroom;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JPasswordField;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;

public class RegisterUserFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JPasswordField confirmField;
	private JList<String> list;
	private JTextArea reportArea;
	DefaultListModel<String> model;

	/**
	 * Create the frame.
	 */
	public RegisterUserFrame() {
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent arg0) {
				model.removeAllElements();
				for(int i=0;i<ChatRoom.getController().getConecctedServersCount();i++){
					model.add(i, ChatRoom.getController().getElementAt(i).getServerName());
				}
				
			}
			public void windowLostFocus(WindowEvent arg0) {
			}
		});
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 11, 31, 14);
		contentPane.add(lblName);
		
		nameField = new JTextField();
		nameField.setBounds(110, 8, 132, 20);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 36, 50, 14);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(110, 33, 132, 20);
		contentPane.add(passwordField);
		
		confirmField = new JPasswordField();
		confirmField.setBounds(110, 58, 132, 20);
		contentPane.add(confirmField);
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setBounds(10, 61, 90, 14);
		contentPane.add(lblConfirmPassword);
		
		list = new JList<String>();
		list.setBounds(252, 11, 172, 239);
		model = new DefaultListModel<String>();
		list.setModel(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new CustomCellRenderer());
		contentPane.add(list);
		
		reportArea = new JTextArea();
		reportArea.setEditable(false);
		reportArea.setBounds(18, 122, 224, 128);
		contentPane.add(reportArea);
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(153, 89, 89, 23);
		contentPane.add(btnNewButton);
	}
	
	public void repaintList(){
		list.repaint();
	}
}
