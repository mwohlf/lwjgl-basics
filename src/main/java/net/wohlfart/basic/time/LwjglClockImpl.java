package net.wohlfart.basic.time;

import org.lwjgl.Sys;

/**
 * A lwjgl specific clock implementation.
 */
public class LwjglClockImpl implements Clock { // REVIEWED

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

    @Override
    public void destroy() {
        // no resources to free here
    }

}
