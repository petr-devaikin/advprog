package PhotoViewerPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import PhotoComponent.JPhotoComponent;

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

			JPhotoComponent photo = new JPhotoComponent(image);
			
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
