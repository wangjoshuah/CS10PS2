import java.awt.*;

/**
 * A rectangle-shaped Shape
 * Written by Josh Wang '15 and Alex Tsu '14
 */
public class Rectangle extends Shape {
	
	 public Rectangle(int x1, int y1, int x2, int y2, Color c) {
		 super(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1,  y2), c);
	 }
	
	public boolean contains(int x, int y) {
		return (x1 < x) && (x < x2) && (y1 < y) && (y < y2); //if it is between the left and right and top and bottom
	}

	public void setCorners(int x1, int y1, int x2, int y2) {
		// Infer upper left and lower right
		this.x1 = Math.min(x1, x2); this.y1 = Math.min(y1, y2);
		this.x2 = Math.max(x1, x2); this.y2 = Math.max(y1,  y2);
	}

	public void draw(Graphics g) { //draw the rectangle
		g.setColor(color); //set the color we will draw in
		g.fillRect(x1, y1, x2-x1, y2-y1); //then ask the graphics to make us a rectangle
	}

	public void border(Graphics g) {
		((Graphics2D)g).setStroke(dottedStroke); //get a 2d doted strike representation
		g.setColor(Color.green); //set it to green
		g.drawRect(x1, y1, x2-x1, y2-y1); //then draw it
	}

	public String toString() {
		return "rectangle "+super.toString(); //differentiate what type of plant.
	}
}
