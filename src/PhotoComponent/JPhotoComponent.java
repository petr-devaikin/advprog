package PhotoComponent;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.UIManager;

// TO DO:
/*
 * scroll big photo bug
 * delete photo
 * check file of file set
 */

public class JPhotoComponent extends JComponent implements ChangeListener {
	private SimplePhotoComponentModel model;
	
	private Dimension defaultSize = new Dimension(300, 300);
	
	public void setModel(SimplePhotoComponentModel m) {
		if (model != null)
			model.removeChangeListener(this);
		model = m;
		model.addChangeListener(this);
	}
	
	public SimplePhotoComponentModel getModel() {
		return model;
	}
	
	public void setUI(PhotoComponentUI ui) {
		super.setUI(ui);
	}
	
	public void updateUI() {
		setUI((PhotoComponentUI) UIManager.getUI(this));
		invalidate();
	}
	
	public String getUIClassID() {
		return PhotoComponentUI.UI_CLASS_ID;
	} 
	
	public JPhotoComponent(Image image) {
		super();
		
		setModel(new SimplePhotoComponentModel(image));
		updateUI();
	}
	
    public Dimension getPreferredSize() {
		if (model.getImage() != null)
			return new Dimension(model.getImage().getWidth(null), model.getImage().getHeight(null));
		else
			return defaultSize;
    }
    
    
    // ChangeListener
	
	public void modelChanged() {
		repaint();
	}
}
