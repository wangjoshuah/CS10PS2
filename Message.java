import java.awt.Color;

/**
 * Representation of a message for updating a sketch
 */
public class Message {
	// Instance variables
	// YOUR CODE HERE
	String constructorString; //the string that started it all
	String[] tokens;
	String indexString;
	
	/**
	 * Initializes it from a string representation used for communication
	 * @param msg
	 */
	public Message(String msg) {
		System.out.println("Message created with " + msg);
		// YOUR CODE HERE
		tokens = msg.split(" ");		
	}
	
	/**
	 * Updates the sketch according to the message
	 * This may result in a modification of the message to be passed on
	 */
	public void update(Sketch sketch) {
		// YOUR CODE HERE
		switch (tokens[0]) {
		case "ellipse":
			Ellipse ellipse = new Ellipse(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Color.decode(tokens[5]));
			if (tokens.length == 6) { //if we are coming from the client side, they don't assign an index
				System.out.println("from client to server");
				indexString = String.valueOf(sketch.doAddEnd(ellipse)); //we add it on the end for the server
			}
			else { //or if it has an index
				System.out.println("from server to client");
				sketch.doAddAt(Integer.parseInt(tokens[6]), ellipse); //we add it in at the index for the client side
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
		return (tokens.toString() + " " + indexString);
	}
}
