package net.wohlfart.basic.time;

/**
 * <p>Clock interface.</p>
 *
 *
 *
 */
public interface Clock {

    /**
     * <p>getTicks.</p>
     *
     * @return a long.
     */
    long getTicks();

    /**
     * <p>getTicksPerSecond.</p>
     *
     * @return a long.
     */
    long getTicksPerSecond();

    // last count that is still valid
    /**
     * <p>getMaxValidCount.</p>
     *
     * @return a long.
     */
    long getMaxValidCount();

}
