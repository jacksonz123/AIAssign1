package robotNav;

import java.util.*;

public abstract class SearchMethod {
	public String code;				//the code used to identify the method at the command line
	public String longName;			//the actual name of the method.

	public abstract Direction[] Solve(Map navMap);
	
	//The fringe needs to be a Queue and a Stack.
	//LinkedList implements both interfaces.
	//LinkedList also implements List, which allows it to be sorted easily.
	public LinkedList<RobotState> Frontier;
	
	//the searched list simply needs to be able to store nodes for the purpose of checking
	//Fast addition and removal is crucial here.
	//HashSet provides constant time for add, contains and size.
	public LinkedList<RobotState> Searched;
	
	abstract public boolean addToFrontier(RobotState aState);
	abstract protected RobotState popFrontier();
	
	public void reset()
	{
		this.Frontier.clear();
		this.Searched.clear();
	}
}
