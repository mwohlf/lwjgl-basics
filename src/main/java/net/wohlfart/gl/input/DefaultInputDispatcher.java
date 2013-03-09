package net.wohlfart.gl.input;

import com.google.common.eventbus.EventBus;

/*
 * public interface for abstracting the platform dependent input devices
 */
/**
 * <p>DefaultInputDispatcher class.</p>
 *
 *
 *
 */
public class DefaultInputDispatcher extends EventBus implements InputDispatcher {
    /** Constant <code>EVENTBUS_NAME="inputProcessor"</code> */
    public static final String EVENTBUS_NAME = "inputProcessor";

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
