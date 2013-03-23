package net.wohlfart.basic.time;

/**
 * <p>A generic Timer interface,
 *    a timer is based on a clock.</p>
 */
public interface Timer { // REVIEWED

    /**
     * <p>Returns the time in seconds since the last call.</p>
     *
     * @return a float with the time in seconds since the last call to getDelta()
     */
    float getDelta();

    /**
     * <p>Destroy the timer and free its resources.</p>
     */
    void destroy();

}
