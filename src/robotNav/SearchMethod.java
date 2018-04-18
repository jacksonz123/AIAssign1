package robotNav;

import java.util.*;

public abstract class SearchMethod {
	public String code;
	public String longName;

	public abstract Direction[] Solve(Map navMap, boolean aDefualtCost);

	public LinkedList<RobotState> Frontier;

	public LinkedList<RobotState> Searched;

	abstract public boolean addToFrontier(RobotState aState);

	abstract protected RobotState popFrontier();

	public void reset() {
		this.Frontier.clear();
		this.Searched.clear();
	}
}
