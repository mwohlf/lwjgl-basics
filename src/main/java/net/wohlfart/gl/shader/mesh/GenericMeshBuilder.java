package net.wohlfart.gl.shader.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>GenericMeshBuilder class.</p>
 */
public class GenericMeshBuilder implements GenericMeshData {
    /** Constant <code>LOGGER</code> */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GenericMeshBuilder.class);

    private final String name;
    private final List<Vector3f> vertices = new ArrayList<Vector3f>();
    private final List<Vector3f> normals = new ArrayList<Vector3f>();
    private final List<Integer> indices = new ArrayList<Integer>();

    private int vaoHandle;
    private int vboVerticesHandle;
    private int vboIndicesHandle;


    /**
     * <p>Constructor for GenericMeshBuilder.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public GenericMeshBuilder(String name) {
        this.name = name;
    }


    /**
     * <p>build.</p>
     *
     * @return a {@link net.wohlfart.gl.renderer.Renderable} object.
     */
    public Renderable build() {
        vaoHandle = GL30.glGenVertexArrays();

        GL30.glBindVertexArray(vaoHandle);
        vboVerticesHandle = createVboVerticesHandle();
        vboIndicesHandle = createVboIndicesHandle();
        GL30.glBindVertexArray(0);

        // TODO: return whatever the data permits e.g. a wire mesh if we have wires here or a real mesh...
        return new GenericMesh(this);
    }



    /**
     * <p>addNormal.</p>
     *
     * @param x a float.
     * @param y a float.
     * @param z a float.
     */
    public void addNormal(float x, float y, float z) {
        normals.add(new Vector3f(x,y,z));
    }

    /**
     * <p>addVertex.</p>
     *
     * @param x a float.
     * @param y a float.
     * @param z a float.
     */
    public void addVertex(float x, float y, float z) {
        vertices.add(new Vector3f(x,y,z));
    }

    /**
     * <p>addVertexIndex.</p>
     *
     * @param integer a int.
     */
    public void addVertexIndex(int integer) {
        indices.add(integer);
    }


    private int createVboIndicesHandle() {
        final int[] buffer = getIndices();
        final int vboIndicesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        final IntBuffer indicesBuffer = BufferUtils.createIntBuffer(buffer.length);
        indicesBuffer.put(buffer);
        indicesBuffer.flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        return vboIndicesHandle;
    }

    private int createVboVerticesHandle() {
        final float[] floatBuff = getVertices();
        final int vboVerticesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
        verticesBuffer.put(floatBuff);
        verticesBuffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        final int positionAttrib = ShaderAttributeHandle.POSITION.getLocation();
        GL20.glEnableVertexAttribArray(positionAttrib);
        GL20.glVertexAttribPointer(positionAttrib, ShaderAttributeHandle.POSITION.getSize(), GL11.GL_FLOAT, false, 0, 0);
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

    /**
     * <p>Getter for the field <code>vaoHandle</code>.</p>
     *
     * @return a int.
     */
    @Override
    public int getVaoHandle() {
        return vaoHandle;
    }

    /**
     * <p>Getter for the field <code>vboVerticesHandle</code>.</p>
     *
     * @return a int.
     */
    @Override
    public int getVboVerticesHandle() {
        return vboVerticesHandle;
    }

    /**
     * <p>Getter for the field <code>vboIndicesHandle</code>.</p>
     *
     * @return a int.
     */
    @Override
    public int getVboIndicesHandle() {
        return vboIndicesHandle;
    }

    /**
     * <p>getLineWidth.</p>
     *
     * @return a int.
     */
    @Override
    public int getLineWidth() {
        return 3;
    }

    /**
     * <p>getIndicesType.</p>
     *
     * @return a int.
     */
    @Override
    public int getIndicesType() {
        //return GL11.GL_LINE_LOOP;
        return GL11.GL_TRIANGLES;
    }

    /**
     * <p>getIndexElemSize.</p>
     *
     * @return a int.
     */
    @Override
    public int getIndexElemSize() {
        return GL11.GL_UNSIGNED_INT;
    }

    /**
     * <p>getIndicesCount.</p>
     *
     * @return a int.
     */
    @Override
    public int getIndicesCount() {
        return indices.size();
    }

    /**
     * <p>getVerticesCount.</p>
     *
     * @return a int.
     */
    public int getVerticesCount() {
        return vertices.size();
    }

    /**
     * <p>getIndexOffset.</p>
     *
     * @return a int.
     */
    @Override
    public int getIndexOffset() {
        return 0;
    }

    /**
     * <p>getColor.</p>
     *
     * @return a {@link org.lwjgl.util.ReadableColor} object.
     */
    @Override
    public ReadableColor getColor() {
        return Color.RED;
    }


}
