package net.wohlfart.gl.renderer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.view.Camera;
import net.wohlfart.gl.view.HasCamProjectionModelViewMatrices;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>A set of Renderables that use the same GraphicContext.
 */
public class RenderableBucket implements IsRenderable, IsUpdateable, HasCamProjectionModelViewMatrices {

    protected final Set<IsRenderable> content = new HashSet<>(10100);
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

    public void setup() {
        graphicContext.setup();
    }

    public void setContent(Collection<IsRenderable> newContent) {
        for (final IsRenderable isRenderable : content) {
            isRenderable.destroy();
        }
        content.clear();
        content.addAll(newContent);
    }

    public void addContent(Collection<IsRenderable> newContent) {
        content.addAll(newContent);
    }

    public void addContent(IsRenderable renderable) {
        content.add(renderable);
    }

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

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_BLEND);

        for (final IsRenderable isRenderable : content) {
            isRenderable.render();
        }
    }

    @Override
    public void update(float timeInSec) {
        // nothing to do
    }

    @Override
    public void destroy() {
        for (final IsRenderable isRenderable : content) {
            isRenderable.destroy();
        }
        content.clear();
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

}
