package net.wohlfart.gl.renderer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.view.HasCamProjectionModelViewMatrices;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>A set of Renderables that use the same GraphicContext.
 */
public class RenderBucket implements Renderable, HasCamProjectionModelViewMatrices {

    protected Set<Renderable> container = new HashSet<>(10100);
    private IGraphicContext graphicContext;
    private Avatar avatar;

    // data for the render loop:
    private final Vector3f posVector = new Vector3f();
    private final Matrix4f posMatrix = new Matrix4f();
    private final Matrix4f rotMatrix = new Matrix4f();
    private final Matrix4f rotPosMatrix = new Matrix4f();


    /**
     * <p>init.</p>
     *
     * @param wireframeGraphicContext a {@link net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext} object.
     * @param avatar a {@link net.wohlfart.model.Avatar} object.
     */
    public void init(IGraphicContext wireframeGraphicContext, Avatar avatar) {
        this.graphicContext = wireframeGraphicContext;
        this.avatar = avatar;
    }


    /**
     * <p>add.</p>
     *
     * @param elements a {@link java.util.Collection} object.
     */
    public void add(Collection<Renderable> elements) {
        for (Renderable renderable : elements) {
            add(renderable);
        }
    }

    /**
     * <p>add.</p>
     *
     * @param renderable a {@link net.wohlfart.gl.renderer.Renderable} object.
     */
    public void add(Renderable renderable) {
        container.add(renderable);
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        SimpleMath.convert(avatar.getPosition().negate(posVector), posMatrix);
        SimpleMath.convert(avatar.getRotation(), rotMatrix);
        Matrix4f.mul(rotMatrix, posMatrix, rotPosMatrix);

        GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(rotPosMatrix);
        ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextManager.INSTANCE.getPerspectiveProjMatrix());

        for (final Renderable renderable : container) {
            renderable.render();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        for (final Renderable renderable : container) {
            renderable.dispose();
        }
        container.clear();
    }

    /** {@inheritDoc} */
    @Override
    public Matrix4f getProjectionMatrix() {
        return GraphicContextManager.INSTANCE.getPerspectiveProjMatrix();
    }

    /** {@inheritDoc} */
    @Override
    public Matrix4f getModelViewMatrix() {
        return rotPosMatrix;
    }

}
