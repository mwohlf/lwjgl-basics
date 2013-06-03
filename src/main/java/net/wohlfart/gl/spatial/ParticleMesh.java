package net.wohlfart.gl.spatial;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ParticleMesh implements IsRenderable {

    private final int vaoHandle;
    private final int vboHandle;

    private final int indicesCount;

    ParticleMesh(int vaoHandle, int vboHandle, int indicesCount) {
        this.vaoHandle = vaoHandle;
        this.vboHandle = vboHandle;
        this.indicesCount = indicesCount;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);

        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.COLOR.getLocation());

        GL11.glDrawArrays(GL11.GL_POINTS, 0, indicesCount);

        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.COLOR.getLocation());

        GL30.glBindVertexArray(0);
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboHandle);

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }

}
