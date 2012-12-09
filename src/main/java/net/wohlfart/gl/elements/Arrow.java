package net.wohlfart.gl.elements;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;

import net.wohlfart.gl.shader.IShader;
import net.wohlfart.tools.Pool;
import net.wohlfart.tools.Pool.IPoolable;

public class Arrow implements IDrawable, IPoolable {
    private static final int VECTOR_SIZE = 4;

    private static final float[] vertices = new float[]{
        +0.00f, +0.00f, +0.00f, +1.00f, // base
        +0.00f, +0.00f, +1.00f, +1.00f, // tip
        +0.10f, +0.00f, +0.90f, +1.00f, // tip right
        -0.10f, +0.00f, +0.90f, +1.00f, // tip left
        +0.00f, +0.10f, +0.90f, +1.00f, // tip top
        +0.00f, -0.10f, +0.90f, +1.00f, // tip bottom
    };

    private static final byte[] indices = {
            0, 1,  // shaft
            2, 1,  // tip1
            3, 1,  // tip2
            4, 1,  // tip3
            5, 1,  // tip4
    };


    private static final Pool<Arrow> pool = new Pool<Arrow>() {
        @Override
        protected Arrow newObject () {
            return new Arrow();
        }
    };

    private IShader shader;
    private int vaoHandle;
    private int vboVerticesHandle;
    private int vboIndicesHandle;


    /**
     * only called/created by the pool
     */
    private Arrow() {}

    /**
     * @return a new object possibly from the pool
     */
    public static Arrow create(final IShader shader) {
        return create( shader,
                       new Vector4f(+0.5f,+0.5f,+0.0f,+1.0f));
    }

    public static Arrow create(final IShader shader,
                               final Vector4f vec) {
    	Arrow result = pool.obtain();
		result.shader = shader;

		// create a VAO in memory
		result.vaoHandle = GL30.glGenVertexArrays();
		// select/bind the VAO
		GL30.glBindVertexArray(result.vaoHandle);

		// create a VBO or the vertices in the VAO
		result.vboVerticesHandle = GL15.glGenBuffers();
		// select/bind the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, result.vboVerticesHandle);
		// create & flip a buffer for the vertices
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(Arrow.vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();
		// set the size and data of the VBO and set it to STATIC_DRAW
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        // create a new VBO for the indices
        result.vboIndicesHandle = GL15.glGenBuffers();
        // and select it
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, result.vboIndicesHandle);
        // setup the indices buffer
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(Arrow.indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        // push the data to the VBO
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(shader.getInPosition());
		// enable shader attribute
		GL20.glVertexAttribPointer(shader.getInPosition(), VECTOR_SIZE, GL11.GL_FLOAT, false, 0, 0);
;
		GL11.glLineWidth(3f);

		GL30.glBindVertexArray(0);

    	return result;
    }


	/**
	 * free the object for reuse
	 */
	public void free() {
		pool.free(this);
	}


	@Override
	public void draw() {
		// bind to the VAO
		GL30.glBindVertexArray(vaoHandle);
		// draw the line strips according to the indices
        GL11.glDrawElements(GL11.GL_LINE_STRIP, Arrow.indices.length, GL11.GL_UNSIGNED_BYTE, 0);
		// unbind the VAO
		GL30.glBindVertexArray(0);
	}


	/**
	 * called by the pool when the object is freed
	 */
	@Override
	public void reset() {
		// Disable the VBO index from the VAO attributes list
		GL20.glDisableVertexAttribArray(0);

		// Delete the vertex VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboIndicesHandle);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboVerticesHandle);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoHandle);
	}

}