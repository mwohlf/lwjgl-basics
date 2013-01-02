package net.wohlfart.gl.shader.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;

public class WireframeMesh implements IMeshData {

    private final int vaoHandle;
    private final int vboVerticesHandle;
    private final int vboIndicesHandle;

    private final ReadableColor color;  // single color
    private final int colorAttrib;
    private final int indicesCount;
    private final int indexOffset;

    // package private
    WireframeMesh(
    		      final int vaoHandle,
                  final int vboVerticesHandle,
                  final int vboIndicesHandle,
                  final ReadableColor color,
                  final int colorAttrib,
                  final int indicesCount) {

        this.vaoHandle = vaoHandle;
        this.vboVerticesHandle = vboVerticesHandle;
        this.vboIndicesHandle = vboIndicesHandle;
        this.color = color;
        this.colorAttrib = colorAttrib;
        this.indicesCount = indicesCount;
        this.indexOffset = 0;
    }

    @Override
    public void draw() {
        GL30.glBindVertexArray(vaoHandle);
		ReadableColor c = this.color==null?ReadableColor.GREY:this.color;
		GL20.glDisableVertexAttribArray(colorAttrib);
		GL20.glVertexAttrib4f(colorAttrib, c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);

        GL11.glDrawElements(GL11.GL_LINE_STRIP, indicesCount, GL11.GL_UNSIGNED_BYTE, indexOffset);
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
