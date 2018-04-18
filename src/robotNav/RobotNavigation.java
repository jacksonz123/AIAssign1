package robotNav;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RobotNavigation {

	public static final int METHOD_COUNT = 6;
	public static SearchMethod[] lMethods;
	private static boolean defaultCost;

	public static void main(String[] args) {
		InitMethods();

		if (args.length == 3) {
			defaultCost = Boolean.parseBoolean(args[2]);
		} else {
			defaultCost = true;
		}

		String method = args[1];
		SearchMethod thisMethod = null;

		for (int i = 0; i < METHOD_COUNT; i++) {
			if (lMethods[i].code.compareTo(method) == 0) {
				thisMethod = lMethods[i];
			}
		}

		Map initialMap = readMapFile(args[0]);

		Direction[] thisSolution = thisMethod.Solve(initialMap, defaultCost);

		System.out.println(args[0] + "   " + method + "   " + thisMethod.Searched.size());

		if (thisSolution == null) {
			System.out.println("No solution found.");
		} else {
			int totalCost = 0;
			for (int j = 0; j < thisSolution.length; j++) {
				totalCost += defaultCost ? 1 : getCost(thisSolution[j]);
				System.out.print(thisSolution[j].toString() + ";");
			}
			System.out.println();
			System.out.println("Total Cost: " + totalCost);
		}
	}

	private static void InitMethods() {
		lMethods = new SearchMethod[METHOD_COUNT];
		lMethods[0] = new BFSStrategy();
		lMethods[1] = new DFSStrategy();
		lMethods[2] = new GreedyBestFirstStrategy();
		lMethods[3] = new ASStrategy();
		lMethods[4] = new CUS1Strategy();
		// lMethods[5] = new CUS2Strategy();
	}

	private static Map readMapFile(String fileName) {

		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader file = new BufferedReader(reader);

			int[] mapDimensions = parseGridDimensions(file.readLine());
			int[] initialState = parseStateCoordinates(file.readLine());
			int[] goalState = parseStateCoordinates(file.readLine());

			/*
			 * System.out.println(mapDimensions[0] + " " + mapDimensions[1]);
			 * System.out.println(initialState[0] + " " + initialState[1]);
			 * System.out.println(goalState[0] + " " + goalState[1]);
			 */

			Map initialMap = new Map(mapDimensions, initialState, goalState);

			String line = null;
			while ((line = file.readLine()) != null) {
				int[] wall = parseWallCoordinates(line);
				initialMap.addWall(wall[0], wall[1], wall[2], wall[3]);
			}

			// initialMap.printMap();

			file.close();

			return initialMap;
		} catch (FileNotFoundException ex) {
			System.out.println(fileName + " not found.");
			System.exit(1);
		} catch (IOException ex) {
			System.out.println("Error reading " + fileName);
			System.exit(1);
		}

		return null;
	}

	private static int[] parseGridDimensions(String line) {
		// Replace the bracket chars
		line = line.replaceAll("\\[|\\]", "");
		// Split into array separated by ,
		String[] dimensions = line.split(",");

		// Check if input for dimensions is as expected
		if (dimensions.length == 2) {
			// Convert from String to int
			int[] intDimensions = { Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]) };

			return intDimensions;
		}

		// Exit if error with dimension input
		System.exit(1);

		return null;
	}

	private static int[] parseStateCoordinates(String line) {
		// Replace the bracket chars
		line = line.replaceAll("\\(|\\)", "");
		// Split into array separated by ,
		String[] coordinates = line.split(",");
		// Check if input for dimensions is as expected
		if (coordinates.length == 2) {
			// Convert from String to int
			int[] intCoordinates = { Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]) };

			return intCoordinates;
		}

		// Exit if error with input
		System.exit(1);

		return null;
	}

	private static int[] parseWallCoordinates(String line) {
		// Replace the bracket chars
		line = line.replaceAll("\\(|\\)", "");
		// Split into array separated by ,
		String[] coordinates = line.split(",");
		// Check if input for dimensions is as expected
		if (coordinates.length == 4) {
			// Convert from String to int
			int[] intCoordinates = { Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]),
					Integer.parseInt(coordinates[2]), Integer.parseInt(coordinates[3]) };

			return intCoordinates;
		}

		// Exit if error with input
		System.exit(1);

		return null;
	}

	private static int getCost(Direction aDirection) {
		switch (aDirection) {
		case Right:
		case Left:
			return 2;
		case Up:
			return 4;
		case Down:
			return 1;
		default:
			break;
		}
		return 0;
	}

}
