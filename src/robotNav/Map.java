package robotNav;

public class Map {

	public char[][] map;
	public int length;
	public int width;
	public int[] goalStateCoordinates;

	public Map(int[] dimensions, int[] initialStateCoordinates, int[] goalStateCoordinates) {
		width = dimensions[1];
		length = dimensions[0];
		// set dimensions of 2D map
		map = new char[width][length];

		// Initialize the board
		for (int i = 0; i < dimensions[1]; i++) {
			for (int j = 0; j < dimensions[0]; j++) {
				map[i][j] = 'O';
			}
		}

		// Mark the Robot as 'X'
		map[initialStateCoordinates[0]][initialStateCoordinates[1]] = 'X';
		this.goalStateCoordinates = goalStateCoordinates;
	}

	public Map(char[][] map, int length, int width, int[] goalStateCoordinates) {
		this.map = map;
		this.length = length;
		this.width = width;
		this.goalStateCoordinates = goalStateCoordinates;
	}

	public void addWall(int x, int y, int cellsWide, int cellsLong) {
		// start from y coordinate and increment according to cellsLong
		for (int j = y; j < y + cellsLong; j++) {
			// start from x and and increment according to cellsWide
			for (int i = x; i < x + cellsWide; i++) {
				// Add wall at coordinates
				map[i][j] = 'W';
			}
		}
	}

	// For Visualization of Map
	// Used For Debugging
	public void printMap() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}

	}

}
