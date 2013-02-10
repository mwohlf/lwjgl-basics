package net.wohlfart.gl.shader.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class TexturedFragmentMesh implements IMesh {

    private final int vaoHandle;
    private final int vboVerticesHandle;
    private final int vboIndicesHandle;
    private final int indicesCount;
    private final int indexOffset;
    private final int indexElemSize;
    private final int indicesType;

    private final int colorAttrib;
    private final int positionAttrib;
    private final int textureAttrib;

    private final int textureId;

    // package private, created by the builder
    public TexturedFragmentMesh(int vaoHandle, int vboVerticesHandle,
    // index
            int vboIndicesHandle, int indicesType, int indexElemSize, int indicesCount, int indexOffset,
            // attrib pos
            int colorAttrib, int positionAttrib, int textureAttrib, int textureId) {

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
        this.textureAttrib = textureAttrib;

        this.textureId = textureId;
    }

    @Override
    public void draw() {
        // Bind the texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL30.glBindVertexArray(vaoHandle);
        GL20.glEnableVertexAttribArray(colorAttrib);
        GL20.glEnableVertexAttribArray(positionAttrib);
        GL20.glEnableVertexAttribArray(textureAttrib);
        // Bind to the index VBO that has all the information about the order of the vertices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        // Draw the vertices
        GL11.glDrawElements(indicesType, indicesCount, indexElemSize, indexOffset);
        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(colorAttrib);
        GL20.glDisableVertexAttribArray(positionAttrib);
        GL20.glDisableVertexAttribArray(textureAttrib);
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
