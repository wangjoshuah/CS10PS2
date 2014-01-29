import java.util.*;
import java.awt.*;

/**
 * Maintains a set of shared graphical shapes
 */

public class Sketch {
	private ArrayList<Shape> shapes;

	public Sketch() { // constructor
		shapes = new ArrayList<Shape>(); //new generic ArrayList of Shapes
	}
	
	public synchronized int size() { //if they want to know our size,
		return shapes.size(); //size
	}
	
	public synchronized Shape get(int idx) { //if they want to get a shape
		return shapes.get(idx); //sync it up
	}
	
	/**
	 * Returns the index of the topmost shape containing the point, 
	 * or -1 if it's not in any shape
	 */
	public synchronized int container(int x, int y) { 
		// YOUR CODE HERE
		for (int idx = shapes.size() - 1; idx >= 0; idx--) { //go through all indexes in the shapes arraylist from top to bottom
			if (shapes.get(idx) != null && shapes.get(idx).contains(x, y)) { //if the shape exists, and contains the point,
				return idx; //return the index where we are at and leave this container call
			}
		} 
		return -1; //if no shapes contain the points or we don't have any shapes yet, return -1
	}
	
	/**
	 * Draws all the shapes on the graphics, properly ordered from bottom to top
	 * If one is selected, gives it a border
	 */
	public synchronized void draw(Graphics g, int selected) {
		// YOUR CODE HERE
		for (int idx = 0; idx < shapes.size(); idx++) { //go through all indexes in the shapes arraylist from bottom up
			if (shapes.get(idx) != null) { //if the shape exists
				shapes.get(idx).draw(g); //draw the shape
				if (idx == selected) { //if the shape is selected,
					shapes.get(idx).border(g); //draw a border
				}
			}
		}
	}
	
	/**
	 * Adds the shape to the sketch at the requested index
	 */
	public synchronized void doAddAt(int idx, Shape shape) {
		// Add holes up to the point where the shape is supposed to go
		while (shapes.size() <= idx) shapes.add(null);
		shapes.set(idx, shape); //create our shape and add it
	}
	
	/**
	 * Adds the shape to the sketch at the end of the list and returns its index
	 */
	public synchronized int doAddEnd(Shape shape) {
		shapes.add(shape); //add it at the end of the AL
		return shapes.size()-1; //return the index where it is at
	}
	
	/**
	 * Deletes a shape by making its position in the sketch list null
	 * @param idx
	 */
	public synchronized void doDelete(int idx) { //delete index
		if (idx != -1) shapes.set(idx, null); //if not (unselected) then actually delete it
	}
	
	/**
	 * Sets the shape of the given index to the given color
	 */
	public synchronized void doRecolor(int idx, Color c) { //set the color of an object
		shapes.get(idx).color = c; //by getting the shape at index (if it exists) and apply oclor
	}
	
	/**
	 * Moves the shape of the given index by the given step
	 */
	public synchronized void doMoveTo(int idx, int x1, int y1) { //if we want to drag and move	
		shapes.get(idx).moveTo(x1, y1); //ask the server if it can end up there.
	}	
}
