package net.wohlfart.gl.input;

import com.google.common.eventbus.EventBus;

/**
 * <p>DefaultInputDispatcher class implements an event
 *    bus for anything coming from the user as input.</p>
 */
public class DefaultInputDispatcher extends EventBus implements InputDispatcher {
    static final String EVENTBUS_NAME = "inputProcessor";

    /**
     * <p>Constructor for DefaultInputDispatcher.</p>
     */
    public DefaultInputDispatcher() {
        super(EVENTBUS_NAME);
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        //
    }

}
