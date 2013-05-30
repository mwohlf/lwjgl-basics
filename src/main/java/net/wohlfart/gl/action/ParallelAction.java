package net.wohlfart.gl.action;

import java.util.ArrayList;
import java.util.Arrays;

public final class ParallelAction implements Action {

    ArrayList<Action> actions = new ArrayList<>();

    private ParallelAction() {
        // use the factory method
    }

    public static ParallelAction create(Action... actions) {
        final ParallelAction result = new ParallelAction();
        result.actions.addAll(Arrays.asList(actions));
        return result;
    }

    @Override
    public void perform(Actor actor, float timeInSec) {
        for (final Action action : actions) {
            action.perform(actor, timeInSec);
        }
    }

}
