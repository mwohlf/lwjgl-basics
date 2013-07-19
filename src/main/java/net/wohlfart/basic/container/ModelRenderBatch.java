package net.wohlfart.basic.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.wohlfart.basic.elements.SpatialEntity;
import net.wohlfart.gl.spatial.Model;
import net.wohlfart.gl.view.PickingRay;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Vector3f;


public class ModelRenderBatch extends DefaultRenderBatch<Model> {

    public List<Model> pick(final PickingRay ray) {
        List<Model> list = new ArrayList<>();

        for (final Model model : getElements()) {
            Vector3f pos = model.getPosition();
            float d = SimpleMath.distance(ray.getStart(), ray.getEnd(), pos);
            float radius = model.getRadius();

            if (d < radius) {
                list.add(model);
            }

            Collections.sort(list, new Comparator<SpatialEntity>() {
                @Override
                public int compare(SpatialEntity o1, SpatialEntity o2) {
                    float f1 = SimpleMath.distance(ray.getStart(), o1.getPosition());
                    float f2 = SimpleMath.distance(ray.getStart(), o2.getPosition());
                    return Float.compare(f1, f2);
                }
            });

        }
        return list;
    }

}
