import java.io.*;
import java.net.Socket;

/**
 * Handles communication between the server and one client, for SketchServer
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			// YOUR CODE HERE
			for (int index = 0; index < this.server.getSketch().size(); index ++) { //for all shapes in the array
				Shape tmpShape = this.server.getSketch().get(index); //get the shape
				if (tmpShape != null) { //if the shape exists
					send("doAddAt " + tmpShape.toString() + " " + index); //communicate that shape to the new editor
				}
			}

			// Keep getting and handling messages from the client
			// YOUR CODE HERE
			String line; //allocate memory for line
			while( (line = in.readLine()) != null) { //while we are receiving a line,
				Message msg = new Message(line); //create a message with the line
				msg.update(server.getSketch()); //update the sketch according to the message
				server.broadcast(msg.toString()); // Broadcast the latest change to all clients.
			}

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
			}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
