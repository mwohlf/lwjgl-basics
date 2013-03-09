package net.wohlfart.basic.time;

/**
 * <p>TimerImpl class.</p>
 *
 *
 *
 */
public class TimerImpl implements Timer {

    private final Clock clock;
    private final long maxValidCount;
    private long lastTickCount;

    /**
     * <p>Constructor for TimerImpl.</p>
     *
     * @param clock a {@link net.wohlfart.basic.time.Clock} object.
     */
    public TimerImpl(Clock clock) {
        this.clock = clock;
        this.lastTickCount = clock.getTicks();
        this.maxValidCount = clock.getMaxValidCount();
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public void destroy() {

    }

}
