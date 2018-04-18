package robotNav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class ASStrategy extends SearchMethod {
	
	public ASStrategy()
	{
		code = "AS";
		longName = "A Star Search";
		Frontier = new LinkedList<RobotState>();
		Searched = new LinkedList<RobotState>();
	}
	
	public boolean addToFrontier(RobotState aState)
	{
		if(aState.GetNodesToState().contains(aState) || Frontier.contains(aState))
		{
			return false;
		}
		else
		{
			Frontier.add(aState);
			return true;
		}
	}
	
	public Direction[] Solve(Map navMap, boolean aDefualtCost)
	{
		addToFrontier(new RobotState(navMap, aDefualtCost));
		while(Frontier.size() > 0)
		{
			RobotState thisState = popFrontier();
			
			if(thisState.equalsRobotLocation(navMap.goalStateCoordinates))
			{
				return thisState.GetPathToState();
			}
			
			ArrayList<RobotState> newStates = thisState.explore();
			
			for(int i = 0; i < newStates.size(); i++)
			{
				RobotState newChild = newStates.get(i);
				if(addToFrontier(newChild))
				{
					newChild.HeuristicValue = HeuristicValue(newStates.get(i), navMap.goalStateCoordinates);
					newChild.setEvaluationFunction(newChild.HeuristicValue + newChild.Cost);
				}
				
			}
			
			Collections.sort(Frontier, new StateComparator());
		}
		return null;
	}

	protected RobotState popFrontier()
	{
		RobotState lState = Frontier.pollFirst();
		
		Searched.add(lState);
		
		return lState;
	}
	
	private int HeuristicValue(RobotState aState, int[] goalState)
	{
		int[] robotLocation = {0 , 0};
		try {
			robotLocation = aState.findRobotCell();
		} catch (InvalidMapException e) {
			System.out.println("Error with heuristic evaluation");
		}
		int heuristic = Math.abs(robotLocation[0] - goalState[0]) + Math.abs(robotLocation[1] - goalState[1]);
				
		return heuristic;
	}
}
