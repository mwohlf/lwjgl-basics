package net.wohlfart.gl.shader.mesh;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.Vertex;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColoredMesh implements IsRenderable {

    private final int vaoHandle;
    private final int indicesCount;
    private final int indexOffset;
    private final int indexElemSize;
    private final int indicesType;

    private ColoredMesh(int vaoHandle, int indicesType, int indexElemSize, int indicesCount, int indexOffset) {
        this.vaoHandle = vaoHandle;
        this.indicesType = indicesType;
        this.indexElemSize = indexElemSize;
        this.indicesCount = indicesCount;
        this.indexOffset = indexOffset;
    }

    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL11.glDrawElements(indicesType, indicesCount, indexElemSize, indexOffset);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }



    public static class Builder extends AbstractMeshBuilder {
        protected static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        @Override
        public IsRenderable build() {
            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);

            createVboHandle(createStream()); // this also binds the GL15.GL_ARRAY_BUFFER

            final byte[] indices = { 0, 1, 2, 2, 3, 0 };
            createIdxBufferHandle(indices);

            final int[] offset = {0};
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                    + ShaderAttributeHandle.COLOR.getByteCount()
                    ;
            ShaderAttributeHandle.POSITION.enable(stride, offset);
            ShaderAttributeHandle.COLOR.enable(stride, offset);
            ShaderAttributeHandle.NORMAL.disable();
            ShaderAttributeHandle.TEXTURE_COORD.disable();

            // Deselect the VAO
            GL30.glBindVertexArray(0);

            return new ColoredMesh(vaoHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indices.length, 0);
        }


        float[] createStream() {
            // We'll define our quad using 4 vertices
            final Vertex v0 = new Vertex();
            v0.setXYZ(-0.5f, 0.5f, 0f);
            v0.setRGB(1, 0, 0);
            final Vertex v1 = new Vertex();
            v1.setXYZ(-0.5f, -0.5f, 0f);
            v1.setRGB(0, 1, 0);
            final Vertex v2 = new Vertex();
            v2.setXYZ(0.5f, -0.5f, 0f);
            v2.setRGB(0, 0, 1);
            final Vertex v3 = new Vertex();
            v3.setXYZ(0.5f, 0.5f, 0f);
            v3.setRGB(1, 1, 1);

            final Vertex[] vertices = new Vertex[] { v0, v1, v2, v3 };
            float[] stream = new float[vertices.length * ( 3 + 4 )];
            int i = 0;
            for (Vertex vertex : vertices) {
                stream[i++] = vertex.getXYZ()[0];
                stream[i++] = vertex.getXYZ()[1];
                stream[i++] = vertex.getXYZ()[2];
                stream[i++] = vertex.getRGBA()[0];
                stream[i++] = vertex.getRGBA()[1];
                stream[i++] = vertex.getRGBA()[2];
                stream[i++] = vertex.getRGBA()[3];
            }

            return stream;
        }

    }


}
