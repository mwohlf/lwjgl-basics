package net.wohlfart.basic.time;

public interface Clock {

	long getTicks();

	long getTicksPerSecond();

	long getWrapAroundCount();

}
