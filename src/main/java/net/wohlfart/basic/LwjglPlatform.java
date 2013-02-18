package net.wohlfart.basic;

import net.wohlfart.basic.time.Clock;
import net.wohlfart.basic.time.LwjglClockImpl;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.input.LwjglInputAdaptor;
import net.wohlfart.gl.input.LwjglInputSource;

public class LwjglPlatform implements Platform {

    @Override
    public Clock createClock() {
        return new LwjglClockImpl();
    }

    @Override
    public InputSource createInputSource(InputDispatcher inputDispatcher) {
        return new LwjglInputSource(new LwjglInputAdaptor(inputDispatcher));
    }

}
