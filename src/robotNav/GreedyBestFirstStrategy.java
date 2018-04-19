package robotNav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class GreedyBestFirstStrategy extends SearchMethod {

	public GreedyBestFirstStrategy() {
		code = "GBFS";
		longName = "Greedy Best-First Search";
		frontier = new LinkedList<RobotState>();
		nodesSearched = 0;
	}

	protected RobotState popFrontier() {
		// retrieve first element from frontier
		RobotState state = frontier.pollFirst();
		// increment number of searched nodes
		nodesSearched++;

		return state;
	}

	public boolean addToFrontier(RobotState state) {
		// Checks that node has not been searched on current path and is not already on
		// Frontier
		if (state.getNodesToState().contains(state) || frontier.contains(state)) {
			// State has already been searched, discard
			return false;
		} else {
			// Add to Frontier
			frontier.add(state);
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
			}
			// expand current node
			ArrayList<RobotState> newStates = thisState.explore();
			// For each child, add to Frontier
			for (int i = 0; i < newStates.size(); i++) {
				RobotState newChild = newStates.get(i);
				if (addToFrontier(newChild)) {
					// Set heuristic value of node
					newChild.heuristicValue = heuristicValue(newStates.get(i), navMap.goalStateCoordinates);
					// set evaluation function f(n) = h(n)
					newChild.setEvaluationFunction(newChild.heuristicValue);
				}

			}
			// sort according to evaluation function
			Collections.sort(frontier, new StateComparator());
		}
		// if no solution is found
		return null;
	}
}
