package robotNav;

import java.util.Comparator;

public class StateComparator implements Comparator<RobotState> {

	@Override
	public int compare(RobotState state1, RobotState state2) {
		return state1.getEvaluationFunction() - state2.getEvaluationFunction();
	}
}
