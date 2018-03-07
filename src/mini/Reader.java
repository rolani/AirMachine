package mini;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

	static Util.NewMap values;
	static Util.NewMap points;

	public static void readInput() throws FileNotFoundException, IOException {

		values = new Util.NewMap();
		try (BufferedReader reader = new BufferedReader(new FileReader("target.txt"))) {

			String sCurrentLine;
			String[] splits;
			// int i = 1;
			while ((sCurrentLine = reader.readLine()) != null) {
				// remove trailing spaces
				sCurrentLine = sCurrentLine.trim();
				splits = sCurrentLine.split(",");
				int timeid = Integer.parseInt(splits[0]);
				int xAxis = Integer.parseInt(splits[1]);
				int yAxis = Integer.parseInt(splits[2]);
				double mean = Double.parseDouble(splits[3]);
				double std = Double.parseDouble(splits[4]);

				values.addValue(timeid, xAxis, yAxis, mean, std);
			}

			reader.close();
		}

	}

	public static Util.NewMap getTimePoint(int id) {

		points = new Util.NewMap();
		for (int i = 0; i < values.getSize(); i++) {
			if (values.getId(i) == id) {
				if (values.getMean(i) >= 15) {
					points.addValue(values.getId(i), values.getxAxis(i), values.getyAxis(i), 15 * Math.random(),
							values.getStd(i));
				} else {
					points.addValue(values.getId(i), values.getxAxis(i), values.getyAxis(i), values.getMean(i),
							values.getStd(i));
				}
			}
		}
		return points;
	}
}
