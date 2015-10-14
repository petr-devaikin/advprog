package PhotoViewerPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JComponent;

// TO DO:
/*
 * scroll big photo bug
 * delete photo
 * check file of file set
 */

public class PhotoComponent extends JComponent {
	private Image image;
	private boolean isFlipped = false;
	private boolean isMousePressed = false;
	private boolean isDrawing = false;
	private Font font = new Font("Arial", Font.PLAIN, 12);
	
	private ArrayList<PhotoNote> notes = new ArrayList<PhotoNote>();
	private PhotoNote currentNote = new PhotoNote(new Point(0, 0));
	private ArrayList<PhotoStroke> strokes = new ArrayList<PhotoStroke>();
	private PhotoStroke currentStroke = new PhotoStroke();
	
	private Dimension defaultSize = new Dimension(300, 300);
	
	public PhotoComponent(Image image) {
		super();
		
		this.image = image;
		
		notes.add(currentNote);
		
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
                		Point imagePosition = getImagePosition();
                		
                		PhotoNote pointedNote = null;
                		for (PhotoNote note : notes) {
                			if (note.position.x <= e.getPoint().x - imagePosition.x &&
            					note.position.y <= e.getPoint().y - imagePosition.y &&
            					note.position.x + note.size.width > e.getPoint().x - imagePosition.x &&
            					note.position.y + note.size.height > e.getPoint().y - imagePosition.y)
                		}
                		
	                	if (currentNote.text.length() == 0)
	                		currentNote.position = e.getPoint();
	                	else {
	                		currentNote = new PhotoNote(e.getPoint());
	                		notes.add(currentNote);
	                	}
                		currentNote.position.x -= imagePosition.x;
                		currentNote.position.y -= imagePosition.y;
                	}
                	repaint();
                }
            }
        });
		
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDrawing) {
					Point newPoint = e.getPoint();
            		Point p = getImagePosition();
            		newPoint.x -= p.x;
            		newPoint.y -= p.y;
            		
					currentStroke.addPoint(newPoint);
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
            		if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && currentNote.text.length() > 0)
            			currentNote.text = currentNote.text.substring(0, currentNote.text.length() - 1);
            		else
            			currentNote.text += e.getKeyChar();
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
        return image != null ? getImageSize() : defaultSize;
    }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
		
		drawBackground(graphics2D);

		drawImage(graphics2D);
		if (isFlipped) {
			drawNotes(graphics2D);
			drawStrokes(graphics2D);
		}

		graphics2D.setClip(getVisibleRect());
		drawCorners(graphics2D);
	}
	
	private void drawBackground(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth(), getHeight());
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	private void drawNotes(Graphics2D g) {
		FontMetrics metrics = g.getFontMetrics(font);

		Dimension size = getImageSize();
		Point position = getImagePosition();

		g.setFont(font);
		
		for (PhotoNote note : notes) {
			String text = note.text.trim();
			String restText = "";
			int x = note.position.x;
			int y = note.position.y;
			int maxWidth = 0;
			
			if (x < size.width + position.x) {
				while (text != "") {
					if (note.position.x + metrics.stringWidth(text) > size.width) {
						int i = 1;
						while (note.position.x + metrics.stringWidth(text.substring(0, i)) < size.width)
							i++;
						if (i > 1)
							i--;
						
						int spacePosition = text.substring(0, i).lastIndexOf(' ');
						if (spacePosition != -1) {
							restText = text.substring(spacePosition + 1, text.length());
							text = text.substring(0, spacePosition).trim();
						}
						else {
							restText = text.substring(i, text.length());
							text = text.substring(0, i).trim();
						}
					}

					g.setColor(Color.BLACK);
					g.drawString(text, x + position.x, y + position.y);
					maxWidth = Math.max(maxWidth, metrics.stringWidth(text));
					y += metrics.getHeight();
					text = restText.trim();
					restText = "";
				}
				note.size = new Dimension(maxWidth, y - metrics.getHeight());
				
				g.setColor(note == currentNote ? Color.green : Color.lightGray);

				g.drawLine(note.position.x + position.x - metrics.getDescent(), note.position.y + position.y - metrics.getAscent(),
						note.position.x + position.x - metrics.getDescent() + 5, note.position.y + position.y - metrics.getAscent());
				g.drawLine(note.position.x + position.x - metrics.getDescent(), note.position.y + position.y - metrics.getAscent(),
						note.position.x + position.x - metrics.getDescent(), note.position.y + position.y - metrics.getAscent() + 5);
				
				g.drawLine(x + position.x + maxWidth + metrics.getDescent(), note.size.height + position.y + metrics.getDescent(),
						x + position.x + maxWidth + metrics.getDescent() - 5, note.size.height + position.y + metrics.getDescent());
				g.drawLine(x + position.x + maxWidth + metrics.getDescent(), note.size.height + position.y + metrics.getDescent(),
						x + position.x + maxWidth + metrics.getDescent(), note.size.height + position.y - 5 + metrics.getDescent());
			}
		}
	}
	
	private void drawStrokes(Graphics2D g) {
		g.setColor(Color.gray);
		Point position = getImagePosition();
		for (PhotoStroke stroke : strokes) {
			g.setPaintMode();
			if (stroke.points.size() > 1)
				for (int i = 1; i < stroke.points.size(); i++)
					g.drawLine(
						(int) stroke.points.get(i - 1).getX() + position.x,
						(int) stroke.points.get(i - 1).getY() + position.y,
						(int) stroke.points.get(i).getX() + position.x,
						(int) stroke.points.get(i).getY() + position.y);
		}
	}
	
	private void drawImage(Graphics2D g) {
		Dimension size = getImageSize();
		Point position = getImagePosition();
		
		if (!isFlipped)
			g.drawImage(image, position.x, position.y, null);
		else {
			g.setColor(Color.white);
			g.fillRect(position.x, position.y, size.width, size.height);
		}
		
		Rectangle vRect = getVisibleRect();
		Rectangle clipRect = new Rectangle(position.x, position.y, size.width, size.height);
		g.setClip(clipRect.intersection(vRect));
	}
	
	private void drawCorners(Graphics2D g) {
		Dimension size = getImageSize();
		Point position = getImagePosition();
		
		g.setColor(Color.black);
		int d = 40;
		g.fillArc(position.x - d / 2 - 5, position.y - d / 2 - 5, d, d, 270, 90);
		g.fillArc(position.x + size.width - d / 2 + 5, position.y + size.height - d / 2 + 5, d, d, 90, 90);
		g.setColor(Color.darkGray);
		g.drawArc(position.x - d / 2 - 5, position.y - d / 2 - 5, d, d, 270, 90);
		g.drawArc(position.x + size.width - d / 2 + 5, position.y + size.height - d / 2 + 5, d, d, 90, 90);
	}
	
	private Point getImagePosition() {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		int x = (int) Math.max((getWidth() - w) / 2, 0);
		int y = (int) Math.max((getHeight() - h) / 2, 0);
		return new Point(x, y);
	}
	
	private Dimension getImageSize() {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		return new Dimension(w, h);
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
