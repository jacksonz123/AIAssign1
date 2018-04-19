package robotNav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class IDAStrategy extends SearchMethod {
	private int cutoff;

	/*
	 * Constructor for IDA*
	 */
	public IDAStrategy() {
		code = "IDA";
		longName = "A Star Search";
		frontier = new LinkedList<RobotState>();
		nodesSearched = 0;
		cutoff = 0;
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
		if (state.GetNodesToState().contains(state) || frontier.contains(state)) {
			// State has already been searched, discard
			return false;
		} else {
			// Add to Frontier
			frontier.add(state);
			return true;
		}
	}

	public Direction[] iterativeDeepeningAStar(RobotState initialState, Map navMap) {
		// Creates the initial state and adds to Frontier
		addToFrontier(initialState);
		// new cutoff is max value
		int newCutoff = Integer.MAX_VALUE;
		// Keep searching until all states are exhausted
		while (frontier.size() > 0) {
			// Get Node from Frontier
			RobotState thisState = popFrontier();
			
			// Check if goal state
			if (thisState.equalsRobotLocation(navMap.goalStateCoordinates)) {
				// return path
				return thisState.GetPathToState();
			}
			// expand current node
			ArrayList<RobotState> newStates = thisState.explore();
			// For each child
			for (int i = 0; i < newStates.size(); i++) {
				RobotState newChild = newStates.get(i);
				// Set heuristic value of node
				newChild.heuristicValue = HeuristicValue(newStates.get(i), navMap.goalStateCoordinates);
				// set evaluation function f(n) = h(n) + g(n)
				newChild.setEvaluationFunction(newChild.heuristicValue + newChild.cost);
				if (newChild.getEvaluationFunction() <= cutoff) {
					// If under cutoff, add to frontier
					frontier.add(newChild);
				}
				else if (newChild.getEvaluationFunction() < newCutoff) {
					// update to the lowest value over cutoff
					newCutoff = newChild.getEvaluationFunction();
				}
			}
			// sort according to evaluation function
			Collections.sort(frontier, new StateComparator());
		}
		// set the next cutoff value to the newCutoff value to expand search
		cutoff = newCutoff;
		// All states under cutoff have been explored
		return null;
	}

	public Direction[] Solve(Map navMap, boolean defaultCost) {
		// Creates the initial state
		RobotState initialState = new RobotState(navMap, defaultCost);
		// Set heuristic value of node
		initialState.heuristicValue = HeuristicValue(initialState, navMap.goalStateCoordinates);
		// set evaluation function f(n) = h(n) + g(n)
		initialState.setEvaluationFunction(initialState.heuristicValue + initialState.cost);
		cutoff = initialState.getEvaluationFunction();
		Direction[] result = null;
		while (result == null) {
			// Search through tree
			result = iterativeDeepeningAStar(initialState, navMap);
			// clear frontier
			frontier.clear();
		}
		// If no solution is found
		return result;
	}
}
