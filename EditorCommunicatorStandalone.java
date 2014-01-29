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

	
	public void draw(Graphics g, int selected) {
		System.out.println("editor called draw");
		editor.getSketch().draw(g, selected);
	}
	public int container(int x, int y) { //
		return editor.getSketch().container(x, y);
	}
	public void doAddAt(int idx, Shape shape) {
		editor.getSketch().doAddAt(idx, shape);
	}
	public int doAddEnd(Shape shape) { //
		return editor.getSketch().doAddEnd(shape);
	}
	public void doRecolor(int idx, Color c) {//
		editor.getSketch().doRecolor(idx, c);
	}
	public void doMoveTo(int idx, int x1, int y1) { //
		editor.getSketch().doMoveTo(idx, x1, y1);
	}
	public void doDelete(int idx) { //
		editor.getSketch().doDelete(idx);
	}
}
