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

public class GenericMeshBuilder {
    protected static final Logger LOGGER = LoggerFactory.getLogger(WireframeMeshBuilder.class);

    private final String name;
    private final List<Vector3f> vertices = new ArrayList<Vector3f>();
    private final List<Vector3f> normals = new ArrayList<Vector3f>();
    private final List<Integer> indices = new ArrayList<Integer>();

    private int vaoHandle;
    private int vboVerticesHandle;
    private int vboIndicesHandle;


    public GenericMeshBuilder(String name) {
        this.name = name;
    }


    public Renderable build() {
        vaoHandle = GL30.glGenVertexArrays();

        GL30.glBindVertexArray(vaoHandle);
        vboVerticesHandle = createVboVerticesHandle();
        vboIndicesHandle = createVboIndicesHandle();

        GL30.glBindVertexArray(0);


        // TODO: return whatever the data permist e.g. a wire mesh if we have wires here or a real mesh...
        return new GenericMesh(this);
    }



    public void addNormal(float x, float y, float z) {
        normals.add(new Vector3f(x,y,z));
    }

    public void addVertex(float x, float y, float z) {
        vertices.add(new Vector3f(x,y,z));
    }

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

    public int getVaoHandle() {
        return vaoHandle;
    }

    public int getVboVerticesHandle() {
        return vboVerticesHandle;
    }

    public int getVboIndicesHandle() {
        return vboIndicesHandle;
    }

    public int getLineWidth() {
        return 3;
    }

    public int getIndicesType() {
        //return GL11.GL_LINE_LOOP;
        return GL11.GL_TRIANGLES;
    }

    public int getIndexElemSize() {
        return GL11.GL_UNSIGNED_INT;
    }

    public int getIndicesCount() {
        return indices.size();
    }

    public int getVerticesCount() {
        return vertices.size();
    }

    public int getIndexOffset() {
        return 0;
    }

    public ReadableColor getColor() {
        return Color.RED;
    }


}
