package PhotoComponent;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

public class PhotoNote {
	public String text = "";
	public Point position = new Point(0, 0);
	private Integer fontSize = 12;

	public void IncreaseSize() {
		if (fontSize < 40)
			fontSize++;
	}
	
	public void DecreaseSize() {
		if (fontSize > 5)
			fontSize--;
	}
	
	public Font getFont() {
		return new Font("Arial", Font.PLAIN, fontSize);
	}
	
	public PhotoNote(Point p) {
		position = p;
	}
}
