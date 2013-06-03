package net.wohlfart.gl.spatial;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.tools.PNGDecoder;
import net.wohlfart.tools.PNGDecoder.Format;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelMeshBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelMeshBuilder.class);

    private int[] indices;
    private int triangelPrimitive;
    private float[] stream;

    public void setTrianglePrimitive(int triangelPrimitive) {
        this.triangelPrimitive = triangelPrimitive;
    }

    public void setIndices(int[] indices) {
        // TODO: create a byte/short array depending on the size of the indices
        this.indices = indices;
    }

    public void setVertexStream(float[] stream) {
        this.stream = stream;
    }

    public IsRenderable build() {

        final int vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        final int textureHandle = createTextureHandle("/models/cube/cube.png", GL13.GL_TEXTURE0);

        final int vboHandle = createVboHandle(stream);

        int offset;
        final int stride = ShaderAttributeHandle.POSITION.getByteCount() + ShaderAttributeHandle.NORMAL.getByteCount()
                + ShaderAttributeHandle.TEXTURE_COORD.getByteCount();

        offset = 0;
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());
        GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(), ShaderAttributeHandle.POSITION.getFloatCount(), GL11.GL_FLOAT, false, stride,
                offset);

        offset += ShaderAttributeHandle.POSITION.getByteCount();
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.NORMAL.getLocation());
        GL20.glVertexAttribPointer(ShaderAttributeHandle.NORMAL.getLocation(), ShaderAttributeHandle.NORMAL.getFloatCount(), GL11.GL_FLOAT, false, stride,
                offset);

        offset += ShaderAttributeHandle.NORMAL.getByteCount();
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.TEXTURE_COORD.getLocation());
        GL20.glVertexAttribPointer(ShaderAttributeHandle.TEXTURE_COORD.getLocation(), ShaderAttributeHandle.TEXTURE_COORD.getFloatCount(), GL11.GL_FLOAT,
                false, stride, offset);

        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.COLOR.getLocation());

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        final int idxBufferHandle = createIdxBufferHandle(indices);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        final int indexElemSize = GL11.GL_UNSIGNED_INT;
        final int indicesCount = indices.length;

        return new ModelMesh(vaoHandle, vboHandle, idxBufferHandle, textureHandle, triangelPrimitive, indexElemSize, indicesCount);
    }

    private int createIdxBufferHandle(int[] indices) {
        final IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        final int idxBufferHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idxBufferHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
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

    // FIXME: ugly hack to keep the same textureID
    private static int staticTextId = 0;

    private int createTextureHandle(String filename, int textureUnit) {
        int texId = 0;

        if (staticTextId != 0) {
            return staticTextId;
        }

        // InputStream inputStream = new FileInputStream(filename);
        try (InputStream inputStream = ClassLoader.class.getResourceAsStream(filename);) {

            // Link the PNG decoder to this stream
            final PNGDecoder decoder = new PNGDecoder(inputStream);
            // Get the width and height of the texture
            final int tWidth = decoder.getWidth();
            final int tHeight = decoder.getHeight();
            // Decode the PNG file in a ByteBuffer
            final ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
            buffer.flip();

            // Create a new texture object in memory and bind it
            texId = GL11.glGenTextures();
            GL13.glActiveTexture(textureUnit);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
            // All RGB bytes are aligned to each other and each component is 1 byte
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            // Upload the texture data and generate mip maps (for scaling)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, tWidth, tHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            // Setup the ST coordinate system
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            // Setup what to do when the texture has to be scaled
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

        } catch (final FileNotFoundException ex) {
            LOGGER.error("can't load texture image", ex);
        } catch (final IOException ex) {
            LOGGER.error("can't load texture image", ex);
        }
        staticTextId = texId;
        return texId;
    }

}
