package robotNav;

public class InvalidMapException extends Exception {
	public RobotState theState;
	static final long serialVersionUID = 1988122501;
	
	public InvalidMapException(RobotState aState) {
		theState = aState;
	}

}
