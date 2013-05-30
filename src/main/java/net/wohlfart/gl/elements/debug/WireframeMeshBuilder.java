package net.wohlfart.gl.elements.debug;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
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

/**
 * <p>
 * A Builder class for creating debug elements. for a scene.
 * </p>
 */
public class WireframeMeshBuilder {

    private final List<Vector3f> vertices = new ArrayList<Vector3f>();
    private final List<Integer> indices = new ArrayList<Integer>();
    private int linePrimitive;

    private ReadableColor color = ReadableColor.GREY;
    private Vector3f translation;
    private Quaternion rotation;

    /**
     * <p>
     * build.
     * </p>
     * 
     * @return a {@link net.wohlfart.gl.shader.mesh.IRenderable} object.
     */
    public IsRenderable build() {
        applyRotationAndTranslation();

        final int vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.COLOR.getLocation());
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.NORMAL.getLocation());
        final int vboHandle = createVboHandle(getVertices(), ShaderAttributeHandle.POSITION);
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.TEXTURE_COORD.getLocation());

        final int idxBufferHandle = createIdxBufferHandle(getIndices());

        GL30.glBindVertexArray(0);

        final int indexElemSize = GL11.GL_UNSIGNED_INT;
        final int indicesCount = getIndices().length;
        final int offset = 0;

        return new WireframeMesh(vaoHandle, vboHandle, idxBufferHandle, linePrimitive, indexElemSize, indicesCount, offset, color);
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

    private int createIdxBufferHandle(int[] indices) {
        final IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        final int idxBufferHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idxBufferHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        return idxBufferHandle;
    }

    private int createVboHandle(float[] floatBuff, final ShaderAttributeHandle attrHandle) {
        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
        verticesBuffer.put(floatBuff);
        verticesBuffer.flip();

        final int vboVerticesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        final int attribLocation = attrHandle.getLocation();
        GL20.glEnableVertexAttribArray(attribLocation);
        GL20.glVertexAttribPointer(attribLocation, attrHandle.getFloatCount(), GL11.GL_FLOAT, false, 0, 0);
        return vboVerticesHandle;
    }

    private float[] getVertices() {
        final int posSize = ShaderAttributeHandle.POSITION.getFloatCount();
        if (posSize < 4) {
            throw new IllegalArgumentException("vertex position size should be 4");
        }
        final float[] result = new float[vertices.size() * ShaderAttributeHandle.POSITION.getFloatCount()];
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

    /**
     * <p>
     * Setter for the field <code>vertices</code>.
     * </p>
     * 
     * @param vertices
     *            a {@link java.util.List} object.
     */
    public void setVertices(final List<Vector3f> vertices) {
        this.vertices.addAll(vertices);
    }

    /**
     * <p>
     * Setter for the field <code>indices</code>.
     * </p>
     * 
     * @param indices
     *            an array of {@link java.lang.Integer} objects.
     */
    public void setIndices(final List<Integer> indices) {
        this.indices.addAll(indices);
    }

    /**
     * <p>
     * Setter for the field <code>rotation</code>.
     * </p>
     * 
     * @param quaternion
     *            a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    public void setRotation(final Quaternion quaternion) {
        this.rotation = quaternion;
    }

    /**
     * <p>
     * Setter for the field <code>translation</code>.
     * </p>
     * 
     * @param translation
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void setTranslation(final Vector3f translation) {
        this.translation = translation;
    }

    /**
     * <p>
     * Setter for the field <code>color</code>.
     * </p>
     * 
     * @param color
     *            a {@link org.lwjgl.util.ReadableColor} object.
     */
    public void setColor(final ReadableColor color) {
        this.color = color;
    }

    /**
     * <p>
     * Setter for the field <code>linePrimitive</code> defines how the indices are turned into lines, valid values are GL_LINES, GL_LINE_STRIP, GL_LINE_LOOP.
     * </p>
     * 
     * @param linePrimitive
     *            a int.
     */
    public void setLinePrimitive(int linePrimitive) {
        this.linePrimitive = linePrimitive;
    }

}
