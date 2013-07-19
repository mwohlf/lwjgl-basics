package net.wohlfart.gl.shader.mesh;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import net.wohlfart.basic.texture.TextureRegistry;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
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

    private final Quaternion initRotation = Quaternion.setIdentity(new Quaternion());
    private final Vector3f initTranslation = new Vector3f(0,0,0);

    private String textureFilename;
    private int texHandle;

    // setters for clients

    public void setInitRotation(Quaternion rotation) {
        this.initRotation.set(rotation);
    }

    public void setInitTranslation(Vector3f translation) {
        this.initTranslation.set(translation);
    }

    public void setTexHandle(int texHandle) {
        this.texHandle = texHandle;
    }

    public void setTexFilename(String textureFilename) {
        this.textureFilename = textureFilename;
    }


    // tools for subclasses

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

    protected int resolveTexHandle() {
        if ((textureFilename == null) && (texHandle > 0)) {
            return texHandle;
        }
        if ((textureFilename != null) && (texHandle == 0)) {
            texHandle = TextureRegistry.TEXTURE_REGISTRY.getTextureHandle(textureFilename, GL13.GL_TEXTURE0);
            textureFilename = null;
            return texHandle;
        }
        throw new IllegalStateException("textureFilename is '" + textureFilename + "' and texHandle is '" + texHandle + "#");
    }

}
