package PhotoComponent;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.UIManager;

// TO DO:
/*
 * scroll big photo bug
 * delete photo
 * check file of file set
 */

public class JPhotoComponent extends JComponent implements ChangeListener {
	private PhotoComponentModel model;
	
	private Dimension defaultSize = new Dimension(300, 300);
	
	public void setModel(PhotoComponentModel m) {
		if (model != null)
			model.removeChangeListener(this);
		model = m;
		model.addChangeListener(this);
	}
	
	public PhotoComponentModel getModel() {
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
	
	
	// Model API
	
	public boolean isFlipped() { return model.isFlipped(); }
	public void flip()  { model.flip(); }
	
	Image getImage() { return model.getImage(); }
	
	ArrayList<PhotoNote> getNotes() { return model.getNotes(); }
	void startNote(Point position) { model.startNote(position); }
	void addCharToCurrentNote(char c) { model.addCharToCurrentNote(c); }
	void backspaceCurrentNote() { model.backspaceCurrentNote(); }
	void increaseCurrentNoteSize() { model.increaseCurrentNoteSize(); }
	void decreaseCurrentNoteSize() { model.decreaseCurrentNoteSize(); }
	boolean isCurrentNote(PhotoNote note) { return model.isCurrentNote(note); }
	void selectNote(PhotoNote note) { model.selectNote(note); }
	
	ArrayList<PhotoStroke> getStrokes() { return model.getStrokes(); }
	void startStroke() { model.startStroke(); }
	void addPointToStroke(Point point) { model.addPointToStroke(point); }
}
