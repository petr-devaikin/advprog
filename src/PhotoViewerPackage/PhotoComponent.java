package PhotoViewerPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

public class PhotoComponent extends JComponent {
	private Image image;
	private boolean isFlipped = false;
	private boolean isMousePressed = false;
	private boolean isDrawing = false;
	private PhotoStroke currentStroke = new PhotoStroke();
	private Font font = new Font("Arial", Font.PLAIN, 12);
	
	private ArrayList<PhotoNote> notes = new ArrayList<PhotoNote>();
	private ArrayList<PhotoStroke> strokes = new ArrayList<PhotoStroke>();
	
	private Dimension defaultSize = new Dimension(300, 300);
	
	public PhotoComponent() {
		super();
		
		notes.add(new PhotoNote(new Point(0, 0)));
		
		addMouseListener(new MouseListener() {
			@Override
            public void mouseReleased(MouseEvent e) {
				if (isFlipped)
					stopStroke();
			}
            @Override
            public void mousePressed(MouseEvent e) {
            	if (isFlipped)
            		startStroke();
            }
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
        	      isFlipped = !isFlipped;
        	      repaint();
                }
                else if (e.getClickCount() == 1) {
                	if (isFlipped) {
                		requestFocus(true);
	                	PhotoNote lastNode = notes.get(notes.size() - 1);
	                	if (lastNode.text.length() == 0)
	                		lastNode.position = e.getPoint();
	                	else {
	                		notes.add(new PhotoNote(e.getPoint()));
	                	}
                	}
                }
            }
        });
		
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDrawing) {
					currentStroke.addPoint(e.getPoint());
					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {}
		});
		

		setFocusable(true);
		addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            	if (isFlipped) {
            		notes.get(notes.size() - 1).text += e.getKeyChar();
            		repaint();
            	}
            }

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {}
        });
	}
	
	@Override
    public Dimension getPreferredSize() {
        return defaultSize;
    }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		drawBackground(g);
		
		if (isFlipped) {
			drawNotes(g);
			drawStrokes(g);
		}
	}
	
	private void drawBackground(Graphics g) {
		g.setColor(isFlipped ? Color.WHITE : Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth(), getHeight());
	}
	
	private void drawNotes(Graphics g) {
		FontMetrics metrics = g.getFontMetrics(font);
	
		for (PhotoNote note : notes) {
			g.setColor(Color.BLACK);
			g.setFont(font);
			String text = note.text;
			int x = note.position.x;
			int y = note.position.y;
			/*
			if (note.position.x + metrics.stringWidth(note.text) > getWidth()) {
				System.out.println(note.text.substring(0, note.text.length() - 1));
				note.text = note.text.substring(0, note.text.length() - 1) + "\n" + 
						note.text.substring(note.text.length() - 1, note.text.length());
			}
			*/
			g.drawString(text, x, y);
		}
	}
	
	private void drawStrokes(Graphics g) {
		for (PhotoStroke stroke : strokes) {
			g.setColor(Color.BLACK);
			g.setPaintMode();
			if (stroke.points.size() > 1)
				for (int i = 1; i < stroke.points.size(); i++)
					g.drawLine(
						(int) stroke.points.get(i - 1).getX(),
						(int) stroke.points.get(i - 1).getY(),
						(int) stroke.points.get(i).getX(),
						(int) stroke.points.get(i).getY());
		}
	}
	
	private void startStroke() {
		isDrawing = true;
		if (currentStroke.points.size() > 1)
			currentStroke = new PhotoStroke();
		strokes.add(currentStroke);
	}
	
	private void stopStroke() {
		isDrawing = false;
	}
}
