package net.wohlfart.gl.elements;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.AttributeHandle;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.TexturedFragmentMesh;
import net.wohlfart.gl.tools.ColorGradient;
import net.wohlfart.gl.tools.SimplexNoise;
import net.wohlfart.gl.tools.Vertex;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public enum SkyboxSide {
	PLUS_Y {
		{
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -1),
					new Vector3f(0, +1, 0), new Quaternion());
		}
	},
	MINS_Y {
		{
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -1),
					new Vector3f(0, -1, 0), new Quaternion());
		}
	},
	PLUS_X {
		{
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -1),
					new Vector3f(+1, 0, 0), new Quaternion());
		}
	},
	MINUS_X {
		{
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -1),
					new Vector3f(-1, 0, 0), new Quaternion());
		}
	},
	PLUS_Z {
		{
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -1),
					new Vector3f(0, 0, +1), new Quaternion());
		}
	},
	MINUS_Z {
		{
			rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -1),
					new Vector3f(0, 0, -1), new Quaternion());
		}
	};

	Quaternion rotation;

	// main entry point
	public IMesh build(Renderer renderer, int width, int height) {

		// load the texture
		int textureId = createAndLoadTexture(GL13.GL_TEXTURE0, width, height);

		// We'll define our quad using 4 vertices of the custom 'Vertex' class
		Vertex v0 = new Vertex();
		v0.setXYZ(rotate(new Vector3f(-0.5f, 0.5f, 0f)));
		v0.setRGB(1, 0, 0);
		v0.setST(0, 0);

		Vertex v1 = new Vertex();
		v1.setXYZ(rotate(new Vector3f(-0.5f, -0.5f, 0f)));
		v1.setRGB(0, 1, 0);
		v1.setST(0, 1);

		Vertex v2 = new Vertex();
		v2.setXYZ(rotate(new Vector3f(0.5f, -0.5f, 0f)));
		v2.setRGB(0, 0, 1);
		v2.setST(1, 1);

		Vertex v3 = new Vertex();
		v3.setXYZ(rotate(new Vector3f(0.5f, 0.5f, 0f)));
		v3.setRGB(1, 1, 1);
		v3.setST(1, 0);

		Vertex[] vertices = new Vertex[] { v0, v1, v2, v3 };
		// Put each 'Vertex' in one FloatBuffer the order depends on the shaders
		// positions!
		FloatBuffer verticesBuffer = BufferUtils
				.createFloatBuffer(vertices.length * Vertex.elementCount);
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

		int positionAttrib = renderer.getVertexAttrib(AttributeHandle.POSITION);
		int colorAttrib = renderer.getVertexAttrib(AttributeHandle.COLOR);
		int textureAttrib = renderer
				.getVertexAttrib(AttributeHandle.TEXTURE_COORD);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		int vboVerticesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer,
				GL15.GL_STATIC_DRAW);

		// Put the positions in attribute list 0
		GL20.glVertexAttribPointer(positionAttrib, Vertex.positionElementCount,
				GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		// Put the colors in attribute list 1
		GL20.glVertexAttribPointer(colorAttrib, Vertex.colorElementCount,
				GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		// Put the texture in attribute list 2
		GL20.glVertexAttribPointer(textureAttrib, Vertex.textureElementCount,
				GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind) - INDICES
		int vboIndicesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		return new TexturedFragmentMesh(vaoHandle, vboVerticesHandle,
				vboIndicesHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE,
				indicesCount, 0, colorAttrib, positionAttrib, textureAttrib,
				textureId);
	}

	int createAndLoadTexture(int textureUnit, int width, int height) {

		IntBuffer buffer = BufferUtils.createIntBuffer(width * height);
		buffer.put(create(width, height, 0.5f, 0.5f, 6, new ColorGradient(Color.WHITE,Color.BLUE)));
		buffer.rewind();

		// Create a new texture object in memory and bind it
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
				0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		// Setup the ST coordinate system
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_REPEAT);
		// Setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR_MIPMAP_LINEAR);

		return texId;
	}

	protected int[] create(int width, int height, float w, float persistence, int octaves, ColorGradient gradient) {
		int[] data = new int[width * height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Vector3f vector = getVector(x, y, width, height);
				double noise = createNoise(vector.x, vector.y, vector.z, w,
						persistence, octaves);
				Color color = gradient.getColor(noise);
				data[x + y * width] = color.getRGB();
			}
		}
		return data;
	}

	protected Vector3f getVector(int x, int y, int width, int height) {
		// normalize
		float xx = -(x / (width / 2f) - 1f);
		float yy = +(y / (height / 2f) - 1f);
		// need to normalize to stay on the sphere
		return rotate(new Vector3f(xx, yy, -1).normalise(new Vector3f()));
	}

	protected Vector3f rotate(Vector3f in) {
		Vector3f result = new Vector3f();
		SimpleMath.mul(rotation, in, result);
		return result;
	}

	protected double createNoise(final float x, final float y, final float z, final float w, final float persistence, final int octaves) {
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

	protected double createNoise(final float x, final float y, final float z, final float w, final float amplitude, final float frequency) {
		// the noise returns [-1 .. +1]
		double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency, w * frequency);
		return amplitude * noise;
	}

}
