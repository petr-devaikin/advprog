package PhotoComponent;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

public interface PhotoComponentModel {
    boolean isFlipped();
	void flip();
	
	Image getImage();
	void setImage(Image img);
	
	ArrayList<PhotoNote> getNotes();
	void startNote(Point position);
	void addCharToCurrentNote(char c);
	void backspaceCurrentNote();
	void increaseCurrentNoteSize();
	void decreaseCurrentNoteSize();
	boolean isCurrentNote(PhotoNote note);
	void selectNote(PhotoNote note);
	
	ArrayList<PhotoStroke> getStrokes();
	void startStroke();
	void addPointToStroke(Point point);
	
	void addChangeListener(ChangeListener listener);
	void removeChangeListener(ChangeListener listener);
}
