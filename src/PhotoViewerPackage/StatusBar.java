package PhotoViewerPackage;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class StatusBar extends JLabel implements ActionListener {
	public StatusBar() {
		super("", JLabel.LEFT);
		
		Dimension size = getPreferredSize();
		size.height = 25;
		setPreferredSize(size);
		
		setBorder(new BevelBorder(BevelBorder.LOWERED));
	}
	
	public void setStatus(String status) {
		setText(status);
	}
	
	public void actionPerformed(ActionEvent e) {
		setText("");
	}
}
