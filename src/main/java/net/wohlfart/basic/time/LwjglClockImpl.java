package net.wohlfart.basic.time;

import org.lwjgl.Sys;

/**
 * <p>A lwjgl specific clock implementation.</p>
 */
public class LwjglClockImpl implements Clock { // REVIEWED

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
