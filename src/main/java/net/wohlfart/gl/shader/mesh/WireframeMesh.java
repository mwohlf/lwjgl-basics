package net.wohlfart.gl.shader.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;


// see: http://code.google.com/p/open-docs/wiki/LWJGL_5_Weitere_Vielecke
public class WireframeMesh implements IMesh {

    private final int vaoHandle;
    private final int vboVerticesHandle;
    private final int vboIndicesHandle;
    private final int indicesCount;
    private final int indexOffset;
	private final int indexElemSize;
	private final int indicesType;

    private final ReadableColor color;  // single color
    private final int colorAttrib;

	private final float lineWidth;

    // package private, created by the builder
    WireframeMesh(int vaoHandle,
                  int vboVerticesHandle,
                  // index
                  int vboIndicesHandle,
                  int indicesType,
                  int indexElemSize,
                  int indicesCount,
                  int indexOffset,
                  // color
                  int colorAttrib,
                  ReadableColor color,
                  // width
                  float lineWidth) {

        this.vaoHandle = vaoHandle;
        this.vboVerticesHandle = vboVerticesHandle;
        // index
        this.vboIndicesHandle = vboIndicesHandle;
        this.indicesType = indicesType;                    // GL11.GL_LINE_STRIP, GL11.GL11.GL_LINES, GL11.GL_LINE_LOOP
        this.indexElemSize = indexElemSize;                // GL11.GL_UNSIGNED_BYTE
        this.indicesCount = indicesCount;
        this.indexOffset = indexOffset;
        // color
        this.colorAttrib = colorAttrib;
        this.color = color;
        // width
        this.lineWidth = lineWidth;
    }


    @Override
    public void draw() {
        GL30.glBindVertexArray(vaoHandle);
		// wire width
		GL11.glLineWidth(lineWidth);
        // color is not indexed
		GL20.glDisableVertexAttribArray(colorAttrib);
		GL20.glVertexAttrib4f(colorAttrib,
				color.getRed()/255f,
				color.getGreen()/255f,
				color.getBlue()/255f,
				color.getAlpha()/255f);
		// vertices are indexed
        GL11.glDrawElements(indicesType, indicesCount, indexElemSize, indexOffset);
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
