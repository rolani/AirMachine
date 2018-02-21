package mini;

import java.util.ArrayList;
import java.util.List;

//represents the fog class.
public class Controller implements Runnable {
	String name;
	Double time;
	static Thread t;
	List<Device> deviceList; // contains devices under a fog
	// constructor with name

	Controller(String tName) {
		deviceList = new ArrayList<Device>();
		name = tName;
		t = new Thread(this, name);
		t.start();
	}

	@Override
	public void run() {

	}

	public void addDevice(Device d) {
		deviceList.add(d);
	}

	public void removeDevice(Device d) {
		deviceList.remove(d);
	}

	public boolean findDevice(Device d) {
		if (deviceList.contains(d)) {
			return true;
		} else
			return false;
	}

	public int numOfDevices() {
		return deviceList.size();
	}

}
