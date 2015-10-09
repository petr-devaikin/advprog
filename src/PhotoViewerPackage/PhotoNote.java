package PhotoViewerPackage;

import java.awt.Point;

public class PhotoNote {
	public String text = "";
	public Point position = new Point(0, 0);
	
	public PhotoNote(Point p) {
		position = p;
	}
}
