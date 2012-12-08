package net.wohlfart.gl.elements;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import net.wohlfart.tools.Pool;
import net.wohlfart.tools.Pool.IPoolable;


// see: http://lwjgl.blogspot.de/2012/04/chapter-one-triangle.html
public class Triangle implements IDrawable, IPoolable {

	private static final Pool<Triangle> pool = new Pool<Triangle>() {
		@Override
		protected Triangle newObject () {
			return new Triangle();
		}
	};

	private int vaoId;   // Vertex Array Object  
	private int vboId;   // Our Vertex Buffer Object 

	private int vertexCount;
	private int indicesCount;


	/**
	 * only callec created by the pool
	 */
	private Triangle() {}

	/**
	 * @return a new object possibly from the pool
	 */
	public static Triangle create() {
		return create( new Vector3f(+0.0f,+0.5f,+0.0f),
				       new Vector3f(-0.5f,-0.5f,+0.0f),
				       new Vector3f(+0.5f,-0.5f,+0.0f));
	}

	public static Triangle create(final Vector3f tm,
            				      final Vector3f bl,
                                  final Vector3f br) {
		Triangle result = pool.obtain();

		float[] vertices = new float[] {
				tm.x, tm.y, tm.z,
				bl.x, bl.y, bl.z,
				br.x, br.y, br.z};
		result.vertexCount = 3;
		
		byte[] indices = {0, 1, 2};
		result.indicesCount = indices.length;


		// Create a new Vertex Array Object in memory and select it (bind)
		// A VAO can have up to 16 attributes (VBO's) assigned to it by default
		result.vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(result.vaoId);

		// Sending data to OpenGL requires the usage of (flipped) byte buffers
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(result.vertexCount * 3);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();

		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(result.indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		// Create a new VBO for the indices and select it (bind)
		result.vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, result.vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);		// Deselect (bind to 0) the VBO
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Create a new VBO for the indices and select it (bind)
		result.vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, result.vboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		return result;
	}

	/**
	 * free the object for reuse
	 */
	public void free() {
		pool.free(this);
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
		GL15.glDeleteBuffers(vboId);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboId);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
	}



	@Override
	public void draw() {
		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);

		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);

		// Draw the vertices
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);

		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
