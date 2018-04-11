package robotNav;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RobotState implements Comparable<RobotState> {
	public Map map;
	public RobotState Parent;
	public ArrayList<RobotState> Children;
	public int Cost;
	public int HeuristicValue;
	private int EvaluationFunction;
	public Direction PathFromParent;

	// For Creating Child states
	public RobotState(RobotState aParent, Direction aFromParent, Map aMap) {
		Parent = aParent;
		PathFromParent = aFromParent;
		map = aMap;
		Cost = Parent.Cost + 1;
		EvaluationFunction = 0;
		HeuristicValue = 0;
	}

	// For initial state
	public RobotState(Map aMap) {
		Parent = null;
		PathFromParent = null;
		Cost = 0;
		map = aMap;
		EvaluationFunction = 0;
		HeuristicValue = 0;
	}

	public int getEvaluationFunction() {
		return EvaluationFunction;
	}

	public void setEvaluationFunction(int value) {
		EvaluationFunction = value;
	}

	public int[] findRobotCell() throws InvalidMapException {
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

	public Direction[] getPossibleActions() {
		// Collection as list as can be dynamic with sizing
		List<Direction> result = new ArrayList<Direction>();
		// find where the blank cell is and store the Directions.
		int[] robotLocation = { 0, 0 }; // dummy value to avoid errors.

		try {
			robotLocation = findRobotCell();
		} catch (InvalidMapException e) {

			System.out.println("Could Not Find robot when getting Possible Actions! Aborting...");
			System.exit(1);
		}
		//Check if can move up on map
		if (robotLocation[1] > 0) {
			//check for wall
			if (map.map[robotLocation[0]][robotLocation[1] - 1] != 'W')
				result.add(Direction.Up); //Add Direction
		}
		
		//check if can move left on map
		if (robotLocation[0] > 0) {
			if (map.map[robotLocation[0] - 1][robotLocation[1]] != 'W')
				result.add(Direction.Left);
		}
		
		//check if can move down
		if (robotLocation[1] < map.length - 1) {
			if (map.map[robotLocation[0]][robotLocation[1] + 1] != 'W')
				result.add(Direction.Down);
		}
		
		//check if can move right
		if (robotLocation[0] < map.width - 1) {
			if (map.map[robotLocation[0] + 1][robotLocation[1]] != 'W')
				result.add(Direction.Right);
		}
		
		/*
		if (robotLocation[0] == 0) {
			// Can check only right of the robot
			// Check if there is not a wall there
			if (map.map[robotLocation[0] + 1][robotLocation[1]] != 'W')
				result.add(Direction.Right);
		} else if (robotLocation[0] == (map.width - 1)) {
			// Checking edge of map
			if (map.map[robotLocation[0] - 1][robotLocation[1]] != 'W')
				result.add(Direction.Left);
		} else {
			if (map.map[robotLocation[0] + 1][robotLocation[1]] != 'W')
				result.add(Direction.Right);

			if (map.map[robotLocation[0] - 1][robotLocation[1]] != 'W')
				result.add(Direction.Left);
		}

		if (robotLocation[1] == 0) {
			// the blank cell is already as far up as it will go, check only if it can move
			// down
			if (map.map[robotLocation[0]][robotLocation[1] + 1] != 'W')
				result.add(Direction.Down);
		} else if (robotLocation[1] == (map.length - 1)) {
			if (map.map[robotLocation[0]][robotLocation[1] - 1] != 'W')
				result.add(Direction.Up);
		} else {
			if (map.map[robotLocation[0]][robotLocation[1] - 1] != 'W')
				result.add(Direction.Up);

			if (map.map[robotLocation[0]][robotLocation[1] + 1] != 'W')
				result.add(Direction.Down);
		}
		*/
		
		// To change type from List to Array
		Direction[] possibleDirections = new Direction[result.size()];
		possibleDirections = result.toArray(possibleDirections);
		
		return possibleDirections;
	}

	public RobotState move(Direction aDirection) throws CantMoveThatWayException {
		// first, create the new one (the one to return)
		RobotState result = new RobotState(this, aDirection, new Map(cloneArray(this.map.map), this.map.length, this.map.width, this.map.goalStateCoordinates));

		// find the robots location
		int[] robotLocation = { 0, 0 };
		try {
			robotLocation = findRobotCell();
		} catch (InvalidMapException e) {
			System.out.println("There was an error in processing! Aborting...");
			System.exit(1);
		}
		try {
			// move the blank cell in the new child map.map
			if (aDirection == Direction.Up) {
				result.map.map[robotLocation[0]][robotLocation[1]] = 'O';
				result.map.map[robotLocation[0]][robotLocation[1] - 1] = 'X';
			} else if (aDirection == Direction.Down) {
				result.map.map[robotLocation[0]][robotLocation[1]] = 'O';
				result.map.map[robotLocation[0]][robotLocation[1] + 1] = 'X';
			} else if (aDirection == Direction.Left) {
				result.map.map[robotLocation[0]][robotLocation[1]] = 'O';
				result.map.map[robotLocation[0] - 1][robotLocation[1]] = 'X';
			} else // aDirection == Right;
			{
				result.map.map[robotLocation[0]][robotLocation[1]] = 'O';
				result.map.map[robotLocation[0] + 1][robotLocation[1]] = 'X';
			}
			return result;
		} catch (IndexOutOfBoundsException ex) {
			throw new CantMoveThatWayException(this, aDirection);
		}
	}

	@Override
	public boolean equals(Object aObject) {
		RobotState aState = (RobotState) aObject;
		// State is same if robot cell is in the same spot
		try {
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
	public int compareTo(RobotState aState) {
		return EvaluationFunction - aState.getEvaluationFunction();
	}

	public ArrayList<RobotState> explore() {
		// populate children
		Direction[] possibleMoves = getPossibleActions();
		Children = new ArrayList<RobotState>();
		for (int i = 0; i < possibleMoves.length; i++) {
			try {
				Children.add(move(possibleMoves[i]));
			} catch (CantMoveThatWayException e) {
				System.out.println("There was an error in processing! Aborting...");
				System.exit(1);
			}
		}
		return Children;
	}

	public Direction[] GetPathToState() {
		Direction result[];

		if (Parent == null) // If this is the root node, there is no path!
		{
			result = new Direction[0];
			return result;
		} else // Other wise, path to here is the path to parent
				// plus parent to here
		{
			Direction[] pathToParent = Parent.GetPathToState();
			result = new Direction[pathToParent.length + 1];
			for (int i = 0; i < pathToParent.length; i++) {
				result[i] = pathToParent[i];
			}
			result[result.length - 1] = this.PathFromParent;
			return result;
		}
	}
	
	private char[][] cloneArray(char[][] cloneMe)
	{
		char[][] result = new char[cloneMe.length][cloneMe[0].length];
		for(int i = 0; i < cloneMe.length; i++)
		{
			for(int j = 0; j < cloneMe[i].length; j++)
			{
				result[i][j] = cloneMe[i][j];
			}
		}
		return result;
	}
}
