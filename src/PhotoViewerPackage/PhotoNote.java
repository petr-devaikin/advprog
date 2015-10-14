package PhotoViewerPackage;

import java.awt.Dimension;
import java.awt.Point;

public class PhotoNote {
	public String text = "";
	public Point position = new Point(0, 0);
	public Dimension size = new Dimension(0, 0);
	
	public PhotoNote(Point p) {
		position = p;
	}
}
