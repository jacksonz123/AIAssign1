package robotNav;

public class InvalidMapException extends Exception {
	private static final long serialVersionUID = -1338253606385256863L;
	public RobotState theState;
	
	public InvalidMapException(RobotState aState) {
		theState = aState;
	}

}
