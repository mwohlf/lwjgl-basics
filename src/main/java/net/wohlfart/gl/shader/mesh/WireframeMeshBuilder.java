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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * this class creates all kind of meshes depending on the available data when the build method is called
 *
 * GL_POINTS,
 * GL_LINE_STRIP,
 * GL_LINE_LOOP,
 * GL_LINES
 * GL_TRIANGLE_STRIP,
 * GL_TRIANGLE_FAN,
 * GL_TRIANGLES
 *
 *
 */
public class WireframeMeshBuilder {
	protected static final Logger LOGGER = LoggerFactory.getLogger(WireframeMeshBuilder.class);

	private static float DEFAULT_LINE_WIDTH = 1f;


	private static int VERTEX_SIZE = 4;


	private float lineWidth = DEFAULT_LINE_WIDTH;

	private final List<Vector3f> vertices = new ArrayList<Vector3f>();
	private ReadableColor color = ReadableColor.GREY;

	private final List<Byte> indices = new ArrayList<Byte>();
	private int indexStructure;
	private int indexElemSize = GL11.GL_UNSIGNED_BYTE;

	private Vector3f translation;
	private Quaternion rotation;

	private Renderer renderer;


	public IMeshData build() {

		applyRotationAndTranslation();

		int vaoHandle = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoHandle);
		int vboVerticesHandle = createVboHandle(getVertices(), renderer, AttributeHandle.POSITION);
		int vboIndicesHandle = createElementArrayBuffer(renderer);

		GL11.glLineWidth(lineWidth);

		GL30.glBindVertexArray(0);

		int indicesCount = getIndices().length;


		int colorAttrib = renderer.getVertexAttrib(AttributeHandle.COLOR);

		return new IndexedLinesMesh(
				vaoHandle,
				vboVerticesHandle,
				vboIndicesHandle, indexStructure, indexElemSize, indicesCount, 0,
				colorAttrib, color);
	}


	private int createElementArrayBuffer(Renderer renderer) {
		int vboIndicesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
		byte[] byteBuff = getIndices();
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(byteBuff.length);
		indicesBuffer.put(byteBuff);
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
		GL20.glVertexAttribPointer(positionAttrib, VERTEX_SIZE, GL11.GL_FLOAT, false, 0, 0);
		return vboVerticesHandle;
	}

	private float[] getVertices() {
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

	private byte[] getIndices() {
		byte[] result = new byte[indices.size()];
		int i = 0;
		for (Byte b : indices) {
			result[i++] = b;
		}
		return result;
	}

	// --

	public void setVertices(final List<Vector3f> vertices) {
		this.vertices.addAll(vertices);
	}

	public void setIndices(final Indices<Byte> indices) {
		this.indexStructure = indices.getStructure();
		this.indexElemSize = indices.getElemSize();
		this.indices.addAll(indices.getContent());
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

}
