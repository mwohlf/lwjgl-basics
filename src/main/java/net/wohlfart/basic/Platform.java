package net.wohlfart.basic;

import net.wohlfart.basic.time.Clock;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.input.InputSource;

public interface Platform {

    Clock createClock();

    InputSource createInputSource(InputDispatcher inputDispatcher);

}
