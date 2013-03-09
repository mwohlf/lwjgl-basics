package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>Abstract AbstractRenderable class.</p>
 *
 *
 *
 */
public abstract class AbstractRenderable implements Renderable {

    protected IMesh meshData;
    protected Matrix4f modelToWorldMatrix = new Matrix4f();

    // initial translation and rotation of the mesh
    protected final Vector3f translation = new Vector3f();
    protected final Quaternion rotation = new Quaternion();

    // dynamic translation and rotation
    protected Vector3f currentTranslation = new Vector3f();
    protected Quaternion currentRotation = new Quaternion();

    /**
     * <p>setupMesh.</p>
     *
     * @return a {@link net.wohlfart.gl.shader.mesh.IMesh} object.
     */
    protected abstract IMesh setupMesh();

    /**
     * <p>resetMeshData.</p>
     */
    protected void resetMeshData() {
        meshData = null;
    }

    /**
     * <p>translate.</p>
     *
     * @param translation a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link net.wohlfart.gl.elements.AbstractRenderable} object.
     */
    public AbstractRenderable translate(Vector3f translation) {
        this.translation.set(translation);
        resetMeshData();
        return this;
    }

    /**
     * <p>rotate.</p>
     *
     * @param rotation a {@link org.lwjgl.util.vector.Quaternion} object.
     * @return a {@link net.wohlfart.gl.elements.AbstractRenderable} object.
     */
    public AbstractRenderable rotate(Quaternion rotation) {
        this.rotation.set(rotation);
        resetMeshData();
        return this;
    }

    /**
     * <p>Setter for the field <code>translation</code>.</p>
     *
     * @param currentTranslation a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void setTranslation(Vector3f currentTranslation) {
        this.currentTranslation = currentTranslation;
    }

    /**
     * <p>Setter for the field <code>rotation</code>.</p>
     *
     * @param currentRotation a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    public void setRotation(Quaternion currentRotation) {
        this.currentRotation = currentRotation;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        if (meshData == null) {
            meshData = setupMesh();
        }
        SimpleMath.createMatrix(currentTranslation, modelToWorldMatrix);
        ShaderUniformHandle.MODEL_TO_WORLD.set(modelToWorldMatrix);
        meshData.draw();
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        meshData.dispose();
        meshData = null;
    }

}
