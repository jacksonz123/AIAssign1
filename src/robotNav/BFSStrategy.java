package robotNav;

import java.util.ArrayList;
import java.util.LinkedList;


public class BFSStrategy extends SearchMethod {
	
	public BFSStrategy()
	{
		code = "BFS";
		longName = "Breadth-First Search";
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
	
	public Direction[] Solve(Map navigationMap) {
		//put the start state in the Fringe to get explored.
		addToFrontier(new RobotState(navigationMap));
		
		
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
				
				for(int i = 0; i < newStates.size(); i++)
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
		//if this state has been found before,
		if(Searched.contains(aState) || Frontier.contains(aState))
		{
			//discard it
			return false;
		}
		else
		{
			//else put this item on the end of the queue;
			Frontier.addLast(aState);
			return true;
		}
	}

}
