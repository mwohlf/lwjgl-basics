package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public abstract class AbstractRenderable implements Renderable {

    protected IMesh meshData;
    protected Matrix4f modelToWorldMatrix = new Matrix4f();

    // initial translation and rotation of the mesh
    protected final Vector3f translation = new Vector3f();
    protected final Quaternion rotation = new Quaternion();

    // dynamic translation and rotation
    protected Vector3f currentTranslation = new Vector3f();
    protected Quaternion currentRotation = new Quaternion();

    protected abstract IMesh setupMesh();

    protected void resetMeshData() {
        meshData = null;
    }

    public AbstractRenderable translate(Vector3f translation) {
        this.translation.set(translation);
        resetMeshData();
        return this;
    }

    public AbstractRenderable rotate(Quaternion rotation) {
        this.rotation.set(rotation);
        resetMeshData();
        return this;
    }

    public void setTranslation(Vector3f currentTranslation) {
        this.currentTranslation = currentTranslation;
    }

    public void setRotation(Quaternion currentRotation) {
        this.currentRotation = currentRotation;
    }

    @Override
    public void render() {
        if (meshData == null) {
            meshData = setupMesh();
        }
        SimpleMath.createMatrix(currentTranslation, modelToWorldMatrix);
        ShaderUniformHandle.MODEL_TO_WORLD.set(modelToWorldMatrix);
        meshData.draw();
    }

    @Override
    public void dispose() {
        meshData.dispose();
        meshData = null;
    }

}
