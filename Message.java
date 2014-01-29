import java.awt.Color;

/**
 * Representation of a message for updating a sketch
 */
public class Message {
	// Instance variables
	// YOUR CODE HERE
	String constructorString; //the string that started it all
	String[] tokens;
	int index;
	
	/**
	 * Initializes it from a string representation used for communication
	 * @param msg
	 */
	public Message(String msg) {
		System.out.println("Message created with " + msg);
		// YOUR CODE HERE
		constructorString = msg;
		tokens = constructorString.split(" ");		
	}
	
	/**
	 * Updates the sketch according to the message
	 * This may result in a modification of the message to be passed on
	 */
	public void update(Sketch sketch) {
		// YOUR CODE HERE
		String action = tokens[0];
		String shape = tokens[1];
		int x1 = Integer.parseInt(tokens[2]);
		int y1 = Integer.parseInt(tokens[3]);
		int x2 = Integer.parseInt(tokens[4]);
		int y2 = Integer.parseInt(tokens[5]);
		Color c = Color.decode(tokens[6]);
		index = Integer.parseInt(tokens[7]);
		
		switch (action) {
		case "doAddEnd": //coming from client we want to add a shape at the end
			switch (shape) { //test what shape it is
			case "ellipse": //if it is an ellipse
				Ellipse ellipse = new Ellipse(x1, y1, x2, y2, c); //create same ellipse in server
				index = sketch.doAddEnd(ellipse); //add our ellipse into the server
				break;

			default:
				break;
			}
			break;

		default:
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
