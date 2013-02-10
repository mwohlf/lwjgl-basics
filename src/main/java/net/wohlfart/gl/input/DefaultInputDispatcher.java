package net.wohlfart.gl.input;

import com.google.common.eventbus.EventBus;

/*
 * public interface for abstracting the platform dependent input devices
 */
public class DefaultInputDispatcher extends EventBus implements InputDispatcher {
    public static final String EVENTBUS_NAME = "inputProcessor";

    public DefaultInputDispatcher() {
        super(EVENTBUS_NAME);
    }

}
