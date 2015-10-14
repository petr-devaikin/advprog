package PhotoViewerPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

public class MainPanel extends JPanel {
	JScrollPane scrollPane;
	
	public MainPanel() {
		setBackground(new Color(255, 255, 255));
		setLayout(new BorderLayout());

		scrollPane = new JScrollPane();
		add(scrollPane);
	}
	
	public boolean addPhoto(String src) {
		try {
			File file = new File(src);
			Image image = ImageIO.read(file);

			PhotoComponent photo = new PhotoComponent(image);
			scrollPane.setViewportView(photo);
			scrollPane.revalidate();
			return true;
	    }
	    catch(IOException ie)
	    {
	        System.out.println(ie.getMessage());
	        return false;
	    }
	}
	
	public boolean deletePhoto() {
		scrollPane.setViewport(null);
		scrollPane.revalidate();
		return true;
	}
}
