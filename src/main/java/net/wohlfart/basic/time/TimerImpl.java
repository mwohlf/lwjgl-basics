package net.wohlfart.basic.time;


public class TimerImpl implements Timer {

	private final Clock clock;
	private final long wrapAroundCount;
	private long lastTickCount;

	public TimerImpl(final Clock clock) {
		this.clock = clock;
		this.lastTickCount = clock.getTicks();
		this.wrapAroundCount = clock.getWrapAroundCount();
	}

	@Override
	public float getDelta() {
		long now = clock.getTicks();
		if (now < lastTickCount) { // wrap around
			lastTickCount -= wrapAroundCount;
		}
		float delta = (now - lastTickCount) / (float) clock.getTicksPerSecond();
		lastTickCount = now;
		return delta;
	}

}
