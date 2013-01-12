package net.wohlfart.gl.shader.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class WireframeMeshBuilder {
	protected static final Logger LOGGER = LoggerFactory.getLogger(WireframeMeshBuilder.class);

	private final List<Vector3f> vertices = new ArrayList<Vector3f>();

	private final List<Integer> indices = new ArrayList<Integer>();
	private int indicesStructure;
	private int indexElemSize;

	private float lineWidth = 1f;
	private ReadableColor color = ReadableColor.GREY;


	private Vector3f translation;
	private Quaternion rotation;

	private Renderer renderer;


	public IMesh build() {

		applyRotationAndTranslation();

		int vaoHandle = GL30.glGenVertexArrays();

		GL30.glBindVertexArray(vaoHandle);
		int vboVerticesHandle = createVboHandle(getVertices(), renderer, AttributeHandle.POSITION);
		int vboIndicesHandle = createElementArrayBuffer(renderer);

		GL30.glBindVertexArray(0);

		int indicesCount = getIndices().length;
		int colorAttrib = renderer.getVertexAttrib(AttributeHandle.COLOR);
		int positionAttrib = renderer.getVertexAttrib(AttributeHandle.POSITION);
		int textureAttrib = renderer.getVertexAttrib(AttributeHandle.TEXTURE_COORD);
		int offset = 0;

		return new WireframeMesh(
				vaoHandle,
				vboVerticesHandle,
				vboIndicesHandle, indicesStructure, indexElemSize, indicesCount, offset,
				colorAttrib, positionAttrib, textureAttrib, color,
				lineWidth);
	}


	private int createElementArrayBuffer(Renderer renderer) {
		int vboIndicesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
		// FIXME: check the vertex count and use a byte or short buffer here if the number of vertices is low enough
		int[] buffer = getIndices();
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(buffer.length);
		indicesBuffer.put(buffer);
		indicesBuffer.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		return vboIndicesHandle;
	}


	private void applyRotationAndTranslation() {
		if (rotation != null) {
			for (Vector3f vec : vertices) {
				SimpleMath.mul(rotation, vec, vec);
			}
		}
		if (translation != null) {
			for (Vector3f vec : vertices) {
				SimpleMath.add(translation, vec, vec);
			}
		}
	}

	private int createVboHandle(float[] floatBuff, final Renderer renderer, final AttributeHandle attrHandle) {
		int vboVerticesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
		verticesBuffer.put(floatBuff);
		verticesBuffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

		int positionAttrib = renderer.getVertexAttrib(attrHandle);
		GL20.glEnableVertexAttribArray(positionAttrib);
		GL20.glVertexAttribPointer(positionAttrib, attrHandle.getSize(), GL11.GL_FLOAT, false, 0, 0);
		return vboVerticesHandle;
	}

	private float[] getVertices() {
		int posSize = AttributeHandle.POSITION.getSize();
		if (posSize < 4) {
			throw new IllegalArgumentException("vertex position size should be 4");
		}
		float[] result = new float[vertices.size() * AttributeHandle.POSITION.getSize()];
		int i = 0;
		for (Vector3f v : vertices) {
			result[i++] = v.x;
			result[i++] = v.y;
			result[i++] = v.z;
			result[i++] = 1f;
		}
		return result;
	}

	private int[] getIndices() {
		int[] result = new int[indices.size()];
		int i = 0;
		for (Integer b : indices) {
			result[i++] = b;
		}
		return result;
	}

	// --

	public void setVertices(final List<Vector3f> vertices) {
		this.vertices.addAll(vertices);
	}

	public void setIndices(final Integer[] indices) {
		this.indices.addAll(Arrays.asList(indices));
	}

	public void setRotation(final Quaternion quaternion) {
		this.rotation = quaternion;
	}

	public void setTranslation(final Vector3f translation) {
		this.translation = translation;
	}

	public void setColor(final ReadableColor color) {
		this.color = color;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public void setIndicesStructure(int indicesStructure) {
		this.indicesStructure = indicesStructure;
	}

	public void setIndexElemSize(int indexElemSize) {
		this.indexElemSize = indexElemSize;
	}

}
