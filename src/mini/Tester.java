package mini;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Tester {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Util.NewMap details = new Util.NewMap();
		//int[][] grid = new int[10][10];// store mean value for each grid 10 x 10
		Reader.readInput();
		details = Reader.getTimePoint(22);// get all the data for this time point
		
		for (int j = 0; j < details.getSize(); j++) {
			System.out.println(details.getMean(j));
		}
	}

}
