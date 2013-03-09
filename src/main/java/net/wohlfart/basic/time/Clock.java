package net.wohlfart.basic.time;

/**
 * <p>A generic clock interface.</p>
 */
public interface Clock {

    /**
     * <p>The current number of ticks for this clock, starts at 0 and wraps around.</p>
     *
     * @return a long.
     */
    long getTicks();

    /**
     * <p>The current ticks per second of this clock.</p>
     *
     * @return a long.
     */
    long getTicksPerSecond();

    /**
     * <p>Returns the last tick count that is still valid for this clock,
     *    anything bigger will be wrapped around, usually a timer can deal with that.</p>
     *
     * @return a long.
     */
    long getMaxValidCount();

}
