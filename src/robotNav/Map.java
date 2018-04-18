package robotNav;

public class Map {

	public char[][] map;
	public int length;
	public int width;
	public int[] goalStateCoordinates;

	public Map(int[] aDimensions, int[] aInitialStateCoordinates, int[] aGoalStateCoordinates) {
		width = aDimensions[1];
		length = aDimensions[0];
		map = new char[width][length];

		for (int i = 0; i < aDimensions[1]; i++) {
			for (int j = 0; j < aDimensions[0]; j++) {
				map[i][j] = 'O';
			}
		}

		map[aInitialStateCoordinates[0]][aInitialStateCoordinates[1]] = 'X';
		goalStateCoordinates = aGoalStateCoordinates;
	}

	public Map(char[][] aMap, int aLength, int aWidth, int[] aGoalStateCoordinates) {
		map = aMap;
		length = aLength;
		width = aWidth;
		goalStateCoordinates = aGoalStateCoordinates;
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

	// For Visualisation of Map
	public void printMap() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}

	}

}
