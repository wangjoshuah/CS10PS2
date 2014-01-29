import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Client-server graphical editor
 * Dartmouth CS 10, Winter 2014
 */

public class Editor extends JFrame {	
	private static String serverIP = "localhost";			// IP address of sketch server
															// null for standalone;
															// "localhost" for your own machine;
															// or ask a friend for IP address

	private static final int width = 800, height = 800;		// canvas size

	// GUI components
	private JComponent canvas, gui;
	JDialog colorDialog;
	JColorChooser colorChooser;
	JLabel colorL;

	// Current settings on GUI
	private boolean drawing = true;				// adding objects vs. moving/deleting/recoloring them
	private String shape = "ellipse"; 			// type of object to add
	private Color color = Color.black;			// current drawing color

	// Drawing state
	private Point point = null;					// initial mouse press for drawing; current position for moving
	private Shape current = null;				// the object currently being drawn (if one is)
	private int selected = -1;					// index of object (if any; -1=none) has been selected for deleting/recoloring
	private boolean dragged = false;			// keep track of whether object was actually moved
	
	// The sketch and communication
	private Sketch sketch;						// holds and handles all the drawn objects
	private EditorCommunicator comm;			// communication with the sketch server

	public Editor() {
		super("Graphical Editor"); // call super

		sketch = new Sketch(); //give us a sketch
		
		// Connect to server
		if (serverIP == null) { //if we don't have a server address,
			comm = new EditorCommunicatorStandalone(this); //then we are working around it with standalone
		}
		else { //otherwise given a server,
			comm = new EditorCommunicator(serverIP, this); //create a new editorcomm that will connect to that server using this editor
			comm.start(); //start the communicator
		}

		// Helpers to create the canvas and GUI (buttons, etc.)
		setupCanvas();
		setupGUI();

		// Put the buttons and canvas together into the window
		Container cp = getContentPane(); //glad to have this premade
		cp.setLayout(new BorderLayout());
		cp.add(canvas, BorderLayout.CENTER);
		cp.add(gui, BorderLayout.NORTH);

		// Usual initialization
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Creates a panel with all the buttons, etc.
	 */
	private void setupGUI() {
		// Toggle whether drawing or editing
		JToggleButton drawingB = new JToggleButton("drawing", drawing);
		drawingB.addActionListener(new AbstractAction("drawing") {
			public void actionPerformed(ActionEvent e) {
				drawing = !drawing; //if we hit the drawing button, we just flip whether we are drawing
				current = null; //release our hold on the last image
				selected = -1; //unselect whatever we had previously selected
			}
		});

		// Select type of shape
		String[] shapes = {"ellipse", "rectangle", "segment"};
		JComboBox shapeB = new JComboBox(shapes);
		shapeB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				shape = (String)((JComboBox)e.getSource()).getSelectedItem(); //select the item and retain it as our shape type in the dropdown menu
			}
		});

		// Select drawing/recoloring color
		// Following Oracle example
		JButton chooseColorB = new JButton("choose color");
		colorChooser = new JColorChooser();
		colorDialog = JColorChooser.createDialog(chooseColorB, //is this the popup?
				"Pick a Color",
				true,  //modal
				colorChooser,
				new AbstractAction() { 
			public void actionPerformed(ActionEvent e) {
				color = colorChooser.getColor(); //get the color from the colorChooser
				colorL.setBackground(color);  //and set that as our retained background color
			} 
		}, //OK button
		null); //no CANCEL button handler
		chooseColorB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				colorDialog.setVisible(true);
			}
		});
		colorL = new JLabel();
		colorL.setBackground(Color.black);
		colorL.setOpaque(true);
		colorL.setBorder(BorderFactory.createLineBorder(Color.black));
		colorL.setPreferredSize(new Dimension(25, 25));

		// Delete object if it is selected
		JButton deleteB = new JButton("delete");
		deleteB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (current != null && selected != -1) {
					comm.doDelete(selected); //tell the server we wanted to delete this
					current = null; //release it and let it be garbage collected so it isn't retained in our local editor
					selected = -1;//we aren't selecting anything so change the selected index
					repaint();
				}
			}
		});

		// Recolor object if it is selected
		JButton recolorB = new JButton("recolor");
		recolorB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (selected != -1) { //if we have selected something
					comm.doRecolor(selected, color); //ask the server to repaint this selected image when we click the recolor button
				}
			}
		});

		// Put all the stuff into a panel
		gui = new JPanel();
		gui.setLayout(new FlowLayout());
		gui.add(shapeB);
		gui.add(chooseColorB);
		gui.add(colorL);
		gui.add(new JSeparator(SwingConstants.VERTICAL));
		gui.add(drawingB);
		gui.add(deleteB);
		gui.add(recolorB);
	}

	private void setupCanvas() {
		canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Display the sketch
				// Also display the object currently being drawn in this editor (not yet part of the sketch)
				if (current != null) {
					current.draw(g);
				}
				sketch.draw(g, selected);				
			}
		};
		
		canvas.setPreferredSize(new Dimension(width, height));

		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				point = event.getPoint();
				// In drawing mode, start a new object;
				// in editing mode, set selected according to which object contains the point
				// YOUR CODE HERE
				selected = -1; //reset selected when we click our mouse
				if(drawing) { //if we are in drawing mode,
					if (shape.equals("ellipse")) { //If the string in the combo box is equal to "eclipse, set current as a new ellipse
						current = new Ellipse(point.x, point.y, point.x, point.y, color); //create a new ellipse
					}
					else if (shape.equals("rectangle")) { //Same as above with rectangle
						current = new Rectangle(point.x, point.y, point.x, point.y, color); //create a new rectangle
					}
					else if (shape.equals("segment")) { //Same as above with line segment
						current = new Segment(point.x, point.y, point.x, point.y, color); //construct the segment
					}
				}
				else { //if we aren't drawing (in selection mode)
					selected = sketch.container(point.x, point.y); //Sets selected if the current point is in the boundaries of an object
					if (selected != -1) { //if something is selected
						current = sketch.get(selected); //make that our current shape
					}
				}
				repaint(); //repaint the canvas every time the mouse is pressed
			}

			public void mouseReleased(MouseEvent event) {
				// Pass the update (added object or moved object) on to the server
				if (drawing) { //if we are drawing,
					comm.doAddEnd(current); //add the shape at the end to our server
				}
				else if (selected != -1) { //if we are not drawing and we did select something,
					comm.doMoveTo(selected, current.x1, current.y1); //move the object because we 
				}
			}
		});		

		canvas.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event) {
				// In drawing mode, update the other corner of the object;
				// in editing mode, move the object by the difference between the current point and the previous one
				// YOUR CODE HERE
				Point p2 = event.getPoint();
				if (drawing) { //If drawing is true
					current.setCorners(point.x, point.y, p2.x, p2.y); //set the boundary box of the Shape
					repaint(); //show how large t is
				}
				else if (selected != -1) { //If drawing is not true and selected is not -1
					current.moveBy(p2.x - point.x, p2.y - point.y); //Move the object
					point = event.getPoint(); //get a new point
					repaint(); //repaint while moving it
				}
			}				
		});
	}

	/**
	 * Getter for the sketch instance variable
	 * @return
	 */
	public Sketch getSketch() {
		return sketch; //for use in the future, protects a private variable
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Editor(); //let's go!
			}
		});	
	}
}
