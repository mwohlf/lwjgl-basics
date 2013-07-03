package net.wohlfart.gl.input;

import com.google.common.eventbus.EventBus;

/**
 * DefaultInputDispatcher class implements an event bus for anything coming from the user as input.
 */
public class DefaultInputDispatcher extends EventBus implements InputDispatcher {
    static final String EVENTBUS_NAME = "inputProcessor";

    public DefaultInputDispatcher() {
        super(EVENTBUS_NAME);
    }

    @Override
    public void destroy() {
        //
    }

    @Override
    public void register(Object listener) {
        super.register(listener);
    }

    @Override
    public void unregister(Object listener) {
        super.unregister(listener);
    }

    @Override
    public void post(Object event) {
        super.post(event);
    }

}
