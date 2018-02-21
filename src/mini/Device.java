package mini;

public class Device implements Runnable {
	String name; // id of taxi
	static Thread t;

	// constructor with name
	Device(String tName) {
		name = tName;
		t = new Thread(this, name);
		t.start();
	}

	@Override
	public void run() {

	}

	public void joinAM(Controller ctrl) {
		ctrl.addDevice(this);
	}

	public void leaveAM(Controller ctrl) {
		ctrl.removeDevice(this);
	}

	public boolean isConnected(Controller ctrl) {
		return ctrl.findDevice(this);
	}

}
