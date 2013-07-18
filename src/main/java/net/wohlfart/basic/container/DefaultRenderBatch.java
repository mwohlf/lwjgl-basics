package net.wohlfart.basic.container;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.basic.elements.IsUpdatable;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.shader.VertexLight;
import net.wohlfart.gl.view.Camera;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * implementing the basic features for a render set,
 * keeping the graphic context and some lights if needed
 */
public class DefaultRenderBatch<T extends IsUpdatable>  implements RenderBatch<T>, IsUpdatable { // REVIEWED

    private IGraphicContext graphicContext;

    private final HashSet<T> elements = new HashSet<T>();
    private final HashSet<VertexLight> lights = new HashSet<VertexLight>(10);

    // reserve some memory for the render loop:  FIXME: need to be Thread local
    private final Vector3f posVector = new Vector3f();
    private final Matrix4f posMatrix = new Matrix4f();
    private final Matrix4f rotMatrix = new Matrix4f();
    private final Matrix4f rotPosMatrix = new Matrix4f();
    private final Matrix4f projMatrix = new Matrix4f();

    public void setGraphicContext(IGraphicContext graphicContext) {
        this.graphicContext = graphicContext;
    }

    public void add(VertexLight light) {
        lights.add(light);
    }

    public void add(T element) {
        elements.add(element);
    }

    public void addAll(Collection<T> multipleElements) {
        elements.addAll(multipleElements);
    }

    public HashSet<T> getElements() {
        return elements;
    }

    @Override
    public void setup() {
        projMatrix.load(GraphicContextManager.INSTANCE.getPerspectiveProjMatrix());
        if (graphicContext == null) { // fallback
            graphicContext = new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER);
        }
        graphicContext.setup();
    }

    @Override
    public void render() {
        assert graphicContext != null : "the graphicContext is null";
        assert graphicContext.isInitialized() : "the graphicContext is not initialized, call setup() before using the graphicContext";

        final Camera camera = GraphicContextManager.INSTANCE.getCamera();       // fixme: this calculation should be moved into the camera
        SimpleMath.convert(camera.getPosition().negate(posVector), posMatrix);
        SimpleMath.convert(camera.getRotation(), rotMatrix);
        Matrix4f.mul(rotMatrix, posMatrix, rotPosMatrix);

        GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX); // the default, each model should set its own later
        ShaderUniformHandle.WORLD_TO_CAM.set(rotPosMatrix);
        ShaderUniformHandle.CAM_TO_CLIP.set(projMatrix);

        int i = 0;
        for (final VertexLight light : lights) {
            ShaderUniformHandle.LIGHTS.set(light, i++);
        }

        for (T element : elements) {
            element.render();
        }
    }

    @Override
    public void update(float timeInSec) {
        for (final T element : elements) {
            element.update(timeInSec);
        }
    }

    @Override
    public Matrix4f getModelViewMatrix() {
        return rotPosMatrix;
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projMatrix;
    }

    @Override
    public void destroy() {
        for (final T element : elements) {
            element.destroy();
        }
        elements.clear();
    }


}
