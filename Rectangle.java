import java.awt.*;

/**
 * A rectangle-shaped Shape
 */
public class Rectangle extends Shape {
	
	 public Rectangle(int x1, int y1, int x2, int y2, Color c) {
		 super(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1,  y2), c);
	 }
	 
	public void draw(Graphics g) {
		  g.setColor(color);
		  g.fillRect(x1, y1, x2-x1, y2-y1);
		 }
	public boolean contains(int x, int y) {
		 return x1 < x && y1 < y && x < x2 && y < y2;
	}
	public String toString() {
			return "rectangle "+super.toString();
	}
	public void border(Graphics g) {
		((Graphics2D)g).setStroke(dottedStroke);
		g.setColor(Color.green);
		g.fillRect(x1, y1, x2-x1, y2-y1);
	}
}
