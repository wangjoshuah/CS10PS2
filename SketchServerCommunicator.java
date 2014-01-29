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
			System.out.println("get ready to start showing the world to them");
			for (int index = 0; index < this.server.getSketch().size(); index ++) {
				System.out.println("index # " + index);
				Shape tmpShape = this.server.getSketch().get(index);
				if (tmpShape != null) {
					System.out.println("Send shape " + tmpShape.toString());
					send("doAddAt " + tmpShape.toString() + " " + index);
					System.out.println("shared a shape");
				}
			}

			// Keep getting and handling messages from the client
			// YOUR CODE HERE
			String line;
			while( (line = in.readLine()) != null) {
				System.out.println("stuck in while loop");
				Message msg = new Message(line);
				msg.update(server.getSketch());
				server.broadcast(msg.toString());
				// Broadcast the latest change to all clients.
			}
			System.out.println("exited while loop");

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
