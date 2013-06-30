package net.wohlfart.basic.container;

import java.util.HashSet;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.view.Camera;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * implementing the basic features for a render set,
 * keeping the camera and the graphic context
 */
@SuppressWarnings("serial")
public class DefaultRenderSet<T extends IsRenderable> extends HashSet<T> implements RenderSet<T> {

    private IGraphicContext graphicContext;

    // reserve some memory for the render loop:
    private final Vector3f posVector = new Vector3f();
    private final Matrix4f posMatrix = new Matrix4f();
    private final Matrix4f rotMatrix = new Matrix4f();
    private final Matrix4f rotPosMatrix = new Matrix4f();


    public void setGraphicContext(IGraphicContext graphicContext) {
        this.graphicContext = graphicContext;
    }

    @Override
    public void setup() {
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
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(rotPosMatrix);
        ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextManager.INSTANCE.getPerspectiveProjMatrix());

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_BLEND);

        for (IsRenderable element : this) {
            element.render();
        }
    }

    @Override
    public void update(float timeInSec) {
        // nothing to do yet
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return GraphicContextManager.INSTANCE.getPerspectiveProjMatrix();
    }

    @Override
    public Matrix4f getModelViewMatrix() {
        return rotPosMatrix;
    }

    @Override
    public void destroy() {
        for (IsRenderable element : this) {
            element.destroy();
        }
    }


}
