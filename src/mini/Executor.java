package mini;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Executor extends Params {
	static int COUNT = 0;
	static double COVERAGE = 0;
	static double FAILURE_RATE;

	private static FileWriter fileWriter1, fileWriter2, fileWriter3, fileWriter4, fileWriter5, fileWriter6;

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
		preProcess(grid, num, AM_NO_DUPLICATION);
		preProcess(grid, num, AM_DUPLICATION);
		preProcess(grid, num, RAND);
		processCoverage(grid, num, AM_NO_PREJOIN);
		processCoverage(grid, num, AM_PREJOIN);
		processCoverage(grid, num, RAND);

	}

	// specify number of time points to go through
	public static void runAnalyze() throws FileNotFoundException, IOException {
		analyzePoints(48);
	}

	// processing grids and device joining
	public static void preProcess(int[][] grid, int num, int status) throws IOException {
		List<Device> deviceList = new ArrayList<Device>();
		// double sum = 0; // number of devices that captured
		// int count = 0; // to get coverage (/100)
		int[][] temp = grid;
		// COVERAGE = 0;
		for (int j = 0; j < 10; j++) {
			for (int z = 0; z < 10; z++) {
				System.out.println("Grid-" + j + "-" + z);
				// create controller for grid
				if (temp[j][z] > 0) {
					// COVERAGE++;
					String name = "Con-" + Integer.toString(num) + "-" + Integer.toString(j) + "-"
							+ Integer.toString(z);
					GridController gc = new GridController(name);

					// devices join grid controller
					for (int k = 0; k < temp[j][z]; k++) {
						String time_int = "Dev-" + Integer.toString(k);
						String tName = name + "(" + time_int + ")";
						Device d = new Device(tName);
						gc.addDevice(d); // add device to controller
					}

					switch (status) {
					case 1: // coordinated capture without duplication
						deviceList = gc.getOneDevice();
						captureVideo(deviceList, status);
						break;
					case 2: // coordinated capture with duplication
						deviceList = gc.getSelectedDevices();
						captureVideo(deviceList, status);
						break;
					case 3: // random capture
						deviceList = gc.getDevices();
						captureVideo(deviceList, status);
						break;
					default: // error state
						System.out.println("Invalid response");
					}

				}
			}
		}
	}

	// processing grids and device joining
	public static void processCoverage(int[][] grid, int num, int status) throws IOException {
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
					// COVERAGE++;
					String name = "Con-" + Integer.toString(num) + "-" + Integer.toString(j) + "-"
							+ Integer.toString(z);
					GridController gc = new GridController(name);

					// devices join grid controller
					for (int k = 0; k < temp[j][z]; k++) {
						String time_int = "Dev-" + Integer.toString(k);
						String tName = name + "(" + time_int + ")";
						Device d = new Device(tName);
						gc.addDevice(d); // add device to controller
					}

					switch (status) {
					case 1: // coordinated capture with early join
						deviceList = gc.getSelectedDevices();
						captureCoverage(deviceList, status);
						break;
					case 2: // coordinated capture without early join
						deviceList = gc.getSelectedDevices();
						captureCoverage(deviceList, status);
						break;
					case 3: // random capture
						deviceList = gc.getDevices();
						captureCoverage(deviceList, status);
						break;
					default: // error state
						System.out.println("Invalid response");
					}

				}
			}
		}
		COVERAGE = COVERAGE / COUNT;
	}

	public static void captureVideo(List<Device> devices, int status) throws IOException {
		// boolean uploaded = false;
		double band = 0;
		// System.out.println("Coverage is " + COVERAGE);
		// System.out.println("Selected devices " + devices.size());
		for (int z = 0; z < devices.size(); z++) {
			if (isOk()) {
				// (/5) to get per second (*60) for 1 minutes (MB/s)
				band += getNextDistribution() / 5 * TIMESLOT;

			} else {
				System.out.println(devices.get(z).name + " failed");
			}
		}
		if (band > 0) {
			switch (status) {
			case 1: // coordinated capture without duplication
				fileWriter1.write(df.format(band) + "\n");
				System.out.println("AM with no dup bandwidth used " + df.format(band) + "MB");
				break;
			case 2: // coordinated capture with duplication
				fileWriter2.write(df.format(band) + "\n");
				System.out.println("AM with dup bandwidth used " + df.format(band) + "MB");
				break;
			case 3: // random capture
				fileWriter3.write(df.format(band) + "\n");
				System.out.println("Random setup bandwidth used " + df.format(band) + "MB");
				break;
			default: // error state
				System.out.println("Invalid response");
			}

		}
	}

	public static void captureCoverage(List<Device> devices, int status) throws IOException {
		// boolean uploaded = false;
		double band = 0;
		double totalExpectedData = 0; // expected data assuming no wasted time
		double processingCost;
		double coverage;
		double vidData = getNextDistribution();

		switch (status) {
		case 1: // coordinated capture without early join
			for (int z = 0; z < devices.size(); z++) {

				if (isOk()) {
					// (/5) to get per second (*60) for 1 minutes (MB/s)
					band += vidData / 5 * (TIMESLOT - JOINTIME);

				} else {
					System.out.println(devices.get(z).name + " failed");
				}
			}
			COUNT++;
			totalExpectedData = vidData / 5 * devices.size() * (TIMESLOT);
			processingCost = ((TIMESLOT - JOINTIME) * PROCESSING_COST) * (band * PROCESSING_COST);
			coverage = band / totalExpectedData * 100;

			// if (coverage > 0) {
			fileWriter4.write(df.format(coverage) + " " + df.format(processingCost) + "\n");
			System.out.println("AM without early join, coverage: " + df.format(coverage) + "MB, cost: "
					+ df.format(processingCost));
			// }
			break;
		case 2: // coordinated capture with early join
			for (int z = 0; z < devices.size(); z++) {

				if (isOk()) {
					// (/5) to get per second (*60) for 1 minutes (MB/s)
					band += vidData / 5 * (TIMESLOT);

				} else {
					System.out.println(devices.get(z).name + " failed");
				}
			}
			totalExpectedData = vidData / 5 * devices.size() * (TIMESLOT);
			processingCost = ((TIMESLOT + JOINTIME) * PROCESSING_COST) * (band * PROCESSING_COST);
			coverage = band / totalExpectedData * 100;

			// if (coverage > 0) {
			fileWriter5.write(df.format(coverage) + " " + df.format(processingCost) + "\n");
			System.out.println(
					"AM with early join, coverage: " + df.format(coverage) + "MB, cost: " + df.format(processingCost));
			// }
			break;
		case 3: // random capture
			for (int z = 0; z < devices.size(); z++) {

				if (isOk()) {
					// (/5) to get per second (*60) for 1 minutes (MB/s)
					band += vidData / 5 * (TIMESLOT);

				} else {
					System.out.println(devices.get(z).name + " failed");
				}
			}
			totalExpectedData = vidData / 5 * devices.size() * (TIMESLOT);
			processingCost = (TIMESLOT * PROCESSING_COST) * (band * PROCESSING_COST);
			coverage = band / totalExpectedData * 100;
			// if (coverage > 0) {
			fileWriter6.write(df.format(coverage) + " " + df.format(processingCost) + "\n");
			System.out.println(
					"Random setup, coverage: " + df.format(coverage) + "MB, cost: " + df.format(processingCost));
			// }
			break;
		default: // error state
			System.out.println("Invalid response");
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
		try {
			FAILURE_RATE = Double.parseDouble(args[0]);

		} catch (Exception e) {
			System.out.println("Seven arguments needed -- input file, output file, no of machines, "
					+ "over/under shoot, sync degree, no of continuous runs and dynamic output file");
		}
		df = new DecimalFormat("#.00");
		// bandwidthUsage = new ArrayList<Double>();
		fileWriter1 = new FileWriter("AM_NoDup.txt", true);
		fileWriter2 = new FileWriter("AM_Dup.txt", true);
		fileWriter3 = new FileWriter("Random.txt", true);
		fileWriter4 = new FileWriter("AM_NoJoin.txt", true);
		fileWriter5 = new FileWriter("AM_Join.txt", true);
		fileWriter6 = new FileWriter("Random_C.txt", true);
		System.out.println("Starting simulation....");
		runAnalyze();

		// close file writers
		fileWriter1.close();
		fileWriter2.close();
		fileWriter3.close();
		fileWriter4.close();
		fileWriter5.close();
		fileWriter6.close();
		System.out.println("Simulation ended");

	}

}
