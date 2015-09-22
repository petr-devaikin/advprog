package PhotoViewerPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class Toolbar extends JPanel {
	public Toolbar(StatusBar statusBar) {
		super();
		
		String[] tags = {
			"Family",
			"Nature",
			"Cars"
		};
		
		for (int i = 0; i < tags.length; i++) {
			JToggleButton button = new JToggleButton(tags[i]);
			button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	                statusBar.setStatus(button.getText() + " pressed");
	            }
	        });
			add(button);
		}
	}
}
