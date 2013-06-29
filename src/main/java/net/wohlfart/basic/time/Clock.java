package net.wohlfart.basic.time;

/**
 * A generic clock interface.
 */
public interface Clock { // REVIEWED

    /**
     * The current number of ticks for this clock, starts at 0 and wraps around.
     *
     * @return a long.
     */
    long getTicks();

    /**
     * The ticks per second of this clock.
     *
     * @return a long.
     */
    long getTicksPerSecond();

    /**
     * Returns the last tick count that is still valid for this clock, anything bigger will be wrapped around, usually a timer can deal with that.
     *
     * @return a long.
     */
    long getMaxValidCount();

    void destroy();

}
