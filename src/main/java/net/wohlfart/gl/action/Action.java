package net.wohlfart.gl.action;

import net.wohlfart.gl.view.CanMove;
import net.wohlfart.gl.view.CanRotate;


// action and actor ideas ... shameless stolen from libgdx
public interface Action {

    public interface Actor extends CanMove, CanRotate {

        void setAction(Action action);

    }


    public static final Action NULL = new Action() {

        @Override
        public void perform(Actor actor, float timeInSec) {
            // do nothing
        }

    };

    /**
     * call to update the internal state of the actor object, usually stuff like
     * position, rotation etc.
     *
     * @param timeInSec the time since the last call to this method in sec
     */
    void perform(Actor actor, float timeInSec);

}
