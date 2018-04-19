package robotNav;

import java.util.ArrayList;
import java.util.LinkedList;

public class BFSStrategy extends SearchMethod {

	public BFSStrategy() {
		code = "BFS";
		longName = "Breadth-First Search";
		frontier = new LinkedList<RobotState>();
		nodesSearched = 0;
	}

	protected RobotState popFrontier() {
		// retrieve first element from frontier
		RobotState thisState = frontier.pop();
		// increment number of searched nodes
		nodesSearched++;

		return thisState;
	}

	public boolean addToFrontier(RobotState state) {
		// Checks that node has not been searched on current path and is not already on
		// Frontier
		if (state.getNodesToState().contains(state) || frontier.contains(state)) {
			// State has already been searched, discard
			return false;
		} else {
			// Add last to ensure FIFO
			frontier.addLast(state);
			return true;
		}
	}

	public Direction[] solve(Map navMap, boolean defualtCost) {
		// Creates the initial state and adds to Frontier
		addToFrontier(new RobotState(navMap, defualtCost));

		// Keep searching until all states are exhausted
		while (frontier.size() > 0) {
			// Get Node from Frontier
			RobotState thisState = popFrontier();
			// Check if goal state
			if (thisState.equalsRobotLocation(navMap.goalStateCoordinates)) {
				// return path
				return thisState.getPathToState();
			} else {
				// expand current node
				ArrayList<RobotState> newStates = thisState.explore();
				// For each child, add to Frontier
				for (int i = 0; i < newStates.size(); i++) {
					addToFrontier(newStates.get(i));
				}
			}
		}
		// if no solution is found
		return null;
	}
}
