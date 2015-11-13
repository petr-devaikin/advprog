package PhotoViewerPackage;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;

import PhotoComponent.PhotoComponentUI;
import PhotoComponent.SimplePhotoComponentUI;

public class App {
	public static void main(String[] args) {
		UIManager.put(PhotoComponentUI.UI_CLASS_ID, SimplePhotoComponentUI.class.getName());
		
		MainWindow mainWindow = new MainWindow();

		mainWindow.setSize(600, 500);
		mainWindow.setMinimumSize(new Dimension(300, 300));
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
}
