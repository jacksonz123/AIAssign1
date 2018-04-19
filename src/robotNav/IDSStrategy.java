package robotNav;

import java.util.ArrayList;
import java.util.LinkedList;

public class IDSStrategy extends SearchMethod {

	public IDSStrategy() {
		code = "IDS";
		longName = "Depth-First Search";
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
		if (state.GetNodesToState().contains(state) || frontier.contains(state)) {
			// State has already been searched, discard
			return false;
		} else {
			// Add First to Ensure LIFO
			frontier.addFirst(state);
			return true;
		}
	}
	
	public Direction[] depthLimitedSearch(RobotState initialState, Map navMap, int depth) {
		// clear the frontier
		frontier.clear();
		// Creates the initial state and adds to Frontier
		addToFrontier(initialState);

		// Keep searching until all states are exhausted
		while (frontier.size() > 0) {
			// Get Node from Frontier
			RobotState thisState = popFrontier();
			// Check if goal state
			if (thisState.equalsRobotLocation(navMap.goalStateCoordinates)) {
				// return path
				return thisState.GetPathToState();
			}
			else {
				// expand current node
				ArrayList<RobotState> newStates = thisState.explore();
				// For each child, add to Frontier
				// Must do in reverse order to ensure LIFO
				for (int i = newStates.size() - 1; i > -1; i--) {
					// Only add to frontier if under depth limit
					if (newStates.get(i).GetPathToState().length < depth) {
						addToFrontier(newStates.get(i));
					}
				}
			}
		}
		// if no solution is found
		return null;
	}

	public Direction[] Solve(Map navMap, boolean defaultCost) {
		RobotState initialState = new RobotState(navMap, defaultCost);
		for (int depth = 0; depth < Integer.MAX_VALUE; depth++) {
			Direction[] result = depthLimitedSearch(initialState, navMap, depth);
			if (result != null) {
				return result;
			}
		}
		// if no solution is found
		return null;
	}
}
