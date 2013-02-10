package net.wohlfart.gl.shader.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

class ColoredFragmentMesh implements IMesh {

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
    ColoredFragmentMesh(int vaoHandle, int vboVerticesHandle,
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

    @Override
    public void draw() {
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

    @Override
    public void dispose() {
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

}
