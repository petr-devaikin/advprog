package PhotoViewerPackage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JFileChooser;
import javax.swing.ButtonGroup;

import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
	private Toolbar toolbarPanel;
	private StatusBar statusBar;
	private MainMenu mainMenu;
	private MainPanel mainPanel;
	
    public MainWindow() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

    	addMainPanel();
    	addStatusbar();
    	addMenu();
    	addToolbar();
    	
        setTitle("Photo Viewer");
    }
    
    private void addMenu() {
    	mainMenu = new MainMenu(statusBar, mainPanel);
    	//mainMenu.addStatusChangeListener(statusBar);
    	setJMenuBar(mainMenu);
    }
    
    private void addMainPanel() {
    	mainPanel = new MainPanel();
    	add(mainPanel);
    }
    
    private void addToolbar() {
    	toolbarPanel = new Toolbar(statusBar);
    	add(toolbarPanel, BorderLayout.NORTH);
    }
    
    private void addStatusbar() {
    	statusBar = new StatusBar();
    	add(statusBar, BorderLayout.SOUTH);
    }
}