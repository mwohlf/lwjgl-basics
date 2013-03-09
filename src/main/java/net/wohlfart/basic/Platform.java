package net.wohlfart.basic;

import net.wohlfart.basic.time.Clock;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.input.InputSource;

/**
 * <p>Platform interface.</p>
 */
public interface Platform {

    /**
     * <p>createClock.</p>
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
