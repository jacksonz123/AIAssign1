package robotNav;

public class CantMoveThatWayException extends Exception {
static final long serialVersionUID = 1988122502;
	
	public RobotState Source;
	public Direction Direction;
	
	public CantMoveThatWayException(RobotState source, Direction aDirection)
	{
		//The puzzle in Source tried to move in the direction aDirection.
		//This is an illegal move (It put a tile off the edge of the puzzle!)
		Source = source;
		Direction = aDirection;
	}
}
