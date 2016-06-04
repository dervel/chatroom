package chatroom;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CustomCellRenderer extends JLabel implements ListCellRenderer<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
			boolean isSelected, boolean cellHasFocus) {
		
		this.setText(value);
		this.setOpaque(true);
		if(ChatRoom.getController().getElementAt(index).hasConnected()){
			this.setForeground(Color.red);
		}else{
			this.setForeground(new Color(0, 100, 0));
		}
		
		if(isSelected){
			this.setBackground(list.getSelectionBackground());
		}else{
			this.setBackground(list.getBackground());
		}
		
		return this;
	}

}
