import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.concurrent.locks.*;

import javax.swing.*;

/**
 * Provides a graphical user interface (GUI) for displaying a world in the
 * epidemic simulation. The world is displayed
 * as a grid of cells. Empty cells will show up as empty (black) in the grid.
 * Uninfected individuals appear as small blue dots, infected individuals appear
 * as large red dots, and recovered individuals appear as small pink dots.
 * The viewer does not distinguish between contagious and non-contagious
 * infected individuals; that is, it displays all infected individuals as large
 * red dots.
 * 
 * <p>The viewer also shows the integer values passed to its {@link #showWorld}
 * method:
 * <ol>
 * <li>The number of simulation steps executed so far.
 * <li>The current count of uninfected individuals.
 * <li>The current count of infected individuals.
 * <li>The current count of recovered individuals.
 * </ol></p>
 * 
 * <p><b>DO NOT EDIT THIS FILE.</b> You should put all your code for this
 * assighnment in Epidemic.java.</p>
 * 
 * @author pollen
 */
public class WorldViewer {

	private Snapshot current, previous, next;
	
	private final JFrame frame;
	private final JButton stepForwardButton;
	private final JButton stepBackButton;
	private final JLabel stepsLabel;
	private final JLabel uninfectedLabel;
	private final JLabel infectedLabel;
	private final JLabel recoveredLabel;
	
	/** Number of milliseconds to pause after each showWorld call. */
	private final int frameDuration;
	
	private final boolean enable;
	
	private final Lock lock = new ReentrantLock();
	private final Condition cond = lock.newCondition();
		
	/**
	 * Creates a new WorldViewer for use in students' code. The window will not
	 * be shown until the showWorld method is called at least once.
	 */
	public WorldViewer() {
		this(40, true);
	}
	
	/**
	 * Creates a new WorldViewer. The window will not be shown until the
	 * showWorld method is called at least once.
	 * 
	 * <p><b>Students' code should not call this constructor. It is for testing
	 * purposes only.</b>
	 * 
	 * @param frameDuration Number of milliseconds to pause after showing each
	 * frame.
	 */
	public WorldViewer(int frameDuration, boolean enable) {
		this.frameDuration = frameDuration;
		this.enable = enable;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Epidemic Simulation");
		frame.setSize(800, 600);
		frame.setLocation(100, 100);
		
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		frame.setContentPane(pane);
		
		pane.add(new WorldPane(), BorderLayout.CENTER);

		JPanel status = new JPanel();
		status.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		status.setLayout(new GridLayout(1, 6));
		pane.add(status, BorderLayout.SOUTH);
		
		stepBackButton = new JButton("<< Step");
		stepBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stepBack();
			}});
		stepBackButton.setEnabled(false);
		status.add(stepBackButton);
		
		stepForwardButton = new JButton("Step >>");
		stepForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stepForward();
			}});
		stepForwardButton.setEnabled(false);
		status.add(stepForwardButton);
		
		stepsLabel = new JLabel();
		status.add(stepsLabel);
		
		uninfectedLabel = new JLabel();
		status.add(uninfectedLabel);
		
		infectedLabel = new JLabel();
		status.add(infectedLabel);
		
		recoveredLabel = new JLabel();
		status.add(recoveredLabel);
	}
	
	private void stepForward() {
		if (next != null) {
			showWorld(next, true, true);
		} else {
			runSimulation();
		}
	}
	
	private void stepBack() {
		showWorld(previous, true, false);
	}
	
	/** Wakens the sleeping simulation to run another step. */
	private void runSimulation() {
		lock.lock();
		try {
			cond.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	/** Pauses the simulation. */
	private void pauseSimulation(boolean await) {
		if (await) {
			lock.lock();
			try {
				cond.await();
			} catch (InterruptedException e) {
				// Empty.
			} finally {
				lock.unlock();
			}
		} else {
			try {
				Thread.sleep(frameDuration);
			} catch (InterruptedException e) {
				// Empty.
			}
		}
	}
	
	/**
	 * Displays the current state of the simulated world on the GUI window.
	 * 
	 * @param world Matrix of tiles that make up the world.
	 * @param stepsTaken Number of steps that have been taken so far in this
	 *        simulation.
	 * @param numUninfected Current number of uninfected individuals in the
	 *        simulation.
	 * @param numInfected Current number of infected individuals in the
	 *        simulation.
	 * @param numRecovered Current number of recovered individuals in the
	 *        simulation.
	 * @param pause True to pause the simulation until the user hits the "Step"
	 *        button. False to keep running without requiring the user to hit
	 *        the "Step" button. Feel free to pass either {@code true} or {@code
	 *        false} here. The <a href=
	 *        "http://pages.cs.wisc.edu/~cs302/programs/p2/example-video.html">
	 *        example video</a> was generated with {@code pause} set to {@code
	 *        false}.
	 */
	public void showWorld(
			int[][] world,
			int stepsTaken,
			int numUninfected,
			int numInfected,
			int numRecovered,
			boolean pause
	) {
		showWorld(new Snapshot(
				world,
				stepsTaken,
				numUninfected,
				numInfected,
				numRecovered),
				pause, true);
		pauseSimulation(pause);
	}
	
	private void showWorld(
			Snapshot newSnapshot,
			boolean await,
			boolean forward) {
		if (forward) {
			previous = current;
			next = null;
		} else {
			previous = null;
			next = current;
		}
		current = newSnapshot;
	
		stepForwardButton.setEnabled(next != null || await);
		stepBackButton.setEnabled(previous != null && await);
		
		update();
	}
	
	/** Updates the painted display based on the current Snapshot. */
	private void update() {
		if (enable) {
			stepsLabel.setText(String.format(
					"  Steps: %d", current.stepsTaken));
			uninfectedLabel.setText(String.format(
					"  Uninfected: %d", current.numUninfected));
			infectedLabel.setText(String.format(
					"  Infected: %d", current.numInfected));
			recoveredLabel.setText(String.format(
					"  Recovered: %d", current.numRecovered));
			
			frame.setVisible(true); // Make sure the window is shown.
			frame.repaint();
		}
	}
	
	/**
	 * Prints a representation of the current state of the world to System.out.
	 * While your program is not required to ever call this method, you may find
	 * it useful for debugging purposes.
	 * <ul>
	 * <li>Empty cells are represented by a space.
	 * <li>Uninfected cells are represented by a period (.).
	 * <li>Recovered cells are represented by a star (*).
	 * <li>Infected cells are represented by an at-sign (@).
	 * </ul>
	 * 
	 * @param world World array to print.
	 */
	public static void printWorld(int[][] world) {
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[i].length; j++) {
				char c;
				switch (world[i][j]) {
				case Config.EMPTY:
					c = ' ';
					break;
				case Config.UNINFECTED:
					c = '.';
					break;
				case Config.RECOVERED:
					c = '*';
					break;
				default:
					c = '@';
					break;
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Gets the current number of uninfected individuals, based on the values
	 * that were passed into {@code showWorld}.
	 * 
	 * <p><b>Students' code should not call this method. It is for testing
	 * purposes only.</b>
	 */
	public int getUninfectedCount() {
		return current == null ? 0 : current.numUninfected;
	}
	
	/**
	 * Gets the current number of infected individuals, based on the values that
	 * were passed into {@code showWorld}.
	 * 
	 * <p><b>Students' code should not call this method. It is for testing
	 * purposes only.</b>
	 */
	public int getInfectedCount() {
		return current == null ? 0 : current.numInfected;
	}
	
	/**
	 * Gets the current number of recovered individuals, based on the values
	 * that were passed into {@code showWorld}.
	 * 
	 * <p><b>Students' code should not call this method. It is for testing
	 * purposes only.</b>
	 */
	public int getRecoveredCount() {
		return current == null ? 0 : current.numRecovered;
	}
	
	/**
	 * Gets the number of simulation steps executed so far, based on the values
	 * that were passed into {@code showWorld}.
	 * 
	 * <p><b>Students' code should not call this method. It is for testing
	 * purposes only.</b>
	 */
	public int getStepCount() {
		return current == null ? 0 : current.stepsTaken;
	}
	
	/** JPanel for painting a World on the screen. */
	private class WorldPane extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public WorldPane() {
			setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		}
		
		@Override
		public void paint(Graphics graphics) {
			int[][] world = current.world;
			Graphics2D g = (Graphics2D) graphics;
			final Insets insets = getInsets();
			final int width = getWidth();
			final int height = getHeight();
			
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			
			if (world == null) {
				// Leave a blank screen.

			} else {
				// Paint each tile in the proper location.
				int size = world.length;
				double tileWidth = Math.min(
						width - insets.left - insets.right,
						height - insets.top - insets.bottom) / (double) size;
				
				// First paint blue and pink individuals (and gray walls, for
				// the extra credit portion of the assignment) at a normal size.
				for (int row = 0; row < size; row++) {
					for (int col = 0; col < size; col++) {
						int tile = world[row][col];
						if (tile == Config.EMPTY) {
							// Paint nothing.
						} else if (tile == Config.WALL) {
							// Paint a gray square.
							g.setColor(Color.GRAY);
							g.fill(new Rectangle2D.Double(
									col * tileWidth + insets.left,
									row * tileWidth + insets.top,
									tileWidth, tileWidth));
						} else {
							// Choose a color based on the individual's status.
							if (tile == Config.UNINFECTED) {
								g.setColor(Color.CYAN);
							} else if (tile == Config.RECOVERED) {
								g.setColor(Color.PINK);
							} else { // Infected.
								continue;
							}
							
							// Paint a circle to represent the individual.
							g.fill(new Ellipse2D.Double(
									(col + 0.1) * tileWidth + insets.left,
									(row + 0.1) * tileWidth + insets.top,
									tileWidth * 0.8, tileWidth * 0.8));
						}
					}
				}
				
				// Now paint red individuals at a larger size.
				for (int row = 0; row < size; row++) {
					for (int col = 0; col < size; col++) {
						int tile = world[row][col];
						if (tile == Config.EMPTY) {
							// Paint nothing.
						} else {
							// Choose a color based on the individual's status.
							if (tile == Config.UNINFECTED) {
								continue;
							} else if (tile == Config.RECOVERED) {
								continue;
							} else if (tile == Config.WALL){
								continue;
							} else { // Infected.
								g.setColor(Color.RED);
							}
							
							// Paint a circle to represent the individual.
							g.fill(new Ellipse2D.Double(
									(col - 0.3) * tileWidth + insets.left,
									(row - 0.3) * tileWidth + insets.top,
									tileWidth * 1.6, tileWidth * 1.6));
						}
					}
				}
			}
		}
		
	}
	
	private static class Snapshot {
		public final int[][] world;
		public final int stepsTaken;
		public final int numUninfected;
		public final int numInfected;
		public final int numRecovered;
		
		public Snapshot(
				int[][] world,
				int stepsTaken,
				int numUninfected,
				int numInfected,
				int numRecovered) {
			this.stepsTaken = stepsTaken;
			this.numUninfected = numUninfected;
			this.numInfected = numInfected;
			this.numRecovered = numRecovered;
			this.world = copy(world);
		}
		
		/** Creates a copy of a world. */
		private static int[][] copy(int[][] world) {
			int[][] copy = new int[world.length][];
			for (int i = 0; i < world.length; i++) {
				copy[i] = Arrays.copyOf(world[i], world[i].length);
			}
			return copy;
		}
	}
	
}