package robotNav;

public class CantMoveThatWayException extends Exception {
	private static final long serialVersionUID = -3145282826321549422L;
	public RobotState Source;
	public Direction Direction;
	
	public CantMoveThatWayException(RobotState source, Direction aDirection)
	{
		Source = source;
		Direction = aDirection;
	}
}
