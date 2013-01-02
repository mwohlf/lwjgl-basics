package net.wohlfart.gl.shader.mesh;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
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

	private static int VERTEX_SIZE = 4;
	private static int COLOR_SIZE = 4;
	private static float LINE_WIDTH = 5f;

	private final List<Vector3f> vertices = new ArrayList<Vector3f>();
	private List<ReadableColor> colors = new ArrayList<ReadableColor>();

	private final List<Byte> indices = new ArrayList<Byte>();
	private int indexType;

	private Vector3f translation;
	private Quaternion rotation;

	public IMeshData build(final Renderer renderer) {

		applyRotationAndTranslation();

		int vaoHandle = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoHandle);

		// colors
		int vboColorHandle = -1;
		if (colors.size() > 1) {
			vboColorHandle = createVboColorHandle(renderer);
		}

		// vertices
		int vboVerticesHandle = createVboVerticesHandle(renderer);

		// indices
		int vboIndicesHandle = createElementArrayBuffer(renderer);

		GL11.glLineWidth(LINE_WIDTH);

		GL30.glBindVertexArray(0);

		int indicesCount = getIndices().length;


		IMeshData result = null;
		switch (indexType) {
			case GL11.GL_LINE_STRIP:
				int colorAttrib = renderer.getVertexAttrib(AttributeHandle.COLOR);
				LOGGER.debug("creating line strip");
				result = new WireframeMesh(
						vaoHandle, vboVerticesHandle, vboIndicesHandle,
						colors.isEmpty()?null:colors.get(0),
						colorAttrib,
						indicesCount);
				break;
			case GL11.GL_TRIANGLE_STRIP:
				LOGGER.debug("creating triangle strip");
				result = new TriangleStripMesh(vaoHandle, vboVerticesHandle, vboColorHandle, vboIndicesHandle, indicesCount);
				break;
			default:
				LOGGER.error("unknown index type: {}", indexType);
		}

		return result;
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

	private int createVboVerticesHandle(final Renderer renderer) {
		int vboVerticesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
		float[] floatBuff = getVertices();
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
		verticesBuffer.put(floatBuff);
		verticesBuffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

		int positionAttrib = renderer.getVertexAttrib(AttributeHandle.POSITION);
		GL20.glEnableVertexAttribArray(positionAttrib);
		GL20.glVertexAttribPointer(positionAttrib, VERTEX_SIZE, GL11.GL_FLOAT, false, 0, 0);
		return vboVerticesHandle;
	}

	private int createVboColorHandle(final Renderer renderer) {
		int vboColorHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboColorHandle);
		float[] colorBuff = getColors();
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorBuff.length);
		colorBuffer.put(colorBuff);
		colorBuffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);

		int colorAttrib = renderer.getVertexAttrib(AttributeHandle.COLOR);
		GL20.glEnableVertexAttribArray(colorAttrib);
		GL20.glVertexAttribPointer(colorAttrib, COLOR_SIZE, GL11.GL_FLOAT, false, 0, 0);
		return vboColorHandle;
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

	private float[] getColors() {
		float[] result = new float[colors.size() * COLOR_SIZE];
		int i = 0;
		for (ReadableColor c : colors) {
			result[i++] = c.getRed() / 256f;  // FIXME: 255 or 256 ???
			result[i++] = c.getGreen() / 256f;
			result[i++] = c.getBlue() / 256f;
			result[i++] = c.getAlpha() / 256f;
		}
		return result;
	}

	// --

	public void setVertices(final List<Vector3f> vertices) {
		this.vertices.addAll(vertices);
	}

	public void setIndices(final Indices<Byte> indices) {
		this.indexType = indices.getType();
		this.indices.addAll(indices.getContent());
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

	public void setColor(final ReadableColor color) {
		this.colors = Collections.singletonList(color);
	}

}
