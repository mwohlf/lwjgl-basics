package net.wohlfart.gl.action;

import java.util.ArrayList;
import java.util.Arrays;

public final class SequentalAction implements Action {

    ArrayList<Action> actions = new ArrayList<>();

    private SequentalAction() {
        // use the factory method
    }

    public static SequentalAction create(Action... actions) {
        final SequentalAction result = new SequentalAction();
        result.actions.addAll(Arrays.asList(actions));
        return result;
    }

    @Override
    public void perform(Actor actor, float timeInSec) {
        // TODO: check if an action is done and skip to the next one...
    }

}
