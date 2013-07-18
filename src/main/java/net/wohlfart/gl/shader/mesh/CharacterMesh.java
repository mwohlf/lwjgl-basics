package net.wohlfart.gl.shader.mesh;

import net.wohlfart.basic.elements.IsRenderable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

/**
 * <p>
 * CharacterMesh class.
 * </p>
 */
public class CharacterMesh implements IsRenderable {

    private final int vaoHandle;
    private final int indicesCount;
    private final int indexOffset;
    private final int indexElemSize;
    private final int indicesType;
    private final int texHandle;


    public CharacterMesh(int vaoHandle, int indicesType, int indexElemSize, int indicesCount, int indexOffset, int textureId) {
        this.vaoHandle = vaoHandle;
        this.indicesType = indicesType;
        this.indexElemSize = indexElemSize;
        this.indicesCount = indicesCount;
        this.indexOffset = indexOffset;
        this.texHandle = textureId;
    }

    @Override
    public void render() {
        // Bind the texture
        GL30.glBindVertexArray(vaoHandle);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texHandle);
        GL11.glDrawElements(indicesType, indicesCount, indexElemSize, indexOffset);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }

}
