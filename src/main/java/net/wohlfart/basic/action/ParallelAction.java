package net.wohlfart.basic.action;

import java.util.ArrayList;
import java.util.Arrays;

public final class ParallelAction implements SpatialActor.Action {

    ArrayList<SpatialActor.Action> actions = new ArrayList<>();

    private ParallelAction() {
        // use the factory method
    }

    public static ParallelAction create(SpatialActor.Action... actions) {
        final ParallelAction result = new ParallelAction();
        result.actions.addAll(Arrays.asList(actions));
        return result;
    }

    @Override
    public void perform(SpatialActor spatialActor, float timeInSec) {
        for (final SpatialActor.Action action : actions) {
            action.perform(spatialActor, timeInSec);
        }
    }

}
