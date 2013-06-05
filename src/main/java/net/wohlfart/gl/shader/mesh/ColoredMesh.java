package net.wohlfart.gl.shader.mesh;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.tools.Vertex;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColoredMesh implements IsRenderable {

    private final int vaoHandle;
    private final int vboVerticesHandle;
    private final int vboIndicesHandle;
    private final int indicesCount;
    private final int indexOffset;
    private final int indexElemSize;
    private final int indicesType;
    private final int colorAttrib;
    private final int positionAttrib;

    // package private, created by the builder
    protected ColoredMesh(int vaoHandle, int vboVerticesHandle,
    // index
            int vboIndicesHandle, int indicesType, int indexElemSize, int indicesCount, int indexOffset,
            // attrib pos
            int colorAttrib, int positionAttrib) {

        this.vaoHandle = vaoHandle;
        this.vboVerticesHandle = vboVerticesHandle;
        // index
        this.vboIndicesHandle = vboIndicesHandle;
        this.indicesType = indicesType;
        this.indexElemSize = indexElemSize;
        this.indicesCount = indicesCount;
        this.indexOffset = indexOffset;
        // attr pos
        this.colorAttrib = colorAttrib;
        this.positionAttrib = positionAttrib;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL20.glEnableVertexAttribArray(colorAttrib);
        GL20.glEnableVertexAttribArray(positionAttrib);
        // Bind to the index VBO that has all the information about the order of the vertices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        // Draw the vertices
        GL11.glDrawElements(indicesType, indicesCount, indexElemSize, indexOffset);
        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(colorAttrib);
        GL20.glDisableVertexAttribArray(positionAttrib);
        GL30.glBindVertexArray(0);
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);
        // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboVerticesHandle);
        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboIndicesHandle);
        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }


    /**
     * <p>
     * ColoredMeshBuilder class.
     * </p>
     */
    public static class Builder {
        /** Constant <code>LOGGER</code> */
        protected static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        /**
         * <p>
         * build.
         * </p>
         *
         * @return a {@link net.wohlfart.gl.shader.mesh.IRenderable} object.
         */
        public IsRenderable build() {

            // We'll define our quad using 4 vertices of the custom 'Vertex' class
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
            // Put each 'Vertex' in one FloatBuffer the order depends on the shaders positions!
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

            final int positionAttrib = ShaderAttributeHandle.POSITION.getLocation();
            final int colorAttrib = ShaderAttributeHandle.COLOR.getLocation();

            // Create a new Vertex Buffer Object in memory and bind it
            final int vboVerticesHandle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
            // Put the positions in attribute list 0
            GL20.glVertexAttribPointer(positionAttrib, 4, GL11.GL_FLOAT, false, Vertex.colorByteCount + Vertex.positionBytesCount, Vertex.positionByteOffset);
            // Put the colors in attribute list 1
            GL20.glVertexAttribPointer(colorAttrib, 4, GL11.GL_FLOAT, false, Vertex.colorByteCount + Vertex.positionBytesCount, Vertex.colorByteOffset);
            // unbind
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            // Deselect (bind to 0) the VAO
            GL30.glBindVertexArray(0);

            // Create a new VBO for the indices and select it (bind) - INDICES
            final int vboIndicesHandle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

            return new ColoredMesh(vaoHandle, vboVerticesHandle, vboIndicesHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0, colorAttrib,
                    positionAttrib);
        }

    }
}
