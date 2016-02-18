import java.util.Random;

/**
 * Defines constant simulation configuration values. You should refer to these
 * constants in your program. Do not put these magic numbers directly into your
 * program's code. Instead, access them like this: {@code Config.RANDOM},
 * {@code Config.INFECTED}, etc.
 * 
 * <p><b>DO NOT ADD, REMOVE, OR RENAME VARIABLES IN THIS FILE.</b> You may
 * change values in order to test your program for different configuration
 * values.</p>
 * 
 * @author pollen
 */
public class Config {
	
	private Config() {
		// This private constructor prevents anyone from ever creating an
		// instance of this class.
	}
	
	/**
	 * Random number generator to use for simulation. You may also create your
	 * own Random objects; we just provide this one for your convenience. In
	 * your Epidemic class, you may access it by typing {@code
	 * Config.RANDOM}.
	 */
	public static final Random RANDOM = new Random();
	
	/**
	 * WorldViewer object to use for showing simulation results to the user in
	 * a GUI (Graphical User Interface) window. Do not create additional
	 * WorldViewer objects, as doing so will break our grading system. Use this
	 * WorldViewer object for displaying all stages of the simulation.
	 */
	public static final WorldViewer VIEWER = new WorldViewer();

	
	
	/** Value to represent an empty tile in the world. */
	public static final int EMPTY = 0;
	
	/** Value to represent an uninfected individual in the world. */
	public static final int UNINFECTED = -1;
	
	/**
	 * Value representing a wall in the world. <b>This is only used for the
	 * extra credit portion of the assignment.</b> If you aren't doing extra
	 * credit, then you should ignore this constant.
	 */
	public static final int WALL = -2;
	
	/**
	 * Value to represent an individual who has recovered from being infected.
	 */
	public static final int RECOVERED = 1;
	
	/** Value to represent an individual who has just become infected. */
	public static final int INFECTED = 9;

	/**
	 * Value to represent an individual who is infected and contagious. Any
	 * value less than or equal to CONTAGIOUS and greater than RECOVERED is
	 * considered a contagious individual.
	 */
	public static final int CONTAGIOUS = 7;
	
	
	
	/**
	 * Probability that infection is passed between any two adjacent
	 * individuals.
	 */
	public static final double INFECTION_PROBABILITY = 0.06;
	
	/**
	 * Probability that an infected individual dies during any simulation step.
	 * <b>This is only used for the extra credit portion of the assignment.</b>
	 * If you aren't doing extra credit, then you should ignore this constant.
	 */
	public static final double DEATH_PROBABILITY = 0.05;
	
	/** Width and height of the world, in cells. */
	public static final int WORLD_SIZE = 90;
	
	/** Initial count of uninfected individuals in the simulation. */
	public static final int INITIAL_UNINFECTED_COUNT =
			(int)(WORLD_SIZE * WORLD_SIZE * 0.8);
	
	/** Initial count of infected individuals in the simulation. */
	public static final int INITIAL_INFECTED_COUNT = 5;
	
	
	
	/**
	 * Height of walls to create in the world. <b>This is only used for the
	 * extra credit portion of the assignment.</b> If you aren't doing extra
	 * credit, then you should ignore this constant.
	 */
	public static final int WALL_HEIGHT = (int)(WORLD_SIZE * 0.80);
	
	/**
	 * Horizontal spacing of walls to create in the world. <b>This is only used
	 * for the extra credit portion of the assignment.</b> If you aren't doing
	 * extra credit, then you should ignore this constant.
	 */
	public static final int WALL_SPACING = 8;
	
}
