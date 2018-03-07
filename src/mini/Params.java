package mini;

import java.text.DecimalFormat;
import java.util.Random;

public class Params {
	static final double PROCESSING_COST = 0.1; // per second cost incurred in processing videos/joining/analyzing
	//static final double FAILURE_RATE = 0.2; // probability of a device failing
	static final double TIMESLOT = 120; // unit in seconds == 2 minutes 
	static final double JOINTIME = 10; // unit in seconds

	// video data represented per every 5 second
	static final double STD = 0.8174;// variance of distribution
	static final double MEAN = 3.4128;// mean of distribution
	
	static final Random RANDOM = new Random();
	static DecimalFormat df;
	
	//status bits
	static final int AM_NO_DUPLICATION = 1;
	static final int AM_DUPLICATION = 2;
	static final int RAND = 3;
	
	//pre-join bits
	static final int AM_NO_PREJOIN = 1;
	static final int AM_PREJOIN = 2;
	
	
	
}
