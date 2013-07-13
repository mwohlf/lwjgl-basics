package net.wohlfart.gl.shader.mesh;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * mesh for drawing multiple colored  dots
 */
public class SimpleMesh implements IsRenderable {

    private final int vaoHandle;
    private final int indicesCount;
    private final int primitive;

    private SimpleMesh(int vaoHandle, int indicesCount, int primitive) {
        this.vaoHandle = vaoHandle;
        this.indicesCount = indicesCount;
        this.primitive = primitive;
    }

    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL11.glDrawArrays(primitive, 0, indicesCount);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }



    public static class Builder extends AbstractMeshBuilder {
        private static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        private float[] stream;
        private int count;


        public void setParticleCount(int count) {
            this.count = count;
        }

        public void setVertexStream(float[] stream) {
            this.stream = stream;
        }

        @Override
        public IsRenderable build() {
            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);

            createVboHandle(stream);

            final int[] offset = {0};
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                             + ShaderAttributeHandle.COLOR.getByteCount()
                             ;
            ShaderAttributeHandle.POSITION.enable(stride, offset);
            ShaderAttributeHandle.COLOR.enable(stride, offset);
            ShaderAttributeHandle.TEXTURE_COORD.disable();
            ShaderAttributeHandle.NORMAL.disable();

            GL30.glBindVertexArray(0);

            LOGGER.debug("bulding a new mesh with {} elements", count);
            return new SimpleMesh(vaoHandle, count, GL11.GL_TRIANGLES);
        }

    }
}
