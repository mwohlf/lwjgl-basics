package net.wohlfart.basic.elements;

public interface IsUpdatable extends IsRenderable {

    /**
     * call to update the internal state of this object
     *
     * @param timeInSec
     *            the time since the last call to this method in sec
     */
    void update(float timeInSec);

}
