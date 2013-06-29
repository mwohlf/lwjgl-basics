package net.wohlfart.basic.action;

import java.util.ArrayList;
import java.util.Arrays;

public final class SequentalAction implements SpatialActor.Action {

    ArrayList<SpatialActor.Action> actions = new ArrayList<>();

    private SequentalAction() {
        // use the factory method
    }

    public static SequentalAction create(SpatialActor.Action... actions) {
        final SequentalAction result = new SequentalAction();
        result.actions.addAll(Arrays.asList(actions));
        return result;
    }

    @Override
    public void perform(SpatialActor spatialActor, float timeInSec) {
        // TODO: check if an action is done and skip to the next one...
    }

}
