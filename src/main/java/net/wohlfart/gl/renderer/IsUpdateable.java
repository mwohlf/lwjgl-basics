package net.wohlfart.gl.renderer;

public interface IsUpdateable {

    /**
     * call to update the internal state of this object
     *
     * @param timeInSec the time since the last call to this method in sec
     */
    void update(float timeInSec);

}
