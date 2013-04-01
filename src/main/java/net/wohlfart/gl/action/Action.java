package net.wohlfart.gl.action;

public interface Action {

    public static final Action NULL = new Action() {

        @Override
        public void update(float timeInSec) {
            //
        }

    };

    /**
     * call to update the internal state of this object
     *
     * @param timeInSec the time since the last call to this method in sec
     */
    void update(float timeInSec);
}
