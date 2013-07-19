package net.wohlfart.gl.shader.mesh;

import java.nio.FloatBuffer;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.texture.TextureRegistry;
import net.wohlfart.gl.shader.ShaderAttributeHandle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class ParticleMesh implements IsRenderable {

    private final int vaoHandle;
    private final int verticesCount;
    private final int primitive;
    private final int textureId;


    private ParticleMesh(int vaoHandle, int verticesCount, int primitive, int textureId) {
        this.vaoHandle = vaoHandle;
        this.verticesCount = verticesCount;
        this.primitive = primitive;
        this.textureId = textureId;
    }

    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glDrawArrays(primitive, 0, verticesCount);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }

    public static class Builder extends AbstractMeshBuilder {

        private String textureFilename;
        private Integer texId;

        private FloatBuffer verticesBuffer;
        private int count;

        public void setTextureFilename(String textureFilename) {
            this.textureFilename = textureFilename;
        }

        public void setTextureId(int texId) {
            this.texId = texId;
        }

        public void setParticleCount(int count) {
            this.count = count;
        }

        public void setFlippedFloatBuffer(FloatBuffer verticesBuffer) {
            this.verticesBuffer = verticesBuffer;
        }

        @Override
        public IsRenderable build() {
            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);

           // load the texture if needed
            if (textureFilename != null) {
                texId  = TextureRegistry.TEXTURE_REGISTRY.getTextureHandle(textureFilename, GL13.GL_TEXTURE0);
            }

            // FIXME: use createStream
            final int vboHandle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboHandle);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

            final int[] offset = {0};
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                    + ShaderAttributeHandle.COLOR.getByteCount()
                    + ShaderAttributeHandle.NORMAL.getByteCount()
                    + ShaderAttributeHandle.TEXTURE_COORD.getByteCount()
                    ;
            ShaderAttributeHandle.POSITION.enable(stride, offset);
            ShaderAttributeHandle.COLOR.enable(stride, offset);
            ShaderAttributeHandle.NORMAL.enable(stride, offset);
            ShaderAttributeHandle.TEXTURE_COORD.enable(stride, offset);

            GL30.glBindVertexArray(0);

            return new ParticleMesh(vaoHandle, count * 6, GL11.GL_TRIANGLES, texId);
        }

    }

}
