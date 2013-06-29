package net.wohlfart.basic.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.SpatialEntity;
import net.wohlfart.gl.shader.VertexLight;
import net.wohlfart.gl.spatial.Emitter;
import net.wohlfart.gl.spatial.Model;
import net.wohlfart.gl.view.PickingRay;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * A set of Renderables that use the same GraphicContext.
 */
public class ModelBucket extends DefaultRenderSet<IsRenderable> {

    protected Set<Model> models = new HashSet<>(10100);
    protected Set<Emitter> emitters = new HashSet<>(10100);
    protected Set<IsRenderable> renderables = new HashSet<>(10100);
    protected Set<VertexLight> lights = new HashSet<>(10);

    public void setContent(Collection<Model> newContent) {
        for (final IsRenderable isRenderable : models) {
            isRenderable.destroy();
        }
        models.clear();
        models.addAll(newContent);
    }

    public void addContent(Collection<Model> newContent) {
        models.addAll(newContent);
    }

    public void addContent(Model renderable) {
        models.add(renderable);
    }

    public void addContent(Emitter renderable) {
        emitters.add(renderable);
    }

    @Override
    public boolean add(IsRenderable renderable) {
        return renderables.add(renderable);
    }

    public void addContent(VertexLight light) {
        lights.add(light);
    }


    /*
    @Override
    public void render() {
        super.prepareRender();

        int i = 0;
        for (final VertexLight light : lights) {
            ShaderUniformHandle.LIGHTS.set(light, i++);
        }
        for (final Emitter emitter : emitters) {
            emitter.render();
        }
        for (final SpatialEntity model : models) {
            model.render();
        }
        for (final IsRenderable renderable : renderables) {
            renderable.render();
        }
    }
*/
    @Override
    public void update(float timeInSec) {
        for (final Model model : models) {
            model.update(timeInSec);
        }
        for (final Emitter e : emitters) {
            e.update(timeInSec);
        }
    }

    @Override
    public void destroy() {
        for (final Model model : models) {
            model.destroy();
        }
        models.clear();
    }

    public List<Model> pick(final PickingRay ray) {
        List<Model> list = new ArrayList<>();

        for (final Model model : models) {
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
