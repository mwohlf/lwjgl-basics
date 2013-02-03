package net.wohlfart.basic.time;

import org.lwjgl.Sys;


public class LwjglClockImpl implements Clock {

	@Override
	public long getTicks() {
		return Sys.getTime(); // this wraps around
	}

	@Override
	public long getTicksPerSecond() {
		return Sys.getTimerResolution();
	}

	@Override
	public long getMaxValidCount() {
		return Long.MAX_VALUE;
	}

}
