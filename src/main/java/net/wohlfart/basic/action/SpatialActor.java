package net.wohlfart.basic.action;

import net.wohlfart.gl.view.CanMove;
import net.wohlfart.gl.view.CanRotate;


public interface SpatialActor extends CanMove, CanRotate {


    public interface Action  {

        /**
         * call this method to update the internal state of the actor object,
         * usually stuff like position, rotation etc.
         *
         * @param timeInSec
         *            the time since the last call to this method in sec
         */
        void perform(SpatialActor spatialActor, float timeInSec);

    }

    public static final Action NULL_ACTION = new Action() {

        @Override
        public void perform(SpatialActor spatialActor, float timeInSec) {
            // do nothing
        }

    };

    /**
     * attach an action to an actor
     */
    void setAction(Action action);

}
