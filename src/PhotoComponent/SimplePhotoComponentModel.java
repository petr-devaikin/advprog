package PhotoComponent;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

public class SimplePhotoComponentModel {
	
	private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
	
	private boolean flipped = false;
	private Image image;

	private ArrayList<PhotoNote> notes = new ArrayList<PhotoNote>();
	private PhotoNote currentNote = null;
	private ArrayList<PhotoStroke> strokes = new ArrayList<PhotoStroke>();
	private PhotoStroke currentStroke = null;
	
	
	public SimplePhotoComponentModel(Image img) {
		image = img;
	}
	
	public boolean isFlipped() {
		return flipped;
	}
	
	public void flip() {
		flipped = !flipped;
		fireChangeEvent();
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image img) {
		image = img;
		fireChangeEvent();
	}
	
	public ArrayList<PhotoNote> getNotes() {
		return notes;
	}
	
	public void startNote(Point position) {
		PhotoNote newNote = new PhotoNote(position);
		notes.add(newNote);
		selectNote(newNote);
	}
	
	public void addCharToCurrentNote(char c) {
		if (currentNote != null)
			currentNote.text += c;
		fireChangeEvent();
	}
	
	public void increaseCurrentNoteSize() {
		if (currentNote != null)
			currentNote.IncreaseSize();
		fireChangeEvent();
	}
	
	public void decreaseCurrentNoteSize() {
		if (currentNote != null)
			currentNote.DecreaseSize();
		fireChangeEvent();
	}
	
	public boolean isCurrentNote(PhotoNote note) {
		return currentNote != null && note == currentNote;
	}
	
	public void selectNote(PhotoNote note) {
		if (!isCurrentNote(note) && currentNote != null && currentNote.text.length() == 0)
			notes.remove(currentNote);
		currentNote = note;
		fireChangeEvent();
	}
	
	public void backspaceCurrentNote() {
		if (currentNote != null && currentNote.text.length() > 0) {
			currentNote.text = currentNote.text.substring(0, currentNote.text.length() - 1);
			fireChangeEvent();
		}
	}
	
	public ArrayList<PhotoStroke> getStrokes() {
		return strokes;
	}
	
	public void startStroke() {
		if (currentStroke == null || currentStroke.points.size() > 1) {
			currentStroke = new PhotoStroke();
			strokes.add(currentStroke);
		}
	}
	
	public void addPointToStroke(Point point) {
		currentStroke.addPoint(point);
		fireChangeEvent();
	}
	
	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		listeners.remove(listener);
	}
	
	private void fireChangeEvent() {
		for (ChangeListener listener : listeners)
			listener.modelChanged();
	}
}
