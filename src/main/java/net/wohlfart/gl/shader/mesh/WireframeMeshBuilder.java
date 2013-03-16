package net.wohlfart.gl.shader.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>WireframeMeshBuilder class.</p>
 *
 *
 *
 */
public class WireframeMeshBuilder {
    /** Constant <code>LOGGER</code> */
    protected static final Logger LOGGER = LoggerFactory.getLogger(WireframeMeshBuilder.class);

    private final List<Vector3f> vertices = new ArrayList<Vector3f>();

    private final List<Integer> indices = new ArrayList<Integer>();
    private int indicesStructure;
    private int indexElemSize;

    private float lineWidth = 1f;
    private ReadableColor color = ReadableColor.GREY;

    private Vector3f translation;
    private Quaternion rotation;

    /**
     * <p>build.</p>
     *
     * @return a {@link net.wohlfart.gl.shader.mesh.IRenderable} object.
     */
    public IsRenderable build() {

        applyRotationAndTranslation();

        final int vaoHandle = GL30.glGenVertexArrays();

        GL30.glBindVertexArray(vaoHandle);
        final int vboVerticesHandle = createVboHandle(getVertices(), ShaderAttributeHandle.POSITION);
        final int vboIndicesHandle = createElementArrayBuffer();

        GL30.glBindVertexArray(0);

        final int indicesCount = getIndices().length;
        final int colorAttrib = ShaderAttributeHandle.COLOR.getLocation();
        final int positionAttrib = ShaderAttributeHandle.POSITION.getLocation();
        final int textureAttrib = ShaderAttributeHandle.TEXTURE_COORD.getLocation();
        final int offset = 0;

        return new WireframeMesh(vaoHandle, vboVerticesHandle, vboIndicesHandle, indicesStructure, indexElemSize, indicesCount, offset, colorAttrib,
                positionAttrib, textureAttrib, color, lineWidth);
    }

    private void applyRotationAndTranslation() {
        if (rotation != null) {
            for (final Vector3f vec : vertices) {
                SimpleMath.mul(rotation, vec, vec);
            }
        }
        if (translation != null) {
            for (final Vector3f vec : vertices) {
                SimpleMath.add(translation, vec, vec);
            }
        }
    }

    private int createElementArrayBuffer() {
        final int vboIndicesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        // FIXME: check the vertex count and use a byte or short buffer here if the number of vertices is low enough
        final int[] buffer = getIndices();
        final IntBuffer indicesBuffer = BufferUtils.createIntBuffer(buffer.length);
        indicesBuffer.put(buffer);
        indicesBuffer.flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        return vboIndicesHandle;
    }

    private int createVboHandle(float[] floatBuff, final ShaderAttributeHandle attrHandle) {
        final int vboVerticesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
        verticesBuffer.put(floatBuff);
        verticesBuffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        final int positionAttrib = attrHandle.getLocation();
        GL20.glEnableVertexAttribArray(positionAttrib);
        GL20.glVertexAttribPointer(positionAttrib, attrHandle.getSize(), GL11.GL_FLOAT, false, 0, 0);
        return vboVerticesHandle;
    }

    private float[] getVertices() {
        final int posSize = ShaderAttributeHandle.POSITION.getSize();
        if (posSize < 4) {
            throw new IllegalArgumentException("vertex position size should be 4");
        }
        final float[] result = new float[vertices.size() * ShaderAttributeHandle.POSITION.getSize()];
        int i = 0;
        for (final Vector3f v : vertices) {
            result[i++] = v.x;
            result[i++] = v.y;
            result[i++] = v.z;
            result[i++] = 1f;
        }
        return result;
    }

    private int[] getIndices() {
        final int[] result = new int[indices.size()];
        int i = 0;
        for (final Integer b : indices) {
            result[i++] = b;
        }
        return result;
    }

    // --

    /**
     * <p>Setter for the field <code>vertices</code>.</p>
     *
     * @param vertices a {@link java.util.List} object.
     */
    public void setVertices(final List<Vector3f> vertices) {
        this.vertices.addAll(vertices);
    }

    /**
     * <p>Setter for the field <code>indices</code>.</p>
     *
     * @param indices an array of {@link java.lang.Integer} objects.
     */
    public void setIndices(final Integer[] indices) {
        this.indices.addAll(Arrays.asList(indices));
    }

    /**
     * <p>Setter for the field <code>rotation</code>.</p>
     *
     * @param quaternion a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    public void setRotation(final Quaternion quaternion) {
        this.rotation = quaternion;
    }

    /**
     * <p>Setter for the field <code>translation</code>.</p>
     *
     * @param translation a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void setTranslation(final Vector3f translation) {
        this.translation = translation;
    }

    /**
     * <p>Setter for the field <code>color</code>.</p>
     *
     * @param color a {@link org.lwjgl.util.ReadableColor} object.
     */
    public void setColor(final ReadableColor color) {
        this.color = color;
    }

    /**
     * <p>Setter for the field <code>lineWidth</code>.</p>
     *
     * @param lineWidth a float.
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * <p>Setter for the field <code>indicesStructure</code>.</p>
     *
     * @param indicesStructure a int.
     */
    public void setIndicesStructure(int indicesStructure) {
        this.indicesStructure = indicesStructure;
    }

    /**
     * <p>Setter for the field <code>indexElemSize</code>.</p>
     *
     * @param indexElemSize a int.
     */
    public void setIndexElemSize(int indexElemSize) {
        this.indexElemSize = indexElemSize;
    }

}
