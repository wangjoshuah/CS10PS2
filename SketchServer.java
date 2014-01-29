import java.net.*;
import java.util.*;
import java.io.*;

/**
 * A subclass of Sketch for the server, getting requests from the clients,
 * updating the overall state, and passing them on to the clients
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 */
public class SketchServer {
	private ServerSocket listen;						// for accepting connections
	private ArrayList<SketchServerCommunicator> comms;	// all the connections with clients
	private Sketch sketch;								// the state of the world
	
	public SketchServer(ServerSocket listen) {
		this.listen = listen;
		sketch = new Sketch();
		comms = new ArrayList<SketchServerCommunicator>();
	}

	public Sketch getSketch() {
		return sketch;
	}
	
	/**
	 * The usual loop of accepting connections and firing off new threads to handle them
	 */
	public void getConnections() throws IOException {
		while (true) {
			SketchServerCommunicator comm = new SketchServerCommunicator(listen.accept(), this); //server start listening
			comm.setDaemon(true); //thread ends when communicator's disconnected
			comm.start(); //start the communicator
			addCommunicator(comm); //add the communicator to the array we are keeping
		}
	}

	/**
	 * Adds the communicator to the list of current communicators
	 */
	public synchronized void addCommunicator(SketchServerCommunicator comm) { //when we get a new connection,
		comms.add(comm); //add a communicator
	}

	/**
	 * Removes the communicator from the list of current communicators
	 */
	public synchronized void removeCommunicator(SketchServerCommunicator comm) { //delete an editor when it closes
		comms.remove(comm); //removes the line to that editor
	}

	/**
	 * Sends the message from the one communicator to all (including the originator)
	 */
	public synchronized void broadcast(String msg) {
		for (SketchServerCommunicator comm : comms) { //for all registered comms
			comm.send(msg); //send the same message we just got
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("waiting for connections");
		new SketchServer(new ServerSocket(4242)).getConnections();
	}
}
