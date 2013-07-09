package net.wohlfart.gl.elements.debug;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;

/**
 * <p>
 * A Generic WireframeMesh class.
 * </p>
 */
public class WireframeMesh implements IsRenderable {

    private final int vaoHandle;
    private final int indicesCount;
    private final int indexOffset;
    private final int indexElemSize;
    private final int linePrimitive;

    private final ReadableColor color;


    WireframeMesh(int vaoHandle, int linePrimitive, int indexElemSize, int indicesCount, int indexOffset, ReadableColor color) {
        this.vaoHandle = vaoHandle;
        this.linePrimitive = linePrimitive; // GL11.GL_LINE_STRIP, GL11.GL11.GL_LINES, GL11.GL_LINE_LOOP
        this.indexElemSize = indexElemSize; // GL11.GL_UNSIGNED_BYTE
        this.indicesCount = indicesCount;
        this.indexOffset = indexOffset;
        this.color = color;
    }

    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL20.glVertexAttrib4f(ShaderAttributeHandle.COLOR.getLocation(),
                color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        GL11.glDrawElements(linePrimitive, indicesCount, indexElemSize, indexOffset);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }

}
