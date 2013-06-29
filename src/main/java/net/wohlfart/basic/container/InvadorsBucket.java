package net.wohlfart.basic.container;

import java.util.List;

import net.wohlfart.basic.action.OrbitAction;
import net.wohlfart.basic.elements.SpatialEntity;
import net.wohlfart.basic.states.SceneCreator;
import net.wohlfart.gl.spatial.Model;
import net.wohlfart.gl.view.PickingRay;

import org.lwjgl.util.vector.Vector3f;

public class InvadorsBucket extends ModelBucket {

    private static final int MIN_MODEL_COUNT = 3000;



    @Override
    public void setup() {
        super.setup();
        Model model = SceneCreator.loadModelFromFile("/models/ships/02.obj");
        model.setPosition(new Vector3f(0,0,10));
        model.setAction(OrbitAction.create(10, new Vector3f(0,0,0), new Vector3f(1,0,0)));
        models.add(model);
    }

    @Override
    public void update(float timeInSec) {

        int delta = MIN_MODEL_COUNT - models.size();
        for (int i = 0 ; i < delta ; i++) {
            add(createModel());
        }

        super.update(timeInSec);
    }


    public SpatialEntity createModel() {
        SpatialEntity model = SceneCreator.loadModelFromFile("/models/ships/02.obj");
        model.setPosition(SceneCreator.getRandomPosition(1000f));
        model.setAction(SceneCreator.getRandomAction());
        return model;
    }


    @Override
    public List<Model> pick(PickingRay ray) {
        List<Model> result = super.pick(ray);
        if (result.size() > 0) {

            Model pick = result.get(0);
            System.out.println("pick: " + pick);
            // models.remove(result.get(0));
        }
        return result;
    }

}
