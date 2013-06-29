package net.wohlfart.basic.time;

/**
 * A Timer implementation that works with any clock.
 */
public class TimerImpl implements Timer { // REVIEWED

    private final Clock clock;
    private final long maxValidCount;
    private long lastTickCount;

    /**
     * Constructor for TimerImpl.
     *
     * @param clock
     *            a {@link net.wohlfart.basic.time.Clock} object.
     */
    public TimerImpl(Clock clock) {
        this.clock = clock;
        this.lastTickCount = clock.getTicks();
        this.maxValidCount = clock.getMaxValidCount();
    }

    @Override
    public float getDelta() {
        final long now = clock.getTicks();
        if (now < lastTickCount) { // we have a wrap around
            lastTickCount -= maxValidCount;
            lastTickCount -= 1;
        }
        final float delta = (now - lastTickCount) / (float) clock.getTicksPerSecond();
        lastTickCount = now;
        return delta;
    }

    @Override
    public void destroy() {

    }

}
