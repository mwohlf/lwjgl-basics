package net.wohlfart.basic;

import java.nio.FloatBuffer;

import net.wohlfart.gl.CanMove;
import net.wohlfart.gl.CanMoveImpl;
import net.wohlfart.gl.CanRotate;
import net.wohlfart.gl.CanRotateImpl;
import net.wohlfart.gl.IState;
import net.wohlfart.gl.shader.SimpleShader;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMatrix4f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class SimpleState implements IState {

	private int vertexCount;
	private int vaoId;
	private int vboId;
	private SimpleShader shader;

	private CanMoveImpl canMove = new CanMoveImpl();
	private CanRotateImpl canRotate = new CanRotateImpl();
	private Avatar avatar = new Avatar(canRotate, canMove);

	@Override
	public void setup(final IGameContext gameContext) {

		shader = new SimpleShader();
		shader.init();
		shader.setProjectionMatrix(gameContext.createProjectionMatrix());
		shader.setViewMatrix(new Matrix4f());
		shader.setModelMatrix(new Matrix4f());


		// OpenGL expects vertices to be defined counter clockwise by default
		float[] vertices = {
				// Left bottom triangle
				 -0.5f,  0.5f, 0f,
				 -0.5f, -0.5f, 0f,
				  0.5f, -0.5f, 0f,

				 // Right top triangle
				  0.5f, -0.5f, 0f,
				  0.5f,  0.5f, 0f,
				 -0.5f,  0.5f, 0f
		};

		// Sending data to OpenGL requires the usage of (flipped) byte buffers
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();

		vertexCount = 6;

		// Create a new Vertex Array Object in memory and select it (bind)
		// A VAO can have up to 16 attributes (VBO's) assigned to it by default
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		// A VBO is a collection of Vectors which in this case resemble the location of each vertex.
		vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
	}


	@Override
	public void update(float tpf) {

		// rotate the view
		Matrix4f viewMatrix = SimpleMatrix4f.create(canRotate);
		shader.setViewMatrix(viewMatrix);


		// move the object
		Matrix4f modelMatrix = SimpleMatrix4f.create(canMove);
		shader.setModelMatrix(modelMatrix);
	}


	@Override
	public void render() {

		shader.bind();

		// Bind to the VAO that has all the information about the quad vertices
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);

		// Draw the vertices
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

		// Put everything back to default (deselect)
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		shader.unbind();
	}


	@Override
	public boolean isDone() {
		return Display.isCloseRequested();
	}



	@Override
	public void teardown(IGameContext gameContext) {
		// nothing to do yet
	}

}
