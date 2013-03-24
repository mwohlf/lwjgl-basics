package net.wohlfart.gl.antlr4;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ModelMesh implements IsRenderable {

    private final int vaoHandle;
    private final int vboHandle;
    private final int idxBufferHandle;
    private final int indicesCount;
    private final int indexElemSize;
    private final int trianglePrimitive;

    ModelMesh(int vaoHandle, int vboHandle, int idxBufferHandle,
            int trianglePrimitive, int indexElemSize, int indicesCount) {

        this.vaoHandle = vaoHandle;
        this.vboHandle = vboHandle;
        this.idxBufferHandle = idxBufferHandle;
        this.trianglePrimitive = trianglePrimitive;
        this.indexElemSize = indexElemSize;
        this.indicesCount = indicesCount;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());

        // GL20.glEnableVertexAttribArray(0);
        // GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idxBufferHandle);

        GL11.glDrawElements(trianglePrimitive, indicesCount, indexElemSize, 0);
        //GL11.glDrawArrays(trianglePrimitive, 0, indicesCount/2);  // works

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());
        GL30.glBindVertexArray(0);
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);
        // Delete the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboHandle);
        // Delete the indexBuffer
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(idxBufferHandle);
        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }

}
