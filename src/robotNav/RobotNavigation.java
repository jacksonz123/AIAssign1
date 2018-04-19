package robotNav;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RobotNavigation {

	public static final int METHOD_COUNT = 6;
	public static SearchMethod[] searchMethods;
	private static boolean defaultCost;

	public static void main(String[] args) {
		initSearchMethods();

		// Check if boolean value is passed to change cost settings
		if (args.length == 3) {
			defaultCost = Boolean.parseBoolean(args[2]);
		} else {
			defaultCost = true;
		}

		// Get method from first String
		String method = args[1];
		SearchMethod thisMethod = null;

		// Select appropriate method
		for (int i = 0; i < METHOD_COUNT; i++) {
			if (searchMethods[i].code.compareTo(method) == 0) {
				thisMethod = searchMethods[i];
			}
		}

		// Read Input File and create map
		Map initialMap = readMapFile(args[0]);

		// Solve the problem
		Direction[] thisSolution = thisMethod.Solve(initialMap, defaultCost);

		System.out.println(args[0] + "   " + method + "   " + thisMethod.nodesSearched);

		// Print out response accordingly
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
	
	// Initialize search methods
	private static void initSearchMethods() {
		searchMethods = new SearchMethod[METHOD_COUNT];
		searchMethods[0] = new BFSStrategy();
		searchMethods[1] = new DFSStrategy();
		searchMethods[2] = new GreedyBestFirstStrategy();
		searchMethods[3] = new ASStrategy();
		searchMethods[4] = new IDAStrategy();
		searchMethods[5] = new IDSStrategy();
	}

	private static Map readMapFile(String fileName) {

		try {
			// Set up file reader
			FileReader reader = new FileReader(fileName);
			BufferedReader file = new BufferedReader(reader);

			// parse map dimensions
			int[] mapDimensions = parseGridDimensions(file.readLine());
			int[] initialState = parseStateCoordinates(file.readLine());
			int[] goalState = parseStateCoordinates(file.readLine());

			// create map
			Map initialMap = new Map(mapDimensions, initialState, goalState);

			// Read remaining lines to add walls
			String line = null;
			while ((line = file.readLine()) != null) {
				int[] wall = parseWallCoordinates(line);
				initialMap.addWall(wall[0], wall[1], wall[2], wall[3]);
			}

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
		System.out.println("Dimensions could not be processed");
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
		System.out.println("Please coordinates in correct format");
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
		System.out.println("Please enter walls in correct format");
		System.exit(1);

		return null;
	}

	// Get the Cost for custom costing
	private static int getCost(Direction direction) {
		switch (direction) {
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
