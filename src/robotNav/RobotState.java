package robotNav;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RobotState implements Comparable<RobotState> {
	public Map map;
	public RobotState parent;
	public ArrayList<RobotState> children;
	public int cost;
	public int heuristicValue;
	private int evaluationFunction;
	public Direction pathFromParent;
	// For Manipulating Cost Set Up
	private static boolean defaultCost;

	// For Creating Child states
	public RobotState(RobotState parent, Direction fromParent, Map map) {
		this.parent = parent;
		pathFromParent = fromParent;
		this.map = map;
		cost = defaultCost ? parent.cost + 1 : parent.cost;
		evaluationFunction = 0;
		heuristicValue = 0;
	}

	// For initial state
	public RobotState(Map map, boolean useDefaultCost) {
		parent = null;
		pathFromParent = null;
		cost = 0;
		this.map = map;
		evaluationFunction = 0;
		heuristicValue = 0;
		defaultCost = useDefaultCost;
	}

	public int getEvaluationFunction() {
		return evaluationFunction;
	}

	public void setEvaluationFunction(int value) {
		evaluationFunction = value;
	}

	/*
	 * Find Location of robot
	 */
	public int[] findRobotCell() throws InvalidMapException {
		// Go through map array looking for Robot
		for (int i = 0; i < map.map.length; i++) {
			for (int j = 0; j < map.map[i].length; j++) {
				if (map.map[i][j] == 'X') {
					int[] result = { i, j };
					return result;
				}
			}
		}
		// Robot Cell Not Found
		throw new InvalidMapException(this);
	}

	/*
	 * Get the possible actions from current location
	 */
	public Direction[] getPossibleActions() {
		// Collection as list as can be dynamic with sizing
		List<Direction> result = new ArrayList<Direction>();
		// Initialize array
		int[] robotLocation = new int[2];

		try {
			robotLocation = findRobotCell();
		} catch (InvalidMapException e) {

			System.out.println("Could Not Find robot when getting Possible Actions! Aborting...");
			System.exit(1);
		}
		// Check if can move up on map
		if (robotLocation[1] > 0) {
			// check for wall
			if (map.map[robotLocation[0]][robotLocation[1] - 1] != 'W')
				result.add(Direction.Up); // Add Direction
		}

		// check if can move left on map
		if (robotLocation[0] > 0) {
			if (map.map[robotLocation[0] - 1][robotLocation[1]] != 'W')
				result.add(Direction.Left);
		}

		// check if can move down
		if (robotLocation[1] < map.length - 1) {
			if (map.map[robotLocation[0]][robotLocation[1] + 1] != 'W')
				result.add(Direction.Down);
		}

		// check if can move right
		if (robotLocation[0] < map.width - 1) {
			if (map.map[robotLocation[0] + 1][robotLocation[1]] != 'W')
				result.add(Direction.Right);
		}

		// To change type from List to Array
		Direction[] possibleDirections = new Direction[result.size()];
		possibleDirections = result.toArray(possibleDirections);

		return possibleDirections;
	}

	public RobotState move(Direction direction) throws CantMoveThatWayException {
		RobotState result = new RobotState(this, direction,
				new Map(cloneArray(this.map.map), this.map.length, this.map.width, this.map.goalStateCoordinates));

		// find the robots location
		int[] robotLocation = new int[2];
		try {
			robotLocation = findRobotCell();
		} catch (InvalidMapException e) {
			System.out.println("There was an error in processing! Aborting...");
			System.exit(1);
		}
		try {
			// If moving up change location of robot
			if (direction == Direction.Up) {
				result.map.map[robotLocation[0]][robotLocation[1]] = 'O';
				result.map.map[robotLocation[0]][robotLocation[1] - 1] = 'X';
				// Change costing to custom value
				if (!defaultCost) {
					result.cost += 4;
				}
				// If moving down change location of robot
			} else if (direction == Direction.Down) {
				result.map.map[robotLocation[0]][robotLocation[1]] = 'O';
				result.map.map[robotLocation[0]][robotLocation[1] + 1] = 'X';
				// Change costing to custom value
				if (!defaultCost) {
					result.cost += 1;
				}
				// If moving left change location of robot
			} else if (direction == Direction.Left) {
				result.map.map[robotLocation[0]][robotLocation[1]] = 'O';
				result.map.map[robotLocation[0] - 1][robotLocation[1]] = 'X';
				// Change costing to custom value
				if (!defaultCost) {
					result.cost += 2;
				}
				// If moving right change location of robot
			} else {
				result.map.map[robotLocation[0]][robotLocation[1]] = 'O';
				result.map.map[robotLocation[0] + 1][robotLocation[1]] = 'X';
				// Change costing to custom value
				if (!defaultCost) {
					result.cost += 2;
				}
			}
			return result;
		} catch (IndexOutOfBoundsException ex) {
			// If cannot move, through exception
			throw new CantMoveThatWayException(this, direction);
		}
	}

	@Override
	public boolean equals(Object object) {
		RobotState aState = (RobotState) object;
		// State is same if robot cell is in the same spot
		try {
			// Compare the two Robot Locations
			int[] robotOne = aState.findRobotCell();
			int[] robotTwo = this.findRobotCell();
			return robotOne[0] == robotTwo[0] && robotOne[1] == robotTwo[1];
		} catch (InvalidMapException e) {
			System.out.println("Error");
			System.exit(1);
		}
		return false;
	}

	// Checks equal by comparing coordinates with robotLocaion
	public boolean equalsRobotLocation(int[] coordinates) {
		try {
			// State is same if robot cell is in the same spot
			return Arrays.equals(this.findRobotCell(), coordinates);
		} catch (InvalidMapException e) {
			System.out.println("Error, aborting");
			System.exit(1);
		}
		// unreachable code
		return false;
	}

	// this is to allow the TreeSet to sort it.
	public int compareTo(RobotState state) {
		return evaluationFunction - state.getEvaluationFunction();
	}

	/*
	 * Method which explores that current state and returns the children states
	 */
	public ArrayList<RobotState> explore() {
		// populate children
		Direction[] possibleMoves = getPossibleActions();
		children = new ArrayList<RobotState>();
		for (int i = 0; i < possibleMoves.length; i++) {
			try {
				children.add(move(possibleMoves[i]));
			} catch (CantMoveThatWayException e) {
				System.out.println("There was an error in processing! Aborting...");
				System.exit(1);
			}
		}
		return children;
	}

	/*
	 * Gets the current path to state
	 */
	public Direction[] getPathToState() {
		Direction result[];

		if (parent == null) {
			result = new Direction[0];
			return result;
		} else {
			Direction[] pathToParent = parent.getPathToState();
			result = new Direction[pathToParent.length + 1];
			for (int i = 0; i < pathToParent.length; i++) {
				result[i] = pathToParent[i];
			}
			result[result.length - 1] = this.pathFromParent;
			return result;
		}
	}

	public LinkedList<RobotState> getNodesToState() {
		LinkedList<RobotState> result = new LinkedList<RobotState>();
		RobotState parentState = parent;
		while (parentState != null) {
			result.add(parentState);
			parentState = parentState.parent;
		}
		return result;
	}

	private char[][] cloneArray(char[][] cloneMe) {
		char[][] result = new char[cloneMe.length][cloneMe[0].length];
		for (int i = 0; i < cloneMe.length; i++) {
			for (int j = 0; j < cloneMe[i].length; j++) {
				result[i][j] = cloneMe[i][j];
			}
		}
		return result;
	}
}
