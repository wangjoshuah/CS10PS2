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

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		if (serverIP == null) {
			System.out.println("operating in standalone mode");
			out = new PrintWriter(System.out);
		}
		else {
			System.out.println("connecting to " + serverIP + "...");
			try {
				Socket sock = new Socket(serverIP, 4242);
				out = new PrintWriter(sock.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				System.out.println("...connected");
			}
			catch (IOException e) {
				System.err.println("couldn't connect");
			}
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}
	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			// YOUR CODE HERE
			// Open the socket with the server, and then the writer and reader
			System.out.println("connecting...");
			Socket sock = new Socket("localhost", 4242);
			PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
			
			//Set message handler
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("message received");
				Message msg = new Message(line);
				msg.update(editor.getSketch());
			}
			
			//set the out line
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
		
<<<<<<< HEAD
//		try{
//			out.close();
//			in.close();
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
=======
>>>>>>> 0625a9247ee467b077a940bd32ca596f4b21fbb9
		
	}

	String noShape = (null + " " + null + " ");
	public void doAddAt(int idx, Shape shape) {
		send("doAddAt " + shape.toString() + " " + idx); //passes a string with 8 items separated by spaces. These 8 items are passed to the Message class and read. This 
	}
	public void doAddEnd(Shape shape) {
		send("doAddEnd " + shape.toString() + " -1"); //action plus shape plus index
	}
	public void doRecolor(int idx, Color c) {
		send("doRecolor " + "NoShape " + "0 0 0 0 " + c.toString() + " " + Integer.toString(idx)); 
	}
	public void doMoveTo(int idx, int x1, int y1) {
		send("doMoveTo " + "NoShape " + Integer.toString(x1) + " " + Integer.toString(y1) + " 0 0 NoColor " + Integer.toString(idx));
	}
	public void doDelete(int idx) {
		send("doDelete " + "NoShape 0 0 0 0 NoColor "+ Integer.toString(idx));
	}

	


	

	// Send editor requests to the server
	// YOUR CODE HERE (methods for different requests)
}
