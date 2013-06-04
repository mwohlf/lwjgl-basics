package net.wohlfart.gl.spatial;

import java.nio.FloatBuffer;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorPointsMeshBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ColorPointsMeshBuilder.class);

    private float[] stream;
    private int count;


    public void setParticleCount(int count) {
        this.count = count;
    }

    public void setVertexStream(float[] stream) {
        this.stream = stream;
    }

    public IsRenderable build() {

        final int vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        final int vboHandle = createVboHandle(stream);

        int offset;  // @formatter:off
        final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                         + ShaderAttributeHandle.COLOR.getByteCount();

        offset = 0;
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());
        GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(),
                                   ShaderAttributeHandle.POSITION.getFloatCount(), GL11.GL_FLOAT, false, stride, offset);

        offset += ShaderAttributeHandle.POSITION.getByteCount();
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.COLOR.getLocation());
        GL20.glVertexAttribPointer(ShaderAttributeHandle.COLOR.getLocation(),
                                   ShaderAttributeHandle.COLOR.getFloatCount(), GL11.GL_FLOAT, false, stride, offset);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0); // @formatter:on

        LOGGER.debug("bulding a new mesh with {} elements", count);
        return new ColorPointsMesh(vaoHandle, vboHandle, count, GL11.GL_POINTS);
    }


    private int createVboHandle(float[] floatBuff) {
        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
        verticesBuffer.put(floatBuff);
        verticesBuffer.flip();
        final int vboVerticesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        return vboVerticesHandle;
    }



}
