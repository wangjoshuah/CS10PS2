import java.awt.Color;

/**
 * Representation of a message for updating a sketch
 */
public class Message {
	// Instance variables
	// YOUR CODE HERE
	String constructorString; //the string that started it all
	String[] tokens; //our array of strings

	/**
	 * Initializes it from a string representation used for communication
	 * @param msg
	 */
	public Message(String msg) {
		System.out.println("Message created with " + msg);
		// YOUR CODE HERE
		constructorString = msg; //retain the original string to publish it again later
		tokens = constructorString.split(" "); //but still deliminate the string based on spaces
	}

	/**
	 * Updates the sketch according to the message
	 * This may result in a modification of the message to be passed on
	 */
	public void update(Sketch sketch) { //we can ask it to refresh and a bunch of different things may happen
		// YOUR CODE HERE
		String action = tokens[0]; //what action should we be doing?
		String shape = tokens[1]; //shape type
		int x1 = Integer.parseInt(tokens[2]); //left side
		int y1 = Integer.parseInt(tokens[3]); //top side
		int x2 = Integer.parseInt(tokens[4]); //right side
		int y2 = Integer.parseInt(tokens[5]); //bottom side
		Color c = Color.decode(tokens[tokens.length - 2]); //color
		int index = Integer.parseInt(tokens[tokens.length - 1]); //index in the array

		switch (action) { //check what action we are performing
		case "doAddEnd": //coming from client we want to add a shape at the end
			switch (shape) { //test what shape it is
			case "ellipse": //if it is an ellipse
				Ellipse ellipse = new Ellipse(x1, y1, x2, y2, c); //create same ellipse in server
				index = sketch.doAddEnd(ellipse); //add our ellipse into the server
				break;
			case "rectangle": //if it is a rectangle
				Rectangle rectangle = new Rectangle(x1, y1, x2, y2, c); //create same rectangle in server
				index = sketch.doAddEnd(rectangle); //add our rectangle into the server
				break;
			case "segment": //if it is a lonely segment
				Segment segment = new Segment(x1, y1, x2, y2, c); //create same segment in our server
				index = sketch.doAddEnd(segment);
				break;
			}
			break;
		case "doAddAt": //add it at an index
			switch (shape) {
			case "ellipse": //if it is an ellipse
				Ellipse ellipse = new Ellipse(x1, y1, x2, y2, c); //create same ellipse in client
				sketch.doAddAt(index, ellipse); //add our ellipse into the client
				break;
			case "rectangle": //if it is a rectangle
				Rectangle rectangle = new Rectangle(x1, y1, x2, y2, c); //create same rectangle in client
				sketch.doAddAt(index, rectangle); //add our rectangle into the client
				break;
			case "segment": //if it is a lonely segment
				Segment segment = new Segment(x1, y1, x2, y2, c); //create same segment in our client
				sketch.doAddAt(index, segment); //add our segment into the client
				break;
			}
		case "doRecolor": //recolor a shape
			sketch.doRecolor(index, c); //the sketch will recolor it with color c
			break;
		case "doDelete": //delete a shape
			sketch.doDelete(index); //sketch will delete it at index (index)
			break;
		case "doMoveTo": //move a shape
			sketch.doMoveTo(index, x1, y1); //by the top left corner
			break;
		}

		switch (tokens[0]) {
		case "ellipse":
			Ellipse ellipse = new Ellipse(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Color.decode(tokens[5]));
			if (tokens[7].equals("-1")) { //if we are coming from the client side, they don't assign an index
				System.out.println("from client to server");
				index = sketch.doAddEnd(ellipse); //we add it on the end for the server
			}
			else { //or if it has an index
				System.out.println("from server to client");
				sketch.doAddAt(Integer.parseInt(tokens[7]), ellipse); //we add it in at the index for the client side
			}
			break;

		default:
			break;
		}
	}

	/**
	 * Converts to a string representation for communication
	 */
	public String toString() {
		// YOUR CODE HERE
		return constructorString;
	}
}
