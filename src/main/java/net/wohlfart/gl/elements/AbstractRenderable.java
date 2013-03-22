package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>Abstract AbstractRenderable class.</p>
 */
public abstract class AbstractRenderable implements IsRenderable {

    // initial translation and rotation of the mesh
    protected final Vector3f translation = new Vector3f();
    protected final Quaternion rotation = new Quaternion();
    // a static mesh that is created lazy
    private IsRenderable delegate;

    // dynamic translation and rotation
    private boolean matrixIsOutdated = true;
    private Vector3f currentTranslation = new Vector3f();
    private Quaternion currentRotation = new Quaternion();
    private final Matrix4f modelToWorldMatrix = Matrix4f.setZero(new Matrix4f());

    /**
     * <p>This is the core method that needs to be implemented by subclasses,
     * use the  </p>
     *
     * @return a {@link net.wohlfart.gl.shader.mesh.IRenderable} object.
     */
    protected abstract IsRenderable setupMesh();

    /**
     * <p>This method has to be called to whenever something in the object data was changed
     *   in order to recreate the mesh data.</p>
     */
    protected void destroyMeshData() {
        delegate = null;
    }

    /**
     * <p>For setting up an initial translation, note this method is not
     * meant for moving this object around, it is rather used for setting
     * up an initial displacement of the object away from the origin.</p>
     *
     * @param translation a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link net.wohlfart.gl.elements.AbstractRenderable} object.
     */
    public AbstractRenderable withTranslation(Vector3f translation) {
        this.translation.set(translation);
        destroyMeshData();
        return this;
    }

    /**
     * <p>rotate.</p>
     *
     * @param rotation a {@link org.lwjgl.util.vector.Quaternion} object.
     * @return a {@link net.wohlfart.gl.elements.AbstractRenderable} object.
     */
    public AbstractRenderable withRotation(Quaternion rotation) {
        this.rotation.set(rotation);
        destroyMeshData();
        return this;
    }

    /**
     * <p>Setter for the field <code>translation</code>.</p>
     *
     * @param currentTranslation a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void setTranslation(Vector3f currentTranslation) {
        this.currentTranslation = currentTranslation;
        matrixIsOutdated = true;
    }

    /**
     * <p>Setter for the field <code>rotation</code>.</p>
     *
     * @param currentRotation a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    public void setRotation(Quaternion currentRotation) {
        this.currentRotation = currentRotation;
        matrixIsOutdated = true;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        if (delegate == null) {
            delegate = setupMesh();
        }
        if (matrixIsOutdated) {
            SimpleMath.convert(currentTranslation, currentRotation, modelToWorldMatrix);
            matrixIsOutdated = false;
        }
        ShaderUniformHandle.MODEL_TO_WORLD.set(modelToWorldMatrix);
        delegate.render();
    }

    @Override
    public void update(float timeInSec) {
        // nothing to update
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        delegate.destroy();
        delegate = null;
    }

}
