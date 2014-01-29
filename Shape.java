import java.awt.*;

/**
 * Abstract superclass for shapes, with a bounding box defined by a pair of points
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 */
public abstract class Shape {
	protected int x1, y1, x2, y2;		// opposite corners of bounding box
	protected Color color;				// for drawing

	// predefined strokes for drawing
	protected static Stroke solidStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
	protected static Stroke dottedStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {10,10}, 0);

	/**
	 * Initializes the shape with the two corners and the color
	 */
	public Shape(int x1, int y1, int x2, int y2, Color c) { //constructor
		this.x1 = x1; this.y1 = y1; //sets the shape corners
		this.x2 = x2; this.y2 = y2; //bottom and right
		this.color = c; //as well as shape color
	}

	/**
	 * Moves the shape such that its upper-left corner is at the new coordinates (and the lower-right follows rigidly)
	 */
	public void moveTo(int x1new, int y1new) { //move a shape?
		x2 += x1new - x1; y2 += y1new - y1; //to where,
		x1 = x1new; y1 = y1new; //x and y are at
	}

	/**
	 * Moves the shape by dx in the x coordinate and dy in the y coordinate
	 */
	public void moveBy(int dx, int dy) { //move by a margin
		x1 += dx; y1 += dy;
		x2 += dx; y2 += dy;
	}

	/**
	 * Sets the corners of the bounding box
	 */
	public void setCorners(int x1, int y1, int x2, int y2) { //set where it is
		this.x1 = x1; this.y1 = y1;
		this.x2 = x2; this.y2 = y2;
	}

	/**
	 * Recolors the shape
	 */
	public void setColor(Color c) { //change color
		this.color = c;
	}

	/**
	 * Returns whether or not the point is inside the shape
	 */
	public abstract boolean contains(int x, int y); //if it is in there, say yes

	/**
	 * Draws the shape via the graphics
	 */
	public abstract void draw(Graphics g); //check if the graphics are there then darw

	/**
	 * Draws a border of the shape via the graphics
	 */
	public abstract void border(Graphics g); //insert border ggraphics. //don't really need this in shape

	/**
	 * Returns a readable description of the shape's state
	 */
	public String toString() { //return value if it is a string
		return x1+" "+y1+" "+x2+" "+y2+" "+color.getRGB();
	}
}
