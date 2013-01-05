package net.wohlfart.gl.shader.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;


// see: http://code.google.com/p/open-docs/wiki/LWJGL_5_Weitere_Vielecke
public class IndexedLinesMesh implements IMeshData {

    private final int vaoHandle;
    private final int vboVerticesHandle;
    private final int vboIndicesHandle;
	private final int indexType;

    private final ReadableColor color;  // single color
    private final int colorAttrib;
    private final int indicesCount;
    private final int indexOffset;
	private final Object indexSize;

    // package private, created by the builder
    IndexedLinesMesh(int vaoHandle,
                  int vboVerticesHandle,

                  int vboIndicesHandle,
                  int indexType,
                  int indexSize,
                  int indicesCount,
                  int indexOffset,

                  int colorAttrib,
                  ReadableColor color) {

        this.vaoHandle = vaoHandle;
        this.vboVerticesHandle = vboVerticesHandle;
        this.vboIndicesHandle = vboIndicesHandle;
        this.indexType = indexType;                    // GL11.GL_LINE_STRIP, GL11.GL11.GL_LINES, GL11.GL_LINE_LOOP
        this.indexSize = indexSize;                    // GL11.GL_UNSIGNED_BYTE
        this.colorAttrib = colorAttrib;
        this.color = color;
        this.indicesCount = indicesCount;
        this.indexOffset = indexOffset;
    }

    @Override
    public void draw() {
        GL30.glBindVertexArray(vaoHandle);
		GL20.glDisableVertexAttribArray(colorAttrib);
		GL20.glVertexAttrib4f(colorAttrib,
				color.getRed()/255f,
				color.getGreen()/255f,
				color.getBlue()/255f,
				color.getAlpha()/255f);
        GL11.glDrawElements(indexType, indicesCount, GL11.GL_UNSIGNED_BYTE, indexOffset);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void dispose() {
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);

        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboIndicesHandle);

        // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboVerticesHandle);

        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }


}
