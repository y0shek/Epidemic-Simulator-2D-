///////////////////////////////////////////////////////////////////////////////
// Title:            Epidemic
// Files:            Epidemic.java
// Semester:         CS302 Fall 2012
//
// Author:           Andrew Caris
// CS Login:         Andrew
// Lecturer's Name:  Deppeller
// Lab Section:      327
//
//                   PAIR PROGRAMMERS COMPLETE THIS SECTION
// Pair Partner:     Josh Kellerman
// CS Login:         kellerma
// Lecturer's Name:  Deppeller
// Lab Section:      327
//
// Credits:
//////////////////////////// 80 columns wide //////////////////////////////////

/**
 * Main class for simulating an epidemic. The simulation consists of a "world"
 * (a square matrix of cells, or ints). Each cell can be empty or contain an
 * individual. The state of a cell is denoted by its integer value in the array.
 * The value {@code Config.EMPTY} (which is 0) indicates an empty cell.
 * Each individual can be in one of the following states:
 * <ul>
 *   <li><b>Uninfected</b>. This individual has never been infected with the
 *     disease. This is indicated by the value {@code Config.UNINFECTED},
 *     which is -1.
 *   <li><b>Recovered</b>. This individual was infected with the disease but is
 *     no longer infected. This is indicated by the value
 *     {@code Config.RECOVERED}, which is 1. Once recovered, an individual can
 *     never again become infected.
 *   <li><b>Infected</b>. This individual currently has the disease. This is
 *     indicated by an integer value greater than 1. The higher this value,
 *     the longer the period will be before this individual switches to the
 *     RECOVERED state. If the value is less than or equal to
 *     {@code Config.CONTAGIOUS}, the individual is contagious and can
 *     spread their disease to others.
 * </ul>
 * 
 * <p><b>Remember to use the constants from the {@code Config} class
 * directly. DO NOT hard-code constant values from the Config class.</b> You
 * will lose points if you do this.
 * 
 * @author Josh Kellerman and Andrew Caris
 *
 */
public class Epidemic {

	/**
	 * Application entry point. Runs an epidemic simulation to completion,
	 * displaying the results after each step using a {@code WorldViewer}
	 * object. Each step in the simulation consists of the following sub-steps:
	 * <ol>
	 *   <li>Move individuals around in the world.
	 *   <li>Decrement infected individuals to represent the passage of time.
	 *   <li>Spread disease from contagious to uninfected individuals.
	 * </ol>
	 * 
	 * <p>At the end of each simulation step (that is, after each spread-disease
	 * substep), you should display the current simulation state by calling
	 * {@code Config.VIEWER.showWorld()} and passing the correct arguments.
	 * This will update the picture displayed on the window.
	 * 
	 * <p>The simulation is complete once there are no more infected
	 * individuals.
	 * 
	 * @param args Command-line arguments (ignored).
	 *
	 * Bugs: none known
	 */
	public static void main(String[] args) {

	//Declaring stepsTaken variable to keep track of count
		int stepsTaken = 0;

	//Declares and Constructs the World
		int [][] world = new int [Config.WORLD_SIZE][Config.WORLD_SIZE];
		
	//Initializes World to Empty spaces
	fillEmptyWorld(world);

	//Calls the populateWorld method to add individuals to the world
	populateWorld(world);		
		
	/**
	 * Logic for when the Epidemic Simulation Ends
	 * simNotFinished method handles whether boolean 
	 * stillInfected is true or false
	 */
	
	/**
	 * Start of the Epidemic Loop
	 * Sentinel value depends on simNotFinished method
	 */
	while (simNotFinished(world)){

	// Calls the move method to move individuals around in the world.
	move(world);

	// Calls the decrement method to advance the state of all infected
	// individuals in the world.
	decrement(world);
			
	// Calls the spreadDisease method to spread disease from contagious
	// to uninfected individuals.
	spreadDisease(world);
	
	/**
	 * Updates important variables in Config.VIEWER 
	 * needed for Config.VIEWER.showWorld method
	 */
	
	stepsTaken += 1;
	boolean pause = !simNotFinished(world); //remove '!' to step through.
	int numUninfected = countNumUninfected(world);
	int numInfected = countNumInfected(world);
	int numRecovered = countNumRecovered(world);
			
	/* Updates the viewer by calling Config.VIEWER.showWorld() and
	 * passing the appropriate information as arguments.
	 * Config.VIEWER.showWorld(world, stepsTaken, numUninfected, numInfected, 
	 * numRecovered, pause);
	 **/

	Config.VIEWER.showWorld(world, stepsTaken, numUninfected, numInfected, 
			numRecovered, !pause);
		
	} //end stillInfected while loop
} //end main
	
	
	/**
	 * @code populateWorld
	 * 
	 * Populates the given world with uninfected and infected individuals, as
	 * specified by {@code Config.INITIAL_UNINFECTED_COUNT} and
	 * {@code Config.INITIAL_INFECTED_COUNT}.
	 * 
	 * @param world World array to fill (8100 cells total).
	 */
	public static void populateWorld(int[][] world) {


		//Populates the world with uninfected people
		
		for (int i = 0; i < Config.INITIAL_UNINFECTED_COUNT; i++){
			
			Epidemic.addIndividual(world, Config.UNINFECTED);
		}

		//Populates the world with 5 infected people	
		
		for (int i = 0; i < Config.INITIAL_INFECTED_COUNT; i++){
			
			Epidemic.addIndividual(world, Config.INFECTED);
		}
	} //end populateWorld
	
	/**
	 * @code addIndividual
	 * 
	 * Adds an individual to the simulation world. This should be done by
	 * randomly selecting an EMPTY cell in the world. That cell's value should
	 * then be changed to the specified {@code newIndividual} value.
	 * 
	 * @param world World array where individual is added to.
	 * @param newIndividual The new value to put into the randomly selected
	 *        cell.
	 */
	public static void addIndividual(int[][] world, int newIndividual) {

		/* Finds a random 2D array element based on world size. If element
		 * is empty, assigns UNINFECTED value to element. If element is already
		 * UNINFECTED, repeat getting random numbers until an empty element
		 * is found, then assign UNINFECTED value to element.
		 **/
		
		int y = Config.RANDOM.nextInt(Config.WORLD_SIZE );
		int x = Config.RANDOM.nextInt(Config.WORLD_SIZE );

		if (world[y][x] == Config.EMPTY){
			world[y][x] = newIndividual;
		}else{
	
	//if cell is already taken, randomly choose a new one 
	//until it finds an empty one
			while (world[y][x] != Config.EMPTY){
				y = Config.RANDOM.nextInt(Config.WORLD_SIZE );
				x = Config.RANDOM.nextInt(Config.WORLD_SIZE );
			}
			world[y][x] = newIndividual;			
		}
	}//end addIndividual

	/**
	 * @code move
	 * 
	 * Randomly moves individuals around in the world. This should be done by
	 * randomly selecting pairs of adjacent cells in the array. Each such pair
	 * should then have their values swapped. The number of pairs swapped should
	 * equal half the number of cells in the world.
	 * 
	 * @param world The world array.
	 * @return returns void but moves individuals around defined by 
	 * @code swapRandomIndividuals
	 */
	
	public static void move(int[][] world) {
		for (int i = 0; i < (int)((Config.WORLD_SIZE*Config.WORLD_SIZE)/2); i++){

			/* Repeatedly calling the swapRandomIndividuals method. 
			 * The number of times we is equal to 
			 * half (rounding down) of the total number
			 * of cells in the world. (cast double to int)
			 **/

			swapRandomIndividuals(world);
		}
	}//end move
	
	/**
	 *@code swapRandomIndividuals 
	 *
	 * Randomly swaps two adjacent individuals when a random cell is 
	 * selected and the two cells adjacent are picked randomly. The cells
	 * to be swapped are defined by the closest neighbor to the individual
	 * either diagonal, left, right, up, and down by only 1 cell.
	 * 
	 * @param world Within the world array segment into an array consisting of
	 * 9 cells, one of them being the first randomly selected cell by the 
	 * @code move method
	 * @return returns void but interchanges cells
	 */
	
	public static void swapRandomIndividuals(int[][] world) {
		
	// Pick a random cell in the world.
	int y = Config.RANDOM.nextInt(Config.WORLD_SIZE);
	int x = Config.RANDOM.nextInt(Config.WORLD_SIZE);
		
	// Randomly pick one of the eight cells adjacent to this one.
	int block_y;
	int block_x;
	
	do {
		block_y = Config.RANDOM.nextInt(2)-1;
		block_x = Config.RANDOM.nextInt(2)-1;
	} while (block_y == 0 && block_x == 0);
		
	// Swap the values contained by the two selected cells.
	int swapIndividualY = wrap(Config.WORLD_SIZE, y + block_y);
	int swapIndividualX = wrap(Config.WORLD_SIZE, x + block_x);
	
	int temp = world [swapIndividualY][swapIndividualX];
	world [swapIndividualY][swapIndividualX] = world [y][x];
	world [y][x] = temp;
	
	}//end swapRandomIndividuals
	
	/**
	 * @code decrement
	 * 
	 * Advances the disease state of all infected individuals. If an individual
	 * is infected, his or her state should be decremented by 1 to show the
	 * advancement of the disease towards the RECOVERED state. Individuals who
	 * are not infected should not be affected by the decrement method.
	 * 
	 * @param world The world array.
	 */
	public static void decrement(int[][] world) {
		for (int i = 0; i < Config.WORLD_SIZE; i++) {
			for (int j = 0; j < Config.WORLD_SIZE; j++){
				if (world[i][j] != Config.UNINFECTED && world[i][j] != 
						Config.EMPTY && world[i][j] != Config.RECOVERED)
				{world[i][j] -= 1;}
			}
		}
	}//end decrement

	/**
	 * @code spreadDisease
	 * 
	 * Spreads disease from contagious individuals to adjacent UNINFECTED
	 * individuals. For each pair of adjacent individuals, if one of them is
	 * contagious and the other is UNINFECTED, then there is a fixed chance
	 * ({@code Config.INFECTION_PROBABILITY}) that the UNINFECTED individual
	 * will become contagious.
	 * 
	 * @param world The world array.
	 */
	public static void spreadDisease(int[][] world) {
		
		// Loop over all the cells in the world.

		for (int i = 0; i < Config.WORLD_SIZE; i++) {
		for (int j = 0; j < Config.WORLD_SIZE; j++){

		/* If the cell does contain a contagious individual, loop over
		 * all eight cells that are adjacent to this contagious
		 * individual. In this loop, you should check each adjacent cell
		 * to see if it is UNINFECTED. If it is, that cell should have a
		 * chance (Config.INFECTION_PROBABILITY) of becoming
		 * infected right now. To mark the cell as infected, set its
 		   value to Config.INFECTED.
 		 **/
			
	   if (world[i][j] <= Config.CONTAGIOUS && world[i][j] > Config.RECOVERED){ 	
	       for (int o = -1; o <= 1; o++) {
	           for (int k = -1; k <= 1; k++){
	    	       if (world[wrap(Config.WORLD_SIZE, i+o)]
	    	    		   [wrap(Config.WORLD_SIZE,j+k)] == Config.UNINFECTED) {
	    				
	    		double probability = Config.RANDOM.nextDouble();
	    		
	    		if (probability <= Config.INFECTION_PROBABILITY){
	    			world[wrap(Config.WORLD_SIZE, i+o)]
	    			[wrap(Config.WORLD_SIZE, j+k)] = Config.INFECTED;}
	    		
	    	       } //end probability if
	           } // end fourth (k) for
	       }  //end third (o) for
	   	} // end world if
		} // end second (j) for
		} // end first (i) for
	} // end spreadDisease
	
	/**
	 * @code wrap
	 * 
	 * Wraps a world coordinate around. Using this on all coordinates means that
	 * our world has no boundaries--individuals and infections simply wrap
	 * around from top to bottom and from left to right. This is sometimes
	 * called a "toroidal world". See
	 * <a href="http://en.wikipedia.org/wiki/Torus">Wikipedia</a>.
	 * 
	 * <p>You should call this method on any index <b>before</b> you use it to
	 * index into the world array. That way, even cells on the edges of the
	 * array will be considered to have eight adjacent cells. For example,
	 * consider the cell marked "X" below:
	 * <pre>
	 *     A * A A
	 *     A * A X
	 *     A * A A
	 *     * * * *</pre>
	 * The cell "X" is adjacent to the eight cells marked "A" because the world
	 * wraps around at the edges.
	 * 
	 * @param size Size of the world.
	 * @param coord Coordinate to be wrapped.
	 * @return The wrapped version of the coordinate.
	 */
	public static int wrap(int size, int coord) {

	int finalCoord;
	if (coord >= size)
	{
	finalCoord = coord % size;
	}
	else if (coord % size == 0 && coord <= -1){
	finalCoord = coord % size;	
	}
	else if (coord <= -1){
	finalCoord = coord % size + size;
	}
	else finalCoord = coord;
		return finalCoord;
	}//end wrap
	
	/**
	 * @code countNumUninfected
	 * 
	 * Finds the number of Uninfected in the world map.
	 * @param world The world map.
	 * @return The number of Uninfected at any given point.
	 */
	public static int countNumUninfected(int [][] world){
	int numUninfected = 0;
	for (int i=0; i < Config.WORLD_SIZE; i++){
		for (int j=0; j < Config.WORLD_SIZE; j++){
			if (world[i][j] == Config.UNINFECTED)
				numUninfected++;
		}
	}
	
		return numUninfected;
	}//end countNumUninfected
	
	/**
	 * @code countNumInfected
	 * 
	 * Finds the number of Infected in the world map.
	 * @param world The world map.
	 * @return The number of Infected at any given point.
	 */
	public static int countNumInfected(int [][] world){
		int numInfected = 0;
		for (int i=0; i < Config.WORLD_SIZE; i++){
			for (int j=0; j < Config.WORLD_SIZE; j++){
				if (world[i][j] <= Config.INFECTED && world[i][j] 
						> Config.RECOVERED)
					numInfected++;
			}
		}	
		return numInfected;
	}//end countNumInfected
	
	/**
	 * @code countNumRecovered
	 * 
	 * Finds the number of Recovered in the world map.
	 * @param world The world map.
	 * @return The number of Recovered at any given point.
	 */
	public static int countNumRecovered(int [][] world){
		int numRecovered = 0;
		for (int i=0; i < Config.WORLD_SIZE; i++){
			for (int j=0; j < Config.WORLD_SIZE; j++){
				if (world[i][j] == Config.RECOVERED)
					numRecovered++;
			}
		}	
		return numRecovered;
	}//end of countNumRecovered
	
	/**
	 * @code simNotFinished
	 * 
	 * Finds when the simulation is finished.
	 * Step through world array to check for Infected Individuals
	 * @param world The world map.
	 * @return boolean value of true or false.
	 */
	public static boolean simNotFinished(int [][] world){
		boolean simNotFinished = false;
		for (int i=0; i < Config.WORLD_SIZE; i++){
			for (int j=0; j < Config.WORLD_SIZE; j++){
				if (world[i][j] > Config.RECOVERED)
					simNotFinished = true;
			}
		}	
		return simNotFinished;
	}// end simNotFinished
	
	/**
	 * @code fillEmptyWorld
	 * 
	 * Fills the world with empty spaces
	 * @param world
	 */
	public static void fillEmptyWorld(int [][] world){
		for (int i = 0; i < world.length; i++){
			for (int j = 0; j < world[0].length; j++){
				world[i][j] = Config.EMPTY;
			}
		}
	} // end fillEmptyWorld
} // end class