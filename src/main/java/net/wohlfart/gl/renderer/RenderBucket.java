package net.wohlfart.gl.renderer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.wohlfart.gl.HasCamProjectionModelViewMatrices;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * a set of renderables that have common features like using the same shader/renderer
 */
public class RenderBucket implements Renderable, HasCamProjectionModelViewMatrices {

    protected Set<Renderable> container = new HashSet<>(10100);
    private IGraphicContext wireframeGraphicContext;
    private Avatar avatar;


    // data for the render loop:
    private final Vector3f posVector = new Vector3f();
    private final Matrix4f posMatrix = new Matrix4f();
    private final Matrix4f rotMatrix = new Matrix4f();
    private final Matrix4f rotPosMatrix = new Matrix4f();


    public void init(IGraphicContext wireframeGraphicContext, Avatar avatar) {
        this.wireframeGraphicContext = wireframeGraphicContext;
        this.avatar = avatar;
    }


    public void add(Collection<Renderable> elements) {
        for (Renderable renderable : elements) {
            add(renderable);
        }
    }

    public void add(Renderable renderable) {
        container.add(renderable);
    }

    @Override
    public void render() {
        SimpleMath.convert(avatar.getPosition().negate(posVector), posMatrix);
        SimpleMath.convert(avatar.getRotation(), rotMatrix);
        Matrix4f.mul(rotMatrix, posMatrix, rotPosMatrix);

        GraphicContextManager.INSTANCE.setCurrentGraphicContext(wireframeGraphicContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(rotPosMatrix);
        ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextManager.INSTANCE.getPerspectiveProjMatrix());

        for (final Renderable renderable : container) {
            renderable.render();
        }
    }

    @Override
    public void dispose() {
        for (final Renderable renderable : container) {
            renderable.dispose();
        }
        container.clear();
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return GraphicContextManager.INSTANCE.getPerspectiveProjMatrix();
    }

    @Override
    public Matrix4f getModelViewMatrix() {
        return rotPosMatrix;
    }

}
