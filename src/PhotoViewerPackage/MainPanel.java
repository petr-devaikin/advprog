package PhotoViewerPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class MainPanel extends JPanel {
	public MainPanel() {
		setBackground(new Color(255, 255, 255));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		PhotoComponent photo = new PhotoComponent();
		add(photo);
	}
}
