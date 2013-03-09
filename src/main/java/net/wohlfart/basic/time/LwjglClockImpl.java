package net.wohlfart.basic.time;

import org.lwjgl.Sys;

/**
 * <p>LwjglClockImpl class.</p>
 *
 *
 *
 */
public class LwjglClockImpl implements Clock {

    /** {@inheritDoc} */
    @Override
    public long getTicks() {
        return Sys.getTime(); // this wraps around
    }

    /** {@inheritDoc} */
    @Override
    public long getTicksPerSecond() {
        return Sys.getTimerResolution();
    }

    /** {@inheritDoc} */
    @Override
    public long getMaxValidCount() {
        return Long.MAX_VALUE;
    }

}
