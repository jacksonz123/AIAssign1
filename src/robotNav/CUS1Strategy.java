package robotNav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class CUS1Strategy extends SearchMethod {

	public CUS1Strategy() {
		code = "C1";
		longName = "Custom Uninformed Search";
		Frontier = new LinkedList<RobotState>();
		Searched = new LinkedList<RobotState>();
	}

	protected RobotState popFrontier() {
		// remove an item from the fringe to be searched
		RobotState thisState = Frontier.pop();
		// Add it to the list of searched states, so that it isn't searched again
		Searched.add(thisState);

		return thisState;
	}

	public Direction[] Solve(Map navigationMap, boolean aDefualtCost) {
		// put the start state in the Fringe to get explored.
		addToFrontier(new RobotState(navigationMap, aDefualtCost));
		while (Frontier.size() > 0) {
			// get the next State
			RobotState thisState = popFrontier();

			// is this the goal state?
			if (thisState.equalsRobotLocation(navigationMap.goalStateCoordinates)) {
				return thisState.GetPathToState();
			}

			// not the goal state, explore this node
			ArrayList<RobotState> newStates = thisState.explore();

			for (int i = 0; i < newStates.size(); i++) {
				RobotState newChild = newStates.get(i);
				// if you can add these new states to the fringe
				if (addToFrontier(newChild)) {
					// then, work out it's heuristic value
					newChild.setEvaluationFunction(newChild.Cost);
				}

			}

			// Sort the fringe by it's Heuristic Value. The PuzzleComparator uses each nodes
			// EvaluationFunction
			// to determine a node's value, based on another. The sort method uses this to
			// sort the Fringe.
			Collections.sort(Frontier, new StateComparator());
		}

		// no more nodes and no path found?
		return null;
	}

	public boolean addToFrontier(RobotState aState) {
		// if this state has been found before,
		if (Searched.contains(aState)) {
			// discard it
			return false;
		} else {
			// else put this item on the end of the queue;
			Frontier.addLast(aState);
			return true;
		}
	}

}
