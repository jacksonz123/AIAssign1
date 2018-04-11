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
		//We only want to add the new state to the fringe if it doesn't exist
		// in the fringe or the searched list.
		if(Searched.contains(aState) || Frontier.contains(aState))
		{
			return false;
		}
		else
		{
			Frontier.add(aState);
			return true;
		}
	}
	
	public Direction[] Solve(Map navMap)
	{
		//put the start state in the Fringe to get explored.
		addToFrontier(new RobotState(navMap));
		while(Frontier.size() > 0)
		{
			//get the next State
			RobotState thisState = popFrontier();
			
			//is this the goal state?
			if(thisState.equalsRobotLocation(navMap.goalStateCoordinates))
			{
				return thisState.GetPathToState();
			}
			
			//not the goal state, explore this node
			ArrayList<RobotState> newStates = thisState.explore();
			
			for(int i = 0; i < newStates.size(); i++)
			{
				RobotState newChild = newStates.get(i);
				//if you can add these new states to the fringe
				if(addToFrontier(newChild))
				{
					//then, work out it's heuristic value
					newChild.HeuristicValue = HeuristicValue(newStates.get(i), navMap.goalStateCoordinates);
					newChild.setEvaluationFunction(newChild.HeuristicValue + newChild.Cost);
				}
				
			}
			
			//Sort the fringe by it's Heuristic Value. The PuzzleComparator uses each nodes EvaluationFunction
			// to determine a node's value, based on another. The sort method uses this to sort the Fringe.
			Collections.sort(Frontier, new StateComparator());
		}
		
		//no more nodes and no path found?
		return null;
	}
	
	protected RobotState popFrontier()
	{
		//remove a state from the top of the fringe so that it can be searched.
		RobotState lState = Frontier.pollFirst();
		
		//add it to the list of searched states so that duplicates are recognised.
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
		// Using Manhattan's Distance
		int heuristic = Math.abs(robotLocation[0] - goalState[0]) + Math.abs(robotLocation[1] - goalState[1]);
				
		return heuristic;
	}
}
