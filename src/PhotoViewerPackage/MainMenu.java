package PhotoViewerPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class MainMenu extends JMenuBar {
	public MainMenu(StatusBar statusBar, MainPanel mainPanel) {
		super();
		
        JMenu file = new JMenu("File");

        JMenuItem importItem = new JMenuItem("Import");
        importItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == 0 && mainPanel.addPhoto(chooser.getSelectedFile().getAbsolutePath()))
                    statusBar.setStatus("Photo imported");
                	
            }
        });
        file.add(importItem);
        
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (mainPanel.deletePhoto())
                    statusBar.setStatus("Photo deleted");
            }
        });
        file.add(deleteItem);
        
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        file.add(quitItem);
        
        add(file);

        
        JMenu view = new JMenu("View");
        ButtonGroup viewGroup = new ButtonGroup();

        JRadioButtonMenuItem photoViewerItem = new JRadioButtonMenuItem("Photo viewer", true);
        view.add(photoViewerItem);
        viewGroup.add(photoViewerItem);
        
        JRadioButtonMenuItem browserItem = new JRadioButtonMenuItem("Browser");
        view.add(browserItem);
        viewGroup.add(browserItem);
        
        JRadioButtonMenuItem splitModeItem = new JRadioButtonMenuItem("Split mode");
        view.add(splitModeItem);
        viewGroup.add(splitModeItem);
        
        add(view);
	}
}
