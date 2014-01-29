import java.awt.Color;

/**
 * Representation of a message for updating a sketch
 */
public class Message {
	// Instance variables
	// YOUR CODE HERE
	String constructorString; //the string that started it all
	
	/**
	 * Initializes it from a string representation used for communication
	 * @param msg
	 */
	public Message(String msg) {
		// YOUR CODE HERE
		constructorString = msg;
	}
	
	/**
	 * Updates the sketch according to the message
	 * This may result in a modification of the message to be passed on
	 */
	public void update(Sketch sketch) {
		// YOUR CODE HERE
	}

	/**
	 * Converts to a string representation for communication
	 */
	public String toString() {
		// YOUR CODE HERE
		String returnString = new String();
		return returnString;
	}
}
