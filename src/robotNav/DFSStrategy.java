package robotNav;

import java.util.ArrayList;
import java.util.LinkedList;

public class DFSStrategy extends SearchMethod {

	public DFSStrategy() {
		code = "DFS";
		longName = "Depth-First Search";
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

		ArrayList<RobotState> newStates = new ArrayList<RobotState>();

		while (Frontier.size() > 0) {
			RobotState thisState = popFrontier();

			if (thisState.equalsRobotLocation(navigationMap.goalStateCoordinates)) {
				return thisState.GetPathToState();
			} else {
				newStates = thisState.explore();

				for (int i = newStates.size() - 1; i > -1; i--) {
					addToFrontier(newStates.get(i));
				}
			}
		}

		return null;
	}

	public boolean addToFrontier(RobotState aState) {
		if (aState.GetNodesToState().contains(aState) || Frontier.contains(aState)) {
			return false;
		} else {
			Frontier.addFirst(aState);
			return true;
		}
	}

}
