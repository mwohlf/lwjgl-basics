package net.wohlfart.gl.spatial;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.mesh.AbstractMeshBuilder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

public class ModelMesh implements IsRenderable {

    private final int vaoHandle;
    private final int texHandle;

    private final int indicesCount;
    private final int indexElemSize;
    private final int trianglePrimitive;

    ModelMesh(int vaoHandle, int texHandle, int trianglePrimitive, int indexElemSize, int indicesCount) {
        this.vaoHandle = vaoHandle;
        this.texHandle = texHandle;
        this.trianglePrimitive = trianglePrimitive;
        this.indexElemSize = indexElemSize;
        this.indicesCount = indicesCount;
    }

    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texHandle);
        GL11.glDrawElements(trianglePrimitive, indicesCount, indexElemSize, 0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }


    public static class Builder extends AbstractMeshBuilder {

        private int[] indices;
        private int triangelPrimitive;
        private float[] stream;

        public void setTrianglePrimitive(int triangelPrimitive) {
            this.triangelPrimitive = triangelPrimitive;
        }

        public void setIndices(int[] indices) {
            // TODO: create a byte/short array depending on the size of the indices
            this.indices = indices;
        }

        public void setVertexStream(float[] stream) {
            this.stream = stream;
        }

        @Override
        public IsRenderable build() {
            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);

            final int textureHandle = createTextureHandle("/models/cube/cube.png", GL13.GL_TEXTURE0);  // also binds the texture
            createVboHandle(stream); // this also binds the GL15.GL_ARRAY_BUFFER
            createIdxBufferHandle(indices); // this also binds the GL15.GL_ELEMENT_ARRAY_BUFFER

            final int[] offset = {0};
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                             + ShaderAttributeHandle.NORMAL.getByteCount()
                             + ShaderAttributeHandle.TEXTURE_COORD.getByteCount()
                             ;
            ShaderAttributeHandle.POSITION.enable(stride, offset);
            ShaderAttributeHandle.NORMAL.enable(stride, offset);
            ShaderAttributeHandle.TEXTURE_COORD.enable(stride, offset);
            ShaderAttributeHandle.COLOR.disable();

            GL30.glBindVertexArray(0);

            return new ModelMesh(vaoHandle, textureHandle, triangelPrimitive, GL11.GL_UNSIGNED_INT, indices.length);
        }

    }

}
