package PhotoViewerPackage;

import java.awt.Point;
import java.util.ArrayList;

public class PhotoStroke {
	public ArrayList<Point> points = new ArrayList<Point>();
	
	public void addPoint(Point p) {
		if (points.size() == 0 || !points.get(points.size() - 1).equals(p))
			points.add(p);
	}
}
