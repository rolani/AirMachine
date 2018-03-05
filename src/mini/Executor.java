package mini;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Executor {
	private static final Random RANDOM = new Random();
	static DecimalFormat df;
	// private static List<Double> bandwidthUsage;

	static int COVERAGE = 0;

	static final double PROCESSING_COST = 10; // cost incurred in processing videos
	static final double FAILURE_RATE = 0.02; // probability of a device failing

	// video data represented per every 5 second
	private static final double STD = 0.8174;// variance of distribution
	private static final double MEAN = 3.4128;// mean of distribution

	private static FileWriter fileWriter1, fileWriter2, fileWriter3, fileWriter4;

	// looping through each time point from 1 to 48 (0 to 47)
	public static void analyzePoints(int num) throws FileNotFoundException, IOException {
		for (int j = 0; j < num; j++) {

			System.out.println("Processing timepoint " + (j + 1));
			timePointDetials(j, num);
			COVERAGE = 0;
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
				// System.out.println(grid[j][z]);
				index++;
			}

		}

		startRandomProcessing(grid, num);
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
		COVERAGE = 0;
		for (int j = 0; j < 10; j++) {
			for (int z = 0; z < 10; z++) {
				System.out.println("Grid-" + j + "-" + z);
				// create controller for grid
				if (temp[j][z] > 0) {
					COVERAGE++;
					String name = "Con-" + Integer.toString(num) + "-" + Integer.toString(j) + "-"
							+ Integer.toString(z);
					GridController gc = new GridController(name);

					// initialize devices in grid
					for (int k = 0; k < temp[j][z]; k++) {
						String time_int = "Dev-" + Integer.toString(k);
						String tName = name + "(" + time_int + ")";
						Device d = new Device(tName);
						gc.addDevice(d); // add device to controller
					}
					deviceList = gc.getSelectedDevices();
				}
				if (deviceList.size() > 0)
					captureAMVideo(deviceList);
			}
		}
		fileWriter3.write(COVERAGE + "\n");
		System.out.println("AM coveage is " + (COVERAGE) + "%");
	}

	// processing for coordinated devices
	public static void startRandomProcessing(int[][] grid, int num) throws IOException {
		List<Device> deviceList = new ArrayList<Device>();
		// double sum = 0; // number of devices that captured
		// int count = 0; // to get coverage (/100)
		int[][] temp = grid;
		COVERAGE = 0;
		for (int j = 0; j < 10; j++) {
			for (int z = 0; z < 10; z++) {
				System.out.println("Grid-" + j + "-" + z);
				// create controller for grid
				if (temp[j][z] > 0) {
					COVERAGE++;
					String name = "Con-" + Integer.toString(num) + "-" + Integer.toString(j) + "-"
							+ Integer.toString(z);
					GridController gc = new GridController(name);

					// initialize devices in grid
					for (int k = 0; k < temp[j][z]; k++) {
						String time_int = "Dev-" + Integer.toString(k);
						String tName = name + "(" + time_int + ")";
						Device d = new Device(tName);
						gc.addDevice(d); // add device to controller
					}
					deviceList = gc.getDevices();
				}
				if (deviceList.size() > 0)
					captureRandomVideo(deviceList);
			}
		}
		fileWriter4.write(COVERAGE + "\n");
		System.out.println("Random coveage is " + (COVERAGE) + "%");
	}

	public static void captureAMVideo(List<Device> devices) throws IOException {
		//boolean uploaded = false;
		boolean update = false;
		int control = 0;
		double band = 0;
		// System.out.println("Coverage is " + COVERAGE);
		// System.out.println("Selected devices " + devices.size());
		for (int z = 0; z < devices.size(); z++) {
			if (isOk()) {
				control = 1;
				// (/5) to get per second (*60) for 1 minutes (MB/s)
				band += getNextDistribution() / 5 * 60;

				if (control == 1 && update == true) {
					COVERAGE++;
				}
			} else {
				System.out.println(devices.get(z).name + " failed");
			}
			if (control == 0) {
				COVERAGE--;
				//uploaded = true;
				update = true;
			}
		}
		if (band > 0) {
			fileWriter1.write(df.format(band) + "\n");
			System.out.println("AM bandwidth used " + df.format(band) + "MB");
		}
	}

	public static void captureRandomVideo(List<Device> devices) throws IOException {
		//boolean uploaded = false;
		boolean update = false;
		int control = 0;
		double band = 0;
		for (int z = 0; z < devices.size(); z++) {
			if (isOk()) {
				control = 1;
				// (/5) to get per second (*60) for 1 minutes (MB/s)
				band += getNextDistribution() / 5 * 60;

				if (control == 1 && update == true) {
					COVERAGE++;
					update = false;
				}
			} else {
				System.out.println(devices.get(z).name + " failed");
			}
			if (control == 0) {
				COVERAGE--;
				//uploaded = true;
				update = true;
			}
		}
		if (band > 0) {
			fileWriter2.write(df.format(band) + "\n");
			System.out.println("Random bandwidth used " + df.format(band) + "MB");
		}
	}

	// video size to upload next
	public static double getNextDistribution() {
		return RANDOM.nextGaussian() * STD + MEAN;
	}

	// check if device fails or not
	public static boolean isOk() {
		if (Math.random() > FAILURE_RATE)
			return true;
		else
			return false;
	}

	// upload captured video duration 30 minutes
	public void uploadData(double size) {

	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		df = new DecimalFormat("#.00");
		// bandwidthUsage = new ArrayList<Double>();
		fileWriter1 = new FileWriter("coordB.txt");
		fileWriter2 = new FileWriter("randB.txt");
		fileWriter3 = new FileWriter("coordC.txt");
		fileWriter4 = new FileWriter("randC.txt");
		System.out.println("Starting simulation....");
		runAnalyze();

		fileWriter1.close();
		fileWriter2.close();
		fileWriter3.close();
		fileWriter4.close();
		System.out.println("Simulation ended");
		
		
	}

}
