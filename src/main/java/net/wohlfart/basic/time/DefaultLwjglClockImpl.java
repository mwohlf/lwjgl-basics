package net.wohlfart.basic.time;

import org.lwjgl.Sys;


public class DefaultLwjglClockImpl implements Clock {

	@Override
	public long getTicks() {
		return Sys.getTime(); // wraps around
	}

	@Override
	public long getTicksPerSecond() {
		return Sys.getTimerResolution();
	}

	@Override
	public long getWrapAroundCount() {
		return Long.MAX_VALUE;
	}

}
