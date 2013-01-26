package net.wohlfart.gl.elements;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.TexturedFragmentMesh;
import net.wohlfart.gl.tools.ColorGradient;
import net.wohlfart.gl.tools.SimplexNoise;
import net.wohlfart.gl.tools.Vertex;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public enum SkyboxSide {
	PLUS_Y {
		{
			translation = new Vector3f(0, +dist, 0);
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist),
					translation, new Quaternion());
		}
	},
	MINUS_Y {
		{
			translation = new Vector3f(0, -dist, 0);
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist),
					translation, new Quaternion());
		}
	},
	PLUS_X {
		{
			translation = new Vector3f(+dist, 0, 0);
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist),
					translation, new Quaternion());
		}
	},
	MINUS_X {
		{
			translation = new Vector3f(-dist, 0, 0);
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist),
					translation, new Quaternion());
		}
	},
	PLUS_Z {
		{
			translation = new Vector3f(0, 0, +dist);
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist),
					translation, new Quaternion());
		}
	},
	MINUS_Z {
		{
			translation = new Vector3f(0, 0, -dist);
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist),
					translation, new Quaternion());
		}
	};

	private static final float dist = 1f; // distance from the origin to the wall of the skybox
	private static final int octaves = 5;
	private static final float persistence = 0.5f;
	private static final float w = 0.5f;


	Quaternion rotation;
	Vector3f translation;

	// main entry point
	public IMesh build(Renderer renderer, int width, int height) {

		// load the texture
		int textureId = createAndLoadTexture(GL13.GL_TEXTURE0, width, height);

		// We'll define our quad using 4 vertices of the custom 'Vertex' class
		Vertex v0 = new Vertex();
		v0.setXYZ(translate(rotate(new Vector3f(-dist, +dist, 0f))));
		v0.setRGB(1, 1, 1);
		v0.setST(0, 0);

		Vertex v1 = new Vertex();
		v1.setXYZ(translate(rotate(new Vector3f(-dist, -dist, 0f))));
		v1.setRGB(1, 1, 1);
		v1.setST(0, 1);

		Vertex v2 = new Vertex();
		v2.setXYZ(translate(rotate(new Vector3f(+dist, -dist, 0f))));
		v2.setRGB(1, 1, 1);
		v2.setST(1, 1);

		Vertex v3 = new Vertex();
		v3.setXYZ(translate(rotate(new Vector3f(+dist, +dist, 0f))));
		v3.setRGB(1, 1, 1);
		v3.setST(1, 0);

		Vertex[] vertices = new Vertex[] { v0, v1, v2, v3 };
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
		for (int i = 0; i < vertices.length; i++) {
			verticesBuffer.put(vertices[i].getXYZW());
			verticesBuffer.put(vertices[i].getRGBA());
			verticesBuffer.put(vertices[i].getST());
		}
		verticesBuffer.flip();

		// OpenGL expects to draw vertices in counter clockwise order by default
		byte[] indices = { 0, 1, 2, 2, 3, 0 };
		int indicesCount = indices.length;
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		// Create a new Vertex Array Object in memory and select it (bind)
		int vaoHandle = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoHandle);

		int positionAttrib = renderer.getVertexAttrib(ShaderAttributeHandle.POSITION);
		int colorAttrib = renderer.getVertexAttrib(ShaderAttributeHandle.COLOR);
		int textureAttrib = renderer.getVertexAttrib(ShaderAttributeHandle.TEXTURE_COORD);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		int vboVerticesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

		// Put the positions in attribute list 0
		GL20.glVertexAttribPointer(positionAttrib, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		// Put the colors in attribute list 1
		GL20.glVertexAttribPointer(colorAttrib, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		// Put the texture in attribute list 2
		GL20.glVertexAttribPointer(textureAttrib, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind) - INDICES
		int vboIndicesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		return new TexturedFragmentMesh(vaoHandle, vboVerticesHandle,
				vboIndicesHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE,
				indicesCount, 0, colorAttrib, positionAttrib, textureAttrib,
				textureId);
	}

	protected int createAndLoadTexture(int textureUnit, int width, int height) {
		int[] canvas = new int[width * height];
		createClouds(canvas, width, height, w, persistence, octaves);
		createStars(canvas, width, height);

		IntBuffer buffer = BufferUtils.createIntBuffer(width * height);
		buffer.put(canvas);
		buffer.rewind();

		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, buffer);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		return texId;
	}

	protected int[] createClouds(int[] data, int width, int height, float w, float persistence, int octaves) {
		final ColorGradient gradient = new ColorGradient(Color.BLACK, Color.BLACK, Color.GRAY);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Vector3f vector = getVector(x, y, width, height);
				double noise = createNoise(vector.x, vector.y, vector.z, w, persistence, octaves);
				Color color = gradient.getColor(noise);
				data[x + y * width] = color.getRGB() << 8;
			}
		}
		return data;
	}

	protected int[] createStars(int[] data, int width, int height) {
		final int o = 5;         // octaves   ~5
		final float p = 0.7f;    // persistence
		final float f = 3f;      // frequency   ~3
		final float w = 1;       // 4d

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Vector3f vector = getVector(x, y, width, height);
				double noise = createNoise(vector.x * f, vector.y * f, vector.z * f, w * f, p, o);

					if ((noise > -0.0001) && (noise < +0.0001)) {
						Color color = Color.WHITE;
						data[x + y * width] = color.getRGB() << 8;
					}

			}
		}
		return data;
	}

	protected Vector3f getVector(int x, int y, int width, int height) {
		// normalize
		float xx = +(x / (width / (dist * 2f)) - dist);
		float yy = -(y / (height / (dist * 2f)) - dist);
		// need to normalize to stay on the sphere
		return rotate(new Vector3f(xx, yy, -1).normalise(new Vector3f()));
	}

	protected Vector3f rotate(Vector3f in) {
		Vector3f result = new Vector3f();
		SimpleMath.mul(rotation, in, result);
		return result;
	}

	protected Vector3f translate(Vector3f in) {
		Vector3f result = new Vector3f();
		SimpleMath.add(translation, in, result);
		return result;
	}


	protected double createNoise(final float x, final float y, final float z, final float w,
			final float persistence, final int octaves) {
		double result = 0;
		float max = 0;
		for (int i = 0; i < octaves; i++) {
			float frequency = (float) Math.pow(2, i);
			float amplitude = (float) Math.pow(persistence, i);
			result += createNoise(x, y, z, w, amplitude, frequency);
			max += amplitude;
		}
		return result / max;
	}

	protected double createNoise(final float x, final float y, final float z, final float w,
			final float amplitude, final float frequency) {
		// the noise returns [-1 .. +1]
		double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency, w * frequency);
		return amplitude * noise;
	}

}
