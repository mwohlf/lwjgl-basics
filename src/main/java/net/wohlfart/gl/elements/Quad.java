package net.wohlfart.gl.elements;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.shader.IShader;
import net.wohlfart.tools.Pool;
import net.wohlfart.tools.Pool.IPoolable;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;


// read: http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
// and: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
public class Quad implements IDrawable, IPoolable {

	private static final Pool<Quad> pool = new Pool<Quad>() {
		@Override
		protected Quad newObject () {
			return new Quad();
		}
	};

	private IShader shader;
	private int vaoHandle;
	private int vboHandle;

	private int vertexCount;
	private int indicesCount;


	/**
	 * only called/created by the pool
	 */
	private Quad() {}

	/**
	 * @return a new object possibly from the pool
	 */
	public static Quad create(final IShader shader) {
		return create( shader,
			           new Vector3f(+0.5f,+0.5f,+0.0f),
				       new Vector3f(-0.5f,+0.5f,+0.0f),
				       new Vector3f(-0.5f,-0.5f,+0.0f),
				       new Vector3f(+0.5f,-0.5f,+0.0f));
	}

	public static Quad create(final IShader shader,
                              final Vector3f tr,
			                  final Vector3f tl,
			                  final Vector3f bl,
			                  final Vector3f br) {
		Quad result = pool.obtain();

		result.shader = shader;


		float[] vertices = new float[] {
				tr.x, tr.y, tr.z,
				tl.x, tl.y, tl.z,
				bl.x, bl.y, bl.z,
				br.x, br.y, br.z};
		result.vertexCount = 4;

		byte[] indices = {
				// top left
				0, 1, 2,
				// bottom right
				2, 3, 0};
		result.indicesCount = indices.length;

		// Create a new Vertex Array Object in memory and select it (bind)
		// A VAO can have up to 16 attributes (VBO's) assigned to it by default
		result.vaoHandle = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(result.vaoHandle);

		// Sending data to OpenGL requires the usage of (flipped) byte buffers
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(result.vertexCount * 3);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();

		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(result.indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		// Create a new VBO for the indices and select it (bind)
		result.vboHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, result.vboHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);		// Deselect (bind to 0) the VBO
		// assign vertex VBO to slot 0 of the VAO
		GL20.glVertexAttribPointer(shader.getInPosition(), 3, GL11.GL_FLOAT, false, 0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Create a new VBO for the indices and select it (bind)
		result.vboHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, result.vboHandle);
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
		GL15.glDeleteBuffers(vboHandle);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboHandle);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoHandle);
	}



	@Override
	public void draw() {
		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vaoHandle);
		GL20.glEnableVertexAttribArray(0);

		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboHandle);

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
