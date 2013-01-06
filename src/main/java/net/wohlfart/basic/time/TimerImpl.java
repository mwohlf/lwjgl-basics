package net.wohlfart.basic.time;


public class TimerImpl implements Timer {

	private final Clock clock;
	private final long maxValidCount;
	private long lastTickCount;

	public TimerImpl(Clock clock) {
		this.clock = clock;
		this.lastTickCount = clock.getTicks();
		this.maxValidCount = clock.getMaxValidCount();
	}

	@Override
	public float getDelta() {
		long now = clock.getTicks();
		if (now < lastTickCount) { // we have a wrap around
			lastTickCount -= maxValidCount;
			lastTickCount -= 1;
		}
		float delta = (now - lastTickCount) / (float) clock.getTicksPerSecond();
		lastTickCount = now;
		return delta;
	}

}
