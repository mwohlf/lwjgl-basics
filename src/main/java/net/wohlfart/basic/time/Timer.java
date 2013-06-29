package net.wohlfart.basic.time;

/**
 * A generic Timer interface, a timer is based on a clock.
 */
public interface Timer { // REVIEWED

    /**
     * Returns the time in seconds since the last call.
     *
     * @return a float with the time in seconds since the last call to getDelta()
     */
    float getDelta();

    /**
     * Destroy the timer and free its resources.
     */
    void destroy();

}
