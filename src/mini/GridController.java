package mini;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

//represents the fog class.
public class GridController implements Runnable {
	String name;
	Double time;
	static Thread t;
	List<Device> deviceList; // contains devices in a grid
	//contains devices selected for video capture (at most two)
	List<Device> selectedDeviceList; 
	private static final Random RANDOM = new Random();  
	private static Set<Integer> set;
	
	
	// constructor with name
	GridController(String tName) {
		set = new HashSet<Integer>();
		deviceList = new ArrayList<Device>();
		selectedDeviceList = new ArrayList<Device>();
		name = tName;
		t = new Thread(this, name);
		t.start();
	}

	@Override
	public void run() {

	}
	//register a device to a grid
	public void addDevice(Device d) {
		deviceList.add(d);
	}
	//remove a device from a grid
	public void removeDevice(Device d) {
		deviceList.remove(d);
	}
	//check if a device is in a grid
	public boolean findDevice(Device d) {
		if (deviceList.contains(d)) {
			return true;
		} else
			return false;
	}
	
	public List<Device> getDevices() {
		return deviceList;
	}
	
	//get the num of devices in grid at time point
	public int numOfDevices() {
		return deviceList.size();
	}
	
	public List<Device> getOneDevice() {
		selectedDeviceList.clear();
		if (numOfDevices() == 1) {
			selectedDeviceList.add(deviceList.get(0));		
		}else if(numOfDevices() > 1) {
			selectedDeviceList.add(deviceList.get(getRandomNumberInRange(0, numOfDevices()-1)));
		}else {
			System.out.println("No device in grid");
		}
		return selectedDeviceList;
	}
	
	//select 2 devices from the list of devices to capture video
	public List<Device> getSelectedDevices() {
		selectedDeviceList.clear();
		if (numOfDevices() > 2) {
			while (set.size() < 2) {
				set.add(getRandomNumberInRange(0, numOfDevices()-1));
			}
			//System.out.println("Size of set is: " +set.size());
			//add selected devices to selected list
			for (Integer s : set) {
			    selectedDeviceList.add(deviceList.get(s));
			    System.out.println("Device " + deviceList.get(s).name + " selected");
			}
		}else if (numOfDevices() > 0 && numOfDevices() <=2) {
			for (int i = 0; i < numOfDevices(); i++) {
				selectedDeviceList.add(deviceList.get(i));
				System.out.println("Device " + deviceList.get(i).name + " selected");
			}
		}else {
			System.out.println("No device in grid");
		}
		return selectedDeviceList;	
	}
	

	//returns a random number between min and max
	public static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		return RANDOM.nextInt((max - min) + 1) + min;
	}

}
