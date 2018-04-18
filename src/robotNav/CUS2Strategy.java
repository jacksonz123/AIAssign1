package robotNav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class CUS2Strategy extends SearchMethod {

	public CUS2Strategy() {
		code = "C2";
		longName = "Custom Informed Search";
		frontier = new LinkedList<RobotState>();
		nodesSearched = 0;
	}

	public boolean addToFrontier(RobotState state) {
		//No Repeated State Check
		frontier.add(state);
		return true;
	}

	public Direction[] Solve(Map navMap, boolean aDefualtCost) {
		addToFrontier(new RobotState(navMap, aDefualtCost));
		while (frontier.size() > 0) {
			RobotState thisState = popFrontier();

			if (thisState.equalsRobotLocation(navMap.goalStateCoordinates)) {
				return thisState.GetPathToState();
			}

			ArrayList<RobotState> newStates = thisState.explore();

			for (int i = 0; i < newStates.size(); i++) {
				RobotState newChild = newStates.get(i);
				if (addToFrontier(newChild)) {
					newChild.heuristicValue = HeuristicValue(newStates.get(i), navMap.goalStateCoordinates);
					newChild.setEvaluationFunction(newChild.heuristicValue);
				}

			}


			Collections.sort(frontier, new StateComparator());
		}

		return null;
	}

	protected RobotState popFrontier() {
		RobotState lState = frontier.pollFirst();


		return lState;
	}
}
