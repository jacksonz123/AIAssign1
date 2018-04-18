package robotNav;

import java.util.*;

public abstract class SearchMethod {
	// Used for identifying method from command line
	public String code;
	// Actual name of method
	public String longName;

	/*
	 * Method for solving the puzzle
	 */
	public abstract Direction[] Solve(Map navMap, boolean defualtCost);

	// Frontier for storing nodes
	public LinkedList<RobotState> frontier;
	// keep count of expanded nodes
	public int nodesSearched;

	// add state to frontier
	abstract public boolean addToFrontier(RobotState state);

	// get node from frontier
	abstract protected RobotState popFrontier();

	// reset frontier and nodes Searched
	public void reset() {
		this.frontier.clear();
		nodesSearched = 0;
	}

	/*
	 * Get Manhattan distance from robot to goal Used as admissible heuristic
	 */
	int HeuristicValue(RobotState state, int[] goalState) {
		// Initialize array to default
		int[] robotLocation = { 0, 0 };
		try {
			// find robot's location
			robotLocation = state.findRobotCell();
		} catch (InvalidMapException e) {
			System.out.println("Error with heuristic evaluation");
		}
		// return Manhattan distance
		int heuristic = Math.abs(robotLocation[0] - goalState[0]) + Math.abs(robotLocation[1] - goalState[1]);

		return heuristic;
	}
}
