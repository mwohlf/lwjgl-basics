package net.wohlfart.gl.elements;

import java.nio.FloatBuffer;

import net.wohlfart.gl.shader.IShader;
import net.wohlfart.tools.Pool;
import net.wohlfart.tools.Pool.IPoolable;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;


// see: http://lwjgl.blogspot.de/2012/04/chapter-one-triangle.html
public class Triangle implements IDrawable, IPoolable {
	private static final int VECTOR_SIZE = 4;

	private static final Pool<Triangle> pool = new Pool<Triangle>() {
		@Override
		protected Triangle newObject () {
			return new Triangle();
		}
	};

	private IShader shader;
	// A VAO can have up to 16 attributes (VBO's) assigned to it by default
	private int vaoHandle;   // Vertex Array Object
	private int vboHandle;   // Vertex Buffer Object

	private int vertexCount;

	/**
	 * only called by the pool
	 */
	private Triangle() {}

	/**
	 * @return a new object possibly from the pool
	 */
	public static Triangle create(final IShader shader) {
		return create( shader,
				       new Vector4f(+0.0f,+0.5f,+0.0f,+1.0f),
				       new Vector4f(-0.5f,-0.5f,+0.0f,+1.0f),
				       new Vector4f(+0.5f,-0.5f,+0.0f,+1.0f));
	}

	public static Triangle create(final IShader shader,
			                      final Vector4f tm,
            				      final Vector4f bl,
                                  final Vector4f br) {
		final Triangle result = pool.obtain();

		result.shader = shader;

		float[] vertices = new float[] {
				tm.x, tm.y, tm.z, tm.w,
				bl.x, bl.y, bl.z, tm.w,
				br.x, br.y, br.z, tm.w};
		result.vertexCount = 3;

		// create a VAO in memory
		result.vaoHandle = GL30.glGenVertexArrays();
		// select/bind the VAO
		GL30.glBindVertexArray(result.vaoHandle);

		// create a VBO in the VAO
		result.vboHandle = GL15.glGenBuffers();
		// select/bind the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, result.vboHandle);
		// create & flip a buffer for the vertices
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(result.vertexCount * VECTOR_SIZE);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();
		// set the size and data of the VBO and set it to STATIC_DRAW
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

		// assign vertex VBO to slot 0 of the VAO
		GL20.glVertexAttribPointer(shader.getInPosition(), VECTOR_SIZE, GL11.GL_FLOAT, false, 0, 0);

		// unbind VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		// disable VAO
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
		GL15.glDeleteBuffers(vboHandle);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoHandle);
	}



	@Override
	public void draw() {
		// bind to the VAO
		GL30.glBindVertexArray(vaoHandle);
		GL20.glEnableVertexAttribArray(shader.getInPosition());
		// draw some triangles, startIndex: 0, count
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
		GL20.glDisableVertexAttribArray(0);
		// unbind the VAO
		GL30.glBindVertexArray(0);
	}

}
