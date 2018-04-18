package robotNav;

import java.util.ArrayList;
import java.util.LinkedList;


public class DFSStrategy extends SearchMethod {
	
	public DFSStrategy()
	{
		code = "DFS";
		longName = "Depth-First Search";
		Frontier = new LinkedList<RobotState>();
		Searched = new LinkedList<RobotState>();
	}
	
	protected RobotState popFrontier()
	{
		//remove an item from the fringe to be searched
		RobotState thisState = Frontier.pop();
		//Add it to the list of searched states, so that it isn't searched again
		Searched.add(thisState);
		
		return thisState;
	}
	
	public Direction[] Solve(Map navigationMap, boolean aDefualtCost) {
		//put the start state in the Fringe to get explored.
		addToFrontier(new RobotState(navigationMap, aDefualtCost));
		
		ArrayList<RobotState> newStates = new ArrayList<RobotState>();
				
		while(Frontier.size() > 0)
		{
			//get the next item off the fringe
			RobotState thisState = popFrontier();
			
			//is it the goal item?
			if(thisState.equalsRobotLocation(navigationMap.goalStateCoordinates))
			{
				//We have found a solution! return it!
				return thisState.GetPathToState();
			}
			else
			{
				//This isn't the goal, just explore the node
				newStates = thisState.explore();
				
				//Must put into Frontier in Reverse order to uphold DFS
				for(int i = newStates.size() - 1; i > -1; i--)
				{
					//add this state to the fringe, addToFrontier() will take care of duplicates
					addToFrontier(newStates.get(i));
				}
			}
		}
		
		//No solution found and we've run out of nodes to search
		//return null.
		return null;
	}
	
	public boolean addToFrontier(RobotState aState)
	{
		//if this state has Searched before
		if(aState.GetNodesToState().contains(aState) || Frontier.contains(aState))
		{
			//discard it
			return false;
		}
		else
		{
			//else put this item on the front of the queue;
			Frontier.addFirst(aState);
			return true;
		}
	}

}
