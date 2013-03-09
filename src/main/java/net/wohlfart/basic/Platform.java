package net.wohlfart.basic;

import net.wohlfart.basic.time.Clock;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.input.InputSource;

/**
 * <p>Platform interface,
 *  we try to encapsulate anything platform specific behind this interface.</p>
 */
public interface Platform {

    /**
     * <p>Create some kind of clock, this method should only be needed once,
     * multiple timers can be derived from this clock</p>
     *
     * @return a {@link net.wohlfart.basic.time.Clock} object.
     */
    Clock createClock();

    /**
     * <p>createInputSource.</p>
     *
     * @param inputDispatcher a {@link net.wohlfart.gl.input.InputDispatcher} object.
     * @return a {@link net.wohlfart.gl.input.InputSource} object.
     */
    InputSource createInputSource(InputDispatcher inputDispatcher);

}
