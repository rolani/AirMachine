package mini;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Executor {
	private static final Random RANDOM = new Random();

	static final double PROCESSING_COST = 10; // cost incurred in processing videos
	static final double FAILURE_RATE = 0.1; // one in twenty devices fail at some point
	// static int TIMEPOINTS = 0;

	// video data represented per every 5 second
	private static final double STD = 0.8174;// variance of distribution
	private static final double MEAN = 3.4128;// mean of distribution

	private static FileWriter fileWriter1, fileWriter2;

	// looping through each time point from 1 to 48 (0 to 47)
	public static void analyzePoints(int num) throws FileNotFoundException, IOException {
		for (int j = 0; j < num; j++) {
			timePointDetials(j, num);
		}
	}

	// perform some operation on each time point specified by the id
	public static void timePointDetials(int i, int num) throws FileNotFoundException, IOException {
		Util.NewMap details = new Util.NewMap();
		int[][] grid = new int[10][10];// store mean value for each grid 10 x 10
		Reader.readInput();
		details = Reader.getTimePoint(i);// get all the data for this time point
		// System.out.println("Size of first bit: " + details.getSize());
		int index = 0;
		// while (index < details.getSize())
		for (int j = 0; j < 10; j++) {
			for (int z = 0; z < 10; z++) {
				// save each mean number of devices per grid
				grid[j][z] = (int) Math.round(details.getMean(index));
				index++;
			}

		}
		System.out.println("Processing timepoint " + i);
		// startRandomProcessing(grid);
		startAMProcessing(grid, num);

	}

	// specify number of time points to go through
	public static void runAnalyze() throws FileNotFoundException, IOException {
		analyzePoints(48);
	}

	// processing for coordinated devices
	public static void startAMProcessing(int[][] grid, int num) throws IOException {
		List<Device> deviceList = new ArrayList<Device>();
		// double sum = 0; // number of devices that captured
		// int count = 0; // to get coverage (/100)
		int[][] temp = grid;
		for (int j = 0; j < 10; j++) {
			for (int z = 0; z < 10; z++) {
				// create controller for grid
				if (temp[j][z] > 0) {
					String name = "Con " + Integer.toString(num) + "-" + Integer.toString(j) + "-"
							+ Integer.toString(z);
					GridController gc = new GridController(name);

					// initialize devices in grid
					for (int k = 0; k < temp[j][z]; k++) {
						String time_int = "Dev " + Integer.toString(k);
						String tName = name + "(" + time_int + ")";
						Device d = new Device(tName);
						gc.addDevice(d); // add device to controller
					}
					deviceList = gc.getSelectedDevices();
				}
				captureVideo(deviceList);
			}
		}
	}

	public static void captureVideo(List<Device> devices) {
		for (int z = 0; z < devices.size(); z++) {
			if (isOk()) {
				
			}
		}
	}
	//video size to upload next
	public static double getNextDistribution() {
		return RANDOM.nextGaussian() * STD + MEAN;
	}
	
	//check if device fails or not
	public static boolean isOk() {
		if (Math.random() > FAILURE_RATE) {
			return true;
		}else
			return false;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		fileWriter1 = new FileWriter("coord.txt");
		fileWriter2 = new FileWriter("rand.txt");
		runAnalyze();

		fileWriter1.close();
		fileWriter2.close();
	}

}
