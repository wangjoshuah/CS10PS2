import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Client-server graphical editor
 * Dartmouth CS 10, Winter 2014
 */

public class Editor extends JFrame {	
	private static String serverIP = null;			// IP address of sketch server
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
		super("Graphical Editor");

		sketch = new Sketch();
		
		// Connect to server
		if (serverIP == null) {
			comm = new EditorCommunicatorStandalone(this);
		}
		else {
			comm = new EditorCommunicator(serverIP, this);
			comm.start();
		}

		// Helpers to create the canvas and GUI (buttons, etc.)
		setupCanvas();
		setupGUI();

		// Put the buttons and canvas together into the window
		Container cp = getContentPane();
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
				drawing = !drawing;
				System.out.println(drawing);
				current = null;
			}
		});

		// Select type of shape
		String[] shapes = {"ellipse", "rectangle", "segment"};
		JComboBox shapeB = new JComboBox(shapes);
		shapeB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				shape = (String)((JComboBox)e.getSource()).getSelectedItem();
			}
		});

		// Select drawing/recoloring color
		// Following Oracle example
		JButton chooseColorB = new JButton("choose color");
		colorChooser = new JColorChooser();
		colorDialog = JColorChooser.createDialog(chooseColorB,
				"Pick a Color",
				true,  //modal
				colorChooser,
				new AbstractAction() { 
			public void actionPerformed(ActionEvent e) {
				color = colorChooser.getColor();
				colorL.setBackground(color); 
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
				if (selected != -1) {
					comm.doDelete(selected);
					repaint();
				}
			}
		});

		// Recolor object if it is selected
		JButton recolorB = new JButton("recolor");
		recolorB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (selected != -1) {
					comm.doRecolor(selected, color);
					repaint();
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
					comm.draw(g, selected);
				}
			}
		};
		
		canvas.setPreferredSize(new Dimension(width, height));

		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				point = event.getPoint();
				// In drawing mode, start a new object;
				// in editing mode, set selected according to which object contains the point
				// YOUR CODE HERE
				if (drawing) { //if we are in drawing mode,
					switch (shape) { //look at the shape argument
					case "ellipse": //if our shape is an ellipse
						current = new Ellipse(point.x, point.y, point.x, point.y, color); //draw an ellipse
						break;
					case "rectangle": //if our shape is a rectangle,
						current = new Rectangle(point.x, point.y, point.x, point.y, color); //draw a rectangle
						break;
					case "segment": //if our shape is a segment
						current = new Segment(point.x, point.y, point.x, point.y, color); //draw a line segment
					default:
						break;
					}
					repaint(); //redraw it
				}
				else if (current != null ) {
					selected = comm.container(point.x, point.y);
					System.out.println(selected);
					repaint();
				}
			}

			public void mouseReleased(MouseEvent event) {
				// Pass the update (added object or moved object) on to the server
				if (drawing) {
					comm.doAddEnd(current);
				}
			}
		});		

		canvas.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event) {
				// In drawing mode, update the other corner of the object;
				// in editing mode, move the object by the difference between the current point and the previous one
				// YOUR CODE HERE
				Point p2 = event.getPoint();
				if (drawing) {
					current.setCorners(point.x, point.y, p2.x, p2.y);
					repaint();
				}
				else if (selected != -1) {
					comm.doMoveTo(selected, p2.x - point.x, p2.y - point.y);
					point = event.getPoint();
					repaint();
				}
			}				
		});
	}

	/**
	 * Getter for the sketch instance variable
	 * @return
	 */
	public Sketch getSketch() {
		return sketch;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Editor();
			}
		});	
	}
}
