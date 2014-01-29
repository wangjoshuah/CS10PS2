import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Shortcircuits communication to/from the server for the editor
 */
public class EditorCommunicatorStandalone extends EditorCommunicator  {
	public EditorCommunicatorStandalone(Editor editor) {
		super(null, editor);
	}

	// Handle editor requests by doing them immediately on the editor's sketch

	


	public void doAddAt(int idx, Shape shape) { // when we add a shape at an index
		editor.getSketch().doAddAt(idx, shape); //do it there
		editor.repaint(); //then repaint
	}

	public void doRecolor(int idx, Color c) {//
		editor.getSketch().doRecolor(idx, c); //change color now
		editor.repaint(); //repaint every time
	}
	public void doMoveTo(int idx, int x1, int y1) { //change x1y1 and others follow
		editor.getSketch().doMoveTo(idx, x1, y1); //new style
		editor.repaint(); //that others will wear
	}
	public void doDelete(int idx) { // //do delete an object
		editor.getSketch().doDelete(idx); //when we absolutely don't need it
		editor.repaint(); //repaint!
	}
}
