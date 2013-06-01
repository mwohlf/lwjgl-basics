package net.wohlfart.gl.renderer;

import java.util.List;

import net.wohlfart.basic.states.SceneCreator;
import net.wohlfart.gl.model.Model;
import net.wohlfart.gl.view.PickingRay;

public class InvadorsBucket extends ModelBucket {

    private static final int MIN_MODEL_COUNT = 30;



    @Override
    public void update(float timeInSec) {

        int delta = MIN_MODEL_COUNT - models.size();
        for (int i = 0 ; i < delta ; i++) {
            addContent(createModel());
        }

        super.update(timeInSec);
    }


    public Model createModel() {
        Model model = SceneCreator.loadModelFromFile("/models/ships/02.obj");
        model.setPosition(SceneCreator.getRandomPosition());
        model.setAction(SceneCreator.getRandomAction());
        return model;
    }


    @Override
    public List<Model> pick(PickingRay ray) {
        List<Model> result = super.pick(ray);
        if (result.size() > 0) {
            models.remove(result.get(0));
        }
        return result;
    }

}
