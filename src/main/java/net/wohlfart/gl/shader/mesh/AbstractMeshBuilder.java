package net.wohlfart.gl.shader.mesh;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import net.wohlfart.tools.PNGDecoder;
import net.wohlfart.tools.PNGDecoder.Format;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class contains only tool methods for building a mesh,
 * the build method needs to be implemented by a concrete subclass
 */
public abstract class AbstractMeshBuilder implements IMeshBuilder {
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractMeshBuilder.class);

    // FIXME: ugly hack to keep the same textureID, we need a texture repository
    protected static int texHandle = 0;

    private final Quaternion initRotation = Quaternion.setIdentity(new Quaternion());
    private final Vector3f initTranslation = new Vector3f(0,0,0);

    protected int createTextureHandle(String filename, int textureUnit) {
        int handle = 0;

        if (texHandle != 0) {
            return texHandle;
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
            handle = GL11.glGenTextures();
            GL13.glActiveTexture(textureUnit);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);

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
        AbstractMeshBuilder.texHandle = handle; // whatever we resolve first is used anywhere
        return handle;
    }

    protected int createIdxBufferHandle(int[] indices) {
        final IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        final int idxBufferHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idxBufferHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        return idxBufferHandle;
    }

    protected int createIdxBufferHandle(byte[] indices) {
        final ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        final int idxBufferHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idxBufferHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        return idxBufferHandle;
    }

    protected int createVboHandle(float[] floatBuff) {
        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
        verticesBuffer.put(floatBuff);
        verticesBuffer.flip();
        final int vboVerticesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        return vboVerticesHandle;
    }

    protected void applyRotationAndTranslation(List<Vector3f> vertices) {
        if (initRotation != null) {
            for (final Vector3f vec : vertices) {
                SimpleMath.mul(initRotation, vec, vec);
            }
        }
        if (initTranslation != null) {
            for (final Vector3f vec : vertices) {
                SimpleMath.add(initTranslation, vec, vec);
            }
        }
    }

    public void setInitRotation(Quaternion rotation) {
        this.initRotation.set(rotation);
    }

    public void setInitTranslation(Vector3f translation) {
        this.initTranslation.set(translation);
    }

    public void setTexHandle(int textHandle) {
        AbstractMeshBuilder.texHandle = textHandle;
    }

}
