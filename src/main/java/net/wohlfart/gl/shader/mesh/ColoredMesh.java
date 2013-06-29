package net.wohlfart.gl.shader.mesh;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.Vertex;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColoredMesh implements IsRenderable {

    private final int vaoHandle;
    private final int indicesCount;
    private final int indexOffset;
    private final int indexElemSize;
    private final int indicesType;

    protected ColoredMesh(int vaoHandle, int indicesType, int indexElemSize, int indicesCount, int indexOffset) {
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



    public static class Builder {
        protected static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        public IsRenderable build() {

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
            final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
            for (int i = 0; i < vertices.length; i++) {
                verticesBuffer.put(vertices[i].getXYZW());
                verticesBuffer.put(vertices[i].getRGBA());
            }
            verticesBuffer.flip();

            // OpenGL expects to draw vertices in counter clockwise order by default
            final byte[] indices = { 0, 1, 2, 2, 3, 0 };
            final int indicesCount = indices.length;
            final ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
            indicesBuffer.put(indices);
            indicesBuffer.flip();

            // Create a new Vertex Array Object in memory and select it (bind)
            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);

            final int vboVerticesHandle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

            final int vboIndicesHandle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

            ShaderAttributeHandle.POSITION.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(),
                    Vertex.POSITION_ELEM_COUNT, GL11.GL_FLOAT, false, Vertex.colorByteCount + Vertex.positionBytesCount, Vertex.positionByteOffset);

            ShaderAttributeHandle.COLOR.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.COLOR.getLocation(),
                    Vertex.COLOR_ELEM_COUNT, GL11.GL_FLOAT, false, Vertex.colorByteCount + Vertex.positionBytesCount, Vertex.colorByteOffset);

            ShaderAttributeHandle.NORMAL.disable();

            ShaderAttributeHandle.TEXTURE_COORD.disable();

            // Deselect the VAO
            GL30.glBindVertexArray(0);


            return new ColoredMesh(vaoHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0);
        }

    }


}
