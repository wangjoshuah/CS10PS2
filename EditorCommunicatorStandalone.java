import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * Shortcircuits communication to/from the server for the editor
 */
public class EditorCommunicatorStandalone extends EditorCommunicator {
	public EditorCommunicatorStandalone(Editor editor) {
		super(null, editor);
	}

	// Handle editor requests by doing them immediately on the editor's sketch
	// YOUR CODE HERE (override methods)
}
