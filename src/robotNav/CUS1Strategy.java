package robotNav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class CUS1Strategy extends SearchMethod {

	public CUS1Strategy() {
		code = "UCS";
		longName = "Uniform Cost Search";
		Frontier = new LinkedList<RobotState>();
		Searched = new LinkedList<RobotState>();
	}

	protected RobotState popFrontier() {
		RobotState thisState = Frontier.pop();
		Searched.add(thisState);

		return thisState;
	}

	public Direction[] Solve(Map navigationMap, boolean aDefualtCost) {
		addToFrontier(new RobotState(navigationMap, aDefualtCost));
		while (Frontier.size() > 0) {
			RobotState thisState = popFrontier();

			if (thisState.equalsRobotLocation(navigationMap.goalStateCoordinates)) {
				return thisState.GetPathToState();
			}

			ArrayList<RobotState> newStates = thisState.explore();

			for (int i = 0; i < newStates.size(); i++) {
				RobotState newChild = newStates.get(i);
				if (addToFrontier(newChild)) {
					newChild.setEvaluationFunction(newChild.Cost);
				}

			}

			Collections.sort(Frontier, new StateComparator());
		}

		return null;
	}

	public boolean addToFrontier(RobotState aState) {
		if (aState.GetNodesToState().contains(aState) || Frontier.contains(aState)) {
			return false;
		} else {
			Frontier.addLast(aState);
			return true;
		}
	}

}
