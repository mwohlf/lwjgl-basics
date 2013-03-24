package net.wohlfart.gl.antlr4;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ModelMeshBuilder {

    private byte[] indices;
    private int triangelPrimitive;
    private float[] stream;

    public void setTrianglePrimitive(int triangelPrimitive) {
        this.triangelPrimitive = triangelPrimitive;
    }

    public void setIndices(byte[] indices) {
        this.indices = indices;
    }

    public void setVertexStream(float[] stream) {
        this.stream = stream;
    }

    public IsRenderable build() {

        final int vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());

        final int vboHandle = createVboHandle(stream);


        int offset;
        int stride = ShaderAttributeHandle.POSITION.getByteCount()
                + ShaderAttributeHandle.NORMAL.getByteCount()
                + ShaderAttributeHandle.TEXTURE_COORD.getByteCount();

        stride = 0;

        offset = 0;
        GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(),
                ShaderAttributeHandle.POSITION.getFloatCount(), GL11.GL_FLOAT, false, stride, offset);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // offset += ShaderAttributeHandle.POSITION.getByteCount();
        // GL20.glEnableVertexAttribArray(ShaderAttributeHandle.NORMAL.getLocation());
        // GL20.glVertexAttribPointer(ShaderAttributeHandle.NORMAL.getLocation(),
        //        ShaderAttributeHandle.NORMAL.getFloatCount(), GL11.GL_FLOAT, false, stride, offset);
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.NORMAL.getLocation());


        // offset += ShaderAttributeHandle.NORMAL.getByteCount();
        // GL20.glEnableVertexAttribArray(ShaderAttributeHandle.TEXTURE_COORD.getLocation());
        // GL20.glVertexAttribPointer(ShaderAttributeHandle.TEXTURE_COORD.getLocation(),
        //         ShaderAttributeHandle.TEXTURE_COORD.getFloatCount(), GL11.GL_FLOAT, false, stride, offset);
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.TEXTURE_COORD.getLocation());

        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.COLOR.getLocation());

        GL30.glBindVertexArray(0);


        final int idxBufferHandle = createIdxBufferHandle(indices);


        final int indexElemSize = GL11.GL_UNSIGNED_BYTE;
        final int indicesCount = indices.length;

        return new ModelMesh(vaoHandle, vboHandle, idxBufferHandle,
                triangelPrimitive, indexElemSize, indicesCount);
    }

    private int createIdxBufferHandle(byte[] indices) {
        final ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        final int idxBufferHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idxBufferHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        return idxBufferHandle;
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
