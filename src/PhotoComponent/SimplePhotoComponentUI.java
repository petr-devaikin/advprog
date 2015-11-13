package PhotoComponent;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class SimplePhotoComponentUI extends PhotoComponentUI
		implements MouseListener, MouseMotionListener, KeyListener {
	private boolean isDrawing = false;

	public static ComponentUI createUI(JComponent c) {
		return new SimplePhotoComponentUI();
	}

	public void installUI(JComponent c) {
		((JPhotoComponent) c).addMouseListener(this);
		((JPhotoComponent) c).addMouseMotionListener(this);
		((JPhotoComponent) c).addKeyListener(this);
		((JPhotoComponent) c).setFocusable(true);
	}

	public void uninstallUI(JComponent c) {
		((JPhotoComponent) c).setFocusable(false);
		((JPhotoComponent) c).removeKeyListener(this);
		((JPhotoComponent) c).removeMouseMotionListener(this);
		((JPhotoComponent) c).removeMouseListener(this);
	}

	public void paint(Graphics g, JComponent c) {
		JPhotoComponent JPhotoComponent = (JPhotoComponent) c;

		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		drawBackground(graphics2D, JPhotoComponent);

		if (!JPhotoComponent.isFlipped())
			drawFace(graphics2D, JPhotoComponent);
		else
			drawBackSide(graphics2D, JPhotoComponent);

		drawCorners(graphics2D, JPhotoComponent);
	}

	private void drawFace(Graphics2D g, JPhotoComponent c) {
		drawImage(g, c);
	}

	private void drawBackSide(Graphics2D g, JPhotoComponent c) {
		Dimension size = getImageSize(c);
		Point position = getImagePosition(c);
		drawImageBackSide(g, c);

		Rectangle vRect = c.getVisibleRect();
		Rectangle clipRect = new Rectangle(position.x, position.y, size.width, size.height);
		g.setClip(clipRect.intersection(vRect));

		drawNotes(g, c);
		drawStrokes(g, c);

		g.setClip(c.getVisibleRect());
	}

	private void drawBackground(Graphics2D g, JPhotoComponent c) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, c.getWidth(), c.getHeight());
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}

	private void drawNotes(Graphics2D g, JPhotoComponent c) {
		for (PhotoNote note : c.getNotes())
			drawNote(g, c, note);
	}

	private ArrayList<String> breakNoteToLines(Graphics2D g, JPhotoComponent c, PhotoNote note) {
		ArrayList<String> result = new ArrayList<String>();

		Dimension size = getImageSize(c);
		Point position = getImagePosition(c);
		FontMetrics metrics = g.getFontMetrics(note.getFont());

		String text = note.text.trim();
		String restText = "";

		if (note.position.x < size.width + position.x)
			while (text.length() > 0) {
				if (note.position.x + metrics.stringWidth(text) > size.width) {
					int i = 0;

					while (note.position.x + metrics.stringWidth(text.substring(0, i + 1)) < size.width)
						i++;
					if (i == 0)
						i++;

					int spacePosition = text.substring(0, i).lastIndexOf(' ');
					if (spacePosition != -1) {
						restText = text.substring(spacePosition + 1, text.length());
						text = text.substring(0, spacePosition).trim();
					} else {
						restText = text.substring(i, text.length());
						text = text.substring(0, i).trim();
					}
				}

				result.add(text);
				text = restText.trim();
				restText = "";
			}

		return result;
	}

	private Dimension getNoteSize(Graphics2D g, ArrayList<String> lines, Font font) {
		int width = 0;
		FontMetrics metrics = g.getFontMetrics(font);
		for (String line : lines)
			width = Math.max(width, metrics.stringWidth(line));

		return new Dimension(width, (lines.size() - 1) * metrics.getHeight());
	}

	private void drawNote(Graphics2D g, JPhotoComponent c, PhotoNote note) {
		Point position = getImagePosition(c);

		g.setFont(note.getFont());
		FontMetrics metrics = g.getFontMetrics(note.getFont());
		int y = note.position.y;

		ArrayList<String> lines = breakNoteToLines(g, c, note);

		g.setColor(Color.BLACK);
		for (String line : lines) {
			g.drawString(line, note.position.x + position.x, y + position.y);
			y += metrics.getHeight();
		}

		drawNoteBorders(g, c, note, lines);
	}

	private void drawNoteBorders(Graphics2D g, JPhotoComponent c, PhotoNote note, ArrayList<String> lines) {
		Point position = getImagePosition(c);

		Dimension linesSize = getNoteSize(g, lines, note.getFont());
		FontMetrics metrics = g.getFontMetrics(note.getFont());

		g.setColor(c.isCurrentNote(note) ? Color.green : Color.lightGray);

		int x = note.position.x + position.x;
		int y = note.position.y + position.y;

		g.drawLine(x - metrics.getDescent(), y - metrics.getAscent(), x - metrics.getDescent() + 5,
				y - metrics.getAscent());
		g.drawLine(x - metrics.getDescent(), y - metrics.getAscent(), x - metrics.getDescent(),
				y - metrics.getAscent() + 5);

		g.drawLine(x + linesSize.width + metrics.getDescent(), y + linesSize.height + metrics.getDescent(),
				x + linesSize.width + metrics.getDescent() - 5, y + linesSize.height + metrics.getDescent());
		g.drawLine(x + linesSize.width + metrics.getDescent(), y + linesSize.height + metrics.getDescent(),
				x + linesSize.width + metrics.getDescent(), y + linesSize.height - 5 + metrics.getDescent());
	}

	private void drawStrokes(Graphics2D g, JPhotoComponent c) {
		g.setColor(Color.gray);
		for (PhotoStroke stroke : c.getStrokes()) {
			if (stroke.points.size() > 1)
				for (int i = 1; i < stroke.points.size(); i++)
					drawStroke(g, c, stroke.points.get(i - 1), stroke.points.get(i));
		}
	}

	private void drawStroke(Graphics2D g, JPhotoComponent c, Point pointA, Point pointB) {
		Point position = getImagePosition(c);

		float distance = (float) pointA.distance(pointB);
		float strokeWidth = 3 - distance * 0.05f;
		g.setStroke(new BasicStroke(strokeWidth < 0.2 ? 0.2f : strokeWidth));
		g.drawLine((int) pointA.x + position.x, (int) pointA.y + position.y, (int) pointB.x + position.x,
				(int) pointB.y + position.y);
	}

	private void drawImage(Graphics2D g, JPhotoComponent c) {
		Point position = getImagePosition(c);

		g.drawImage(c.getImage(), position.x, position.y, null);
	}

	private void drawImageBackSide(Graphics2D g, JPhotoComponent c) {
		Dimension size = getImageSize(c);
		Point position = getImagePosition(c);

		g.setColor(Color.white);
		g.fillRect(position.x, position.y, size.width, size.height);
	}

	private void drawCorners(Graphics2D g, JPhotoComponent c) {
		Dimension size = getImageSize(c);
		Point position = getImagePosition(c);

		g.setStroke(new BasicStroke(1));

		g.setColor(Color.black);
		int d = 40;
		g.fillArc(position.x - d / 2 - 5, position.y - d / 2 - 5, d, d, 270, 90);
		g.fillArc(position.x + size.width - d / 2 + 5, position.y + size.height - d / 2 + 5, d, d, 90, 90);
		g.setColor(Color.darkGray);
		g.drawArc(position.x - d / 2 - 5, position.y - d / 2 - 5, d, d, 270, 90);
		g.drawArc(position.x + size.width - d / 2 + 5, position.y + size.height - d / 2 + 5, d, d, 90, 90);
	}

	private Point getImagePosition(JPhotoComponent c) {
		int w = c.getImage().getWidth(null);
		int h = c.getImage().getHeight(null);
		int x = (int) Math.max((c.getWidth() - w) / 2, 0);
		int y = (int) Math.max((c.getHeight() - h) / 2, 0);
		return new Point(x, y);
	}

	private Dimension getImageSize(JPhotoComponent c) {
		int w = c.getImage().getWidth(null);
		int h = c.getImage().getHeight(null);
		return new Dimension(w, h);
	}

	private PhotoNote findNoteByPosition(JPhotoComponent c, int x, int y) {
		Graphics2D g = (Graphics2D) c.getGraphics();
		ArrayList<PhotoNote> notes = c.getNotes();
		PhotoNote result = null;
		Point imagePosition = getImagePosition(c);

		for (PhotoNote note : notes) {
			ArrayList<String> lines = breakNoteToLines(g, c, note);
			Dimension noteSize = getNoteSize(g, lines, note.getFont());

			if (note.position.x - 5 <= x - imagePosition.x && note.position.y - 5 <= y - imagePosition.y
					&& note.position.x + 5 + noteSize.width > x - imagePosition.x
					&& note.position.y + 5 + noteSize.height > y - imagePosition.y) {

				result = note;
				break;
			}
		}

		return result;
	}

	// MOUSE LISTENER

	public void mouseReleased(MouseEvent e) {
		if (((JPhotoComponent) e.getComponent()).isFlipped())
			isDrawing = false;
	}

	public void mousePressed(MouseEvent e) {
		if (((JPhotoComponent) e.getComponent()).isFlipped()) {
			isDrawing = true;
			((JPhotoComponent) e.getComponent()).startStroke();
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		JPhotoComponent c = ((JPhotoComponent) e.getComponent());

		if (e.getClickCount() == 2)
			c.flip();
		else if (e.getClickCount() == 1) {
			if (c.isFlipped()) {
				c.requestFocus(true);
				Point imagePosition = getImagePosition(c);

				PhotoNote selectedNote = findNoteByPosition(c, e.getX(), e.getY());

				if (selectedNote != null)
					c.selectNote(selectedNote);
				else {
					Point notePosition = new Point(e.getPoint().x - imagePosition.x, e.getPoint().y - imagePosition.y);
					c.startNote(notePosition);
				}
			}
		}
	}

	// MOUSE MOTION LISTENER

	public void mouseDragged(MouseEvent e) {
		JPhotoComponent c = ((JPhotoComponent) e.getComponent());
		if (isDrawing) {
			Point newPoint = e.getPoint();

			Point p = getImagePosition(c);
			newPoint.x -= p.x;
			newPoint.y -= p.y;

			c.addPointToStroke(newPoint);
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	// KEY LISTENER

	public void keyTyped(KeyEvent e) {
		JPhotoComponent c = ((JPhotoComponent) e.getComponent());

		if (c.isFlipped()) {
			if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE)
				c.backspaceCurrentNote();
			else
				c.addCharToCurrentNote(e.getKeyChar());
		}
	}

	public void keyPressed(KeyEvent e) {
		JPhotoComponent c = ((JPhotoComponent) e.getComponent());

		if (c.isFlipped()) {
			if (e.getModifiers() == 4 && e.getKeyCode() == 61)
				c.increaseCurrentNoteSize();
			else if (e.getModifiers() == 4 && e.getKeyCode() == 45)
				c.decreaseCurrentNoteSize();
		}
	}

	public void keyReleased(KeyEvent e) {
	}
}
