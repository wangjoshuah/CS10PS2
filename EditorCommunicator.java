import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * Handles communication to/from the server for the editor
 * Dartmouth CS 10, Winter 2014
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for
	private Socket sock; 			// the underlying handler for communication

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor; //assign our parent editor within our creator
		if (serverIP == null) { //if we are running in standalone mode
			out = new PrintWriter(System.out); //create a printer for the sketch
		}
		else { //if we are using a server component
			System.out.println("connecting to " + serverIP + "...");//print where we are connecting to...
			try { //try to connect
				sock = new Socket(serverIP, 4242); //get a socket
				out = new PrintWriter(sock.getOutputStream(), true); //get a printwriter to that socket's outstream
				in = new BufferedReader(new InputStreamReader(sock.getInputStream())); //get a line reader from the socket's in stream
				System.out.println("...connected"); //if we did connect successfully...
			}
			catch (IOException e) { //no connection :(
				System.err.println("couldn't connect"); //tell them we couldn't connect
			}
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) { //abstract sending a message
		out.println(msg); //send the message in the out stream
		editor.repaint(); //refresh our canvas whenever we send a message
	}
	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try { //do this
			// Handle messages
			// YOUR CODE HERE			
			//Set message handler
			String line; //allocate memory for a String object 
			while ((line = in.readLine()) != null) { //while we get a line from the in stream (server)
				Message msg = new Message(line); //create a message from what we got from the server
				msg.update(editor.getSketch()); //update based on the server's message
				editor.repaint(); //repaint after getting a line
			}
		}
		catch (IOException e) { //if we couldn't run and communicated
			e.printStackTrace(); //print an error
		}
		finally { //then in the end
			System.out.println("server hung up"); //hang up and leave
		}
	}

	// Send editor requests to the server
	// YOUR CODE HERE (methods for different requests)
	public void doAddAt(int idx, Shape shape) { //tell our editor to add a shape at a specific index
		send("doAddAt " + shape.toString() + " " + idx); //passes a string with 8 items separated by spaces. These 8 items are passed to the Message class and read. This
	}
	public void doAddEnd(Shape shape) { //ask server to add a shape at the end of the array
		send("doAddEnd " + shape.toString() + " -1"); //action plus shape plus index
	}
	public void doRecolor(int idx, Color c) { //ask server to recolor a shape 
		send("doRecolor " + editor.getSketch().get(idx).toString() + " " + c.hashCode() + " " + Integer.toString(idx)); //pass the action, the shape, and the color and the index. We do it differently here because the color in the message takes the 2nd to last token. So here, the shape's original color is never used 
	}
	public void doMoveTo(int idx, int x1, int y1) { //move the shape to x1, y1
		send("doMoveTo " + editor.getSketch().get(idx).toString() + " " + Integer.toString(idx)); //ask it to move on the server 
	}
	public void doDelete(int idx) { //ask server to forget about him
		send("doDelete " + editor.getSketch().get(idx).toString() + " "+ Integer.toString(idx)); //delete shape at index...
	}
}
