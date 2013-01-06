package net.wohlfart.basic.time;

public interface Clock {

	long getTicks();

	long getTicksPerSecond();

	// last count that is still valid
	long getMaxValidCount();

}
