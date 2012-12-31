package net.wohlfart.gl.shader.mesh;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.AttributeHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

// see: http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2005.html
public class VertexArrayObject implements IMeshData {

	private final int vaoHandle;
	private final int indicesCount;

	private final int vboVerticesHandle;
	private final int vboIndicesHandle;
	private final int vboColorHandle;

	private VertexArrayObject(final Builder builder) {

		if (builder.rotation != null) {
			for (Vector3f vec : builder.vertices) {
				SimpleMath.mul(builder.rotation, vec, vec);
			}
		}
		if (builder.translation != null) {
			for (Vector3f vec : builder.vertices) {
				SimpleMath.add(builder.translation, vec, vec);
			}
		}

		vaoHandle = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoHandle);

		// color
		vboColorHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboColorHandle);
		float[] colorBuff = builder.getColors();
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorBuff.length);
		colorBuffer.put(colorBuff);
		colorBuffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);

		int colorAttrib = builder.renderer.getVertexAttrib(AttributeHandle.COLOR);
		GL20.glEnableVertexAttribArray(colorAttrib);
		GL20.glVertexAttribPointer(colorAttrib, Builder.COLOR_SIZE, GL11.GL_FLOAT, false, 0, 0);


		// vertices
		vboVerticesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
		float[] floatBuff = builder.getVertices();
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
		verticesBuffer.put(floatBuff);
		verticesBuffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

		int positionAttrib = builder.renderer.getVertexAttrib(AttributeHandle.POSITION);
		GL20.glEnableVertexAttribArray(positionAttrib);
		GL20.glVertexAttribPointer(positionAttrib, Builder.VERTEX_SIZE, GL11.GL_FLOAT, false, 0, 0);


		// indices
		vboIndicesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
		byte[] byteBuff = builder.getIndices();
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(byteBuff.length);
		indicesBuffer.put(byteBuff);
		indicesBuffer.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

		GL11.glLineWidth(Builder.LINE_WIDTH);

		GL30.glBindVertexArray(0);

		indicesCount = byteBuff.length;
	}

	public static class Builder {
		private static int VERTEX_SIZE = 4;
		private static int COLOR_SIZE = 4;
		private static float LINE_WIDTH = 5f;
		private final List<Vector3f> vertices = new ArrayList<Vector3f>();
		private List<ReadableColor> colors = new ArrayList<ReadableColor>();
		private final List<Byte> indices = new ArrayList<Byte>();
		private Renderer renderer;
		private Vector3f translation;
		private Quaternion rotation;

		public VertexArrayObject build() {
			return new VertexArrayObject(this);
		}

		public void setVertices(final List<Vector3f> vertices) {
			this.vertices.addAll(vertices);
		}

		public void setIndices(final List<Byte> indices) {
			this.indices.addAll(indices);
		}

		public void setRenderer(final Renderer renderer) {
			this.renderer = renderer;
		}

		public void setRotation(final Quaternion quaternion) {
			this.rotation = quaternion;
		}

		public void setTranslation(final Vector3f translation) {
			this.translation = translation;
		}


		public void setColor(final List<ReadableColor> colors) {
			this.colors = colors;
		}

		public float[] getVertices() {
			float[] result = new float[vertices.size() * VERTEX_SIZE];
			int i = 0;
			for (Vector3f v : vertices) {
				result[i++] = v.x;
				result[i++] = v.y;
				result[i++] = v.z;
				result[i++] = 1f;
			}
			return result;
		}

		public byte[] getIndices() {
			byte[] result = new byte[indices.size()];
			int i = 0;
			for (Byte b : indices) {
				result[i++] = b;
			}
			return result;
		}

		public float[] getColors() {
			float[] result = new float[colors.size() * COLOR_SIZE];
			int i = 0;
			for (ReadableColor c : colors) {
				result[i++] = c.getRed()/256f;
				result[i++] = c.getGreen()/256f;
				result[i++] = c.getBlue()/256f;
				result[i++] = c.getAlpha()/256f;
			}
			return result;
		}


	}


	@Override
	public void draw() {
		GL30.glBindVertexArray(vaoHandle);
		GL11.glDrawElements(GL11.GL_LINE_STRIP, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void dispose() {
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
