package net.wohlfart.basic;

import net.wohlfart.basic.time.Clock;
import net.wohlfart.basic.time.LwjglClockImpl;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.input.LwjglInputAdaptor;
import net.wohlfart.gl.input.LwjglInputSource;

/**
 * <p>A class providing lwjgl specific features</p>
 */
public class LwjglPlatform implements Platform {

    /** {@inheritDoc} */
    @Override
    public Clock createClock() {
        return new LwjglClockImpl();
    }

    /** {@inheritDoc} */
    @Override
    public InputSource createInputSource(InputDispatcher inputDispatcher) {
        return new LwjglInputSource(new LwjglInputAdaptor(inputDispatcher));
    }

}
