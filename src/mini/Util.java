package mini;

import java.util.ArrayList;
import java.util.List;

public class Util {
	public static class NewMap {
		private List<Integer> time_id, xAxis, yAxis;
		private List<Double>mean, std;
		
		public NewMap() {
			xAxis = new ArrayList<Integer>();
			yAxis = new ArrayList<Integer>(); 
			time_id = new ArrayList<Integer>();
			mean = new ArrayList<Double>();
			std = new ArrayList<Double>();
		}

		public void addValue(int id, int a, int b, double ave, double d) {
			xAxis.add(a);
			yAxis.add(b);
			time_id.add(id);
			mean.add(ave);
			std.add(d);
		}

		public int getyAxis(int index) {
			return yAxis.get(index);
		}

		public int getxAxis(int index) {
			return xAxis.get(index);
		}
		
		public int getId(int index) {
			return time_id.get(index);
		}
		
		public double getMean(int index) {
			return mean.get(index);
		}
		
		public double getStd(int index) {
			return std.get(index);
		}
		
		public int getSize(){
			return time_id.size();
		}
	}
}
