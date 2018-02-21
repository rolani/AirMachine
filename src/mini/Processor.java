package mini;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Processor {

	static final double UPLOAD_SIZE = 50; // Average Mb of video across devices in a time point.
	static final double FOG_BANDWIDTH = 100; // Bandwidth available in Mbps
	static final double DEVICE_BANDWIDTH = 1; // Device bandwidth required 1Mbps
	static final double PROCESSING_COST = 10; // cost incurred in processing videos
	static final double FAILURE_RATE = 0.1; // one in twenty devices fail at some point

	private static FileWriter fileWriter1, fileWriter2;

	// looping through each time point from 1 to 48 (0 to 47)
	public static void analyzePoints(int num) throws FileNotFoundException, IOException {
		for (int j = 0; j < num; j++) {
			timePointDetials(j);
		}
	}

	// perform some operation on each time point specified by the id
	public static void timePointDetials(int i) throws FileNotFoundException, IOException {
		Util.NewMap details = new Util.NewMap();
		int[][] grid = new int[10][10];// store mean value for each grid 10 x 10
		Reader.readInput();
		details = Reader.getTimePoint(i);// get all the data for this time point
		// System.out.println("Size of first bit: " + details.getSize());
		int index = 0;
		// while (index < details.getSize())
		for (int j = 0; j < 10; j++) {
			for (int z = 0; z < 10; z++) {
				grid[j][z] = (int) Math.round(details.getMean(index));// save each mean number of devices per grid
				index++;
			}

		}
		System.out.println("Processing timepoint " + i);
		startRandomProcessing(grid);
		startAMProcessing(grid);

	}

	// specify number of time points to go through
	public static void runAnalyze() throws FileNotFoundException, IOException {
		analyzePoints(48);
	}

	// processing for coordinated devices
	public static void startAMProcessing(int[][] grid) throws IOException {
		double sum = 0; // number of devices that captured
		int count = 0; // to get coverage (/100)
		int[][] temp = grid;
		for (int j = 0; j < 10; j++) {
			for (int z = 0; z < 10; z++) {
				// System.out.println("VALUE IN GRID COORD IS: " + temp[j][z]);
				// select a single device per grid and check for failures
				if (temp[j][z] > 0 && Math.random() < FAILURE_RATE) {
					System.out.println("Value reduced from " + temp[j][z] + " to " + (temp[j][z] = temp[j][z] - 1));
					temp[j][z] = temp[j][z] - 1;
					System.out.println("Coordinated device failed");
				}
				if (temp[j][z] > 0) {

					temp[j][z] = 1;
					// System.out.println("I am adding " + temp[j][z] + " to " + sum);
					sum += temp[j][z];
					count++;
				}

			}
		}
		double BANDWIDTH_USAGE = sum * DEVICE_BANDWIDTH;
		System.out.println("The bandwidth usage with coordination is: " + BANDWIDTH_USAGE);
		System.out.println("The percentage of grid covered is: " + count);
		fileWriter1.write(sum + " " + count + "\n");

	}

	public static void startRandomProcessing(int[][] grid) throws IOException {
		double sum = 0;
		int count = 0;
		int[][] tempR = grid;
		for (int j = 0; j < 10; j++) {
			for (int z = 0; z < 10; z++) {
				// System.out.println("VALUE IN GRID IS: " + tempR[j][z]);
				if (tempR[j][z] > 0 && Math.random() < FAILURE_RATE) {
					System.out.println("Value reduced from " + tempR[j][z] + " to " + (tempR[j][z] = tempR[j][z] - 1));
					tempR[j][z] = tempR[j][z] - 1;
					System.out.println("Random device failed");
				}
				// System.out.println("I am adding " + tempR[j][z] + " to " + sum);
				sum += tempR[j][z];
				if (tempR[j][z] > 0) {
					count++;
				}
			}
		}
		double BANDWIDTH_USAGE = sum * DEVICE_BANDWIDTH;
		System.out.println("The bandwidth usage without coordination is: " + BANDWIDTH_USAGE);
		System.out.println("The percentage of grid covered is: " + count);

		fileWriter2.write(sum + " " + count + "\n");
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		fileWriter1 = new FileWriter("coord.txt");
		fileWriter2 = new FileWriter("rand.txt");
		runAnalyze();

		fileWriter1.close();
		fileWriter2.close();
	}

}
