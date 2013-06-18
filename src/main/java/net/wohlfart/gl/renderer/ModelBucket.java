package net.wohlfart.gl.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.VertexLight;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.spatial.Emitter;
import net.wohlfart.gl.spatial.Model;
import net.wohlfart.gl.spatial.Spatial;
import net.wohlfart.gl.view.Camera;
import net.wohlfart.gl.view.PickingRay;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * A set of Renderables that use the same GraphicContext.
 */
public class ModelBucket implements RenderBucket {

    protected Set<Model> models = new HashSet<>(10100);
    protected Set<Emitter> emitters = new HashSet<>(10100);
    protected Set<IsRenderable> renderables = new HashSet<>(10100);
    protected Set<VertexLight> lights = new HashSet<>(10);


    private IGraphicContext graphicContext;
    private Camera camera;

    // data for the render loop:
    private final Vector3f posVector = new Vector3f();
    private final Matrix4f posMatrix = new Matrix4f();
    private final Matrix4f rotMatrix = new Matrix4f();
    private final Matrix4f rotPosMatrix = new Matrix4f();

    public void setGraphicContext(IGraphicContext graphicContext) {
        this.graphicContext = graphicContext;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void setup() {
        graphicContext.setup();
    }

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
    public void addContent(IsRenderable renderable) {
        renderables.add(renderable);
    }

    public void addContent(VertexLight light) {
        lights.add(light);
    }


    /** {@inheritDoc} */
    @Override
    public void render() {
        assert graphicContext != null : "the graphicContext is null, make sure to call the init method";
        assert camera != null : "the camera is null, make sure to call the init method";

        SimpleMath.convert(camera.getPosition().negate(posVector), posMatrix);
        SimpleMath.convert(camera.getRotation(), rotMatrix);
        Matrix4f.mul(rotMatrix, posMatrix, rotPosMatrix);

        GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(rotPosMatrix);
        ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextManager.INSTANCE.getPerspectiveProjMatrix());
        ShaderUniformHandle.LIGHT_POSITION.set(new Vector3f(0, 0, 1));

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_BLEND);


        int i = 0;
        for (final VertexLight light : lights) {
            ShaderUniformHandle.LIGHTS.set(light, i++);
        }
        for (final Emitter emitter : emitters) {
            emitter.render();
        }
        for (final Spatial model : models) {
            model.render();
        }
        for (final IsRenderable renderable : renderables) {
            renderable.render();
        }
    }

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

    @Override
    public Matrix4f getProjectionMatrix() {
        return GraphicContextManager.INSTANCE.getPerspectiveProjMatrix();
    }

    @Override
    public Matrix4f getModelViewMatrix() {
        return rotPosMatrix;
    }

    public void dispose() {
        // TODO maybe destroy ?
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

            Collections.sort(list, new Comparator<Spatial>() {
                @Override
                public int compare(Spatial o1, Spatial o2) {
                    float f1 = SimpleMath.distance(ray.getStart(), o1.getPosition());
                    float f2 = SimpleMath.distance(ray.getStart(), o2.getPosition());
                    return Float.compare(f1, f2);
                }
            });

        }
        return list;
    }

}
