package PhotoViewerPackage;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class StatusBar extends JLabel {
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
}
