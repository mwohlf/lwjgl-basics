package net.wohlfart.gl.renderer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.wohlfart.gl.antlr4.Model;
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
public class ModelBucket implements IsRenderable, IsUpdateable, HasCamProjectionModelViewMatrices {

    protected Set<Model> container = new HashSet<>(10100);
    private IGraphicContext graphicContext;
    private Camera camera;

    // data for the render loop:
    private final Vector3f posVector = new Vector3f();
    private final Matrix4f posMatrix = new Matrix4f();
    private final Matrix4f rotMatrix = new Matrix4f();
    private final Matrix4f rotPosMatrix = new Matrix4f();


    /**
     * <p>init.</p>
     *
     * @param wireframeGraphicContext a {@link net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext} object.
     * @param camera a {@link net.wohlfart.gl.view.Camera} object.
     */
    public void init(IGraphicContext wireframeGraphicContext, Camera camera) {
        this.graphicContext = wireframeGraphicContext;
        this.camera = camera;
    }


    /**
     * <p>add.</p>
     *
     * @param elements a {@link java.util.Collection} object.
     */
    public void add(Collection<Model> elements) {
        for (Model model : elements) {
            add(model);
        }
    }

    /**
     * <p>add.</p>
     *
     * @param isRenderable a {@link net.wohlfart.gl.renderer.IsRenderable} object.
     */
    public void add(Model model) {
        container.add(model);
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        SimpleMath.convert(camera.getPosition().negate(posVector), posMatrix);
        SimpleMath.convert(camera.getRotation(), rotMatrix);
        Matrix4f.mul(rotMatrix, posMatrix, rotPosMatrix);

        GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(rotPosMatrix);
        ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextManager.INSTANCE.getPerspectiveProjMatrix());
        ShaderUniformHandle.NORMAL.set(GraphicContextManager.INSTANCE.getNormalMatrix());


        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        for (final Model model : container) {
            model.render();
        }
    }

    @Override
    public void update(float timeInSec) {
        for (final Model model : container) {
            model.update(timeInSec);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        for (final Model model : container) {
            model.destroy();
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
