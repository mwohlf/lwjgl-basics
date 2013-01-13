package net.wohlfart.gl.shader.mesh;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.AttributeHandle;
import net.wohlfart.gl.tools.Vertex;
import net.wohlfart.tools.PNGDecoder;
import net.wohlfart.tools.PNGDecoder.Format;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TexturedMeshBuilder {
	protected static final Logger LOGGER = LoggerFactory.getLogger(TexturedMeshBuilder.class);

	private Renderer renderer;
	private String textureFilename;

	private Quaternion rotation;

	private Vector3f translation;


	public IMesh build() {

		// load the texture
		int textureId = loadPNGTexture(textureFilename, GL13.GL_TEXTURE0);


		// We'll define our quad using 4 vertices of the custom 'Vertex' class
		Vertex v0 = new Vertex();
		v0.setXYZ(-0.5f, 0.5f, 0f);
		v0.setRGB(1, 0, 0);
		v0.setST(0, 0);

		Vertex v1 = new Vertex();
		v1.setXYZ(-0.5f, -0.5f, 0f);
		v1.setRGB(0, 1, 0);
		v1.setST(0, 1);

		Vertex v2 = new Vertex();
		v2.setXYZ(0.5f, -0.5f, 0f);
		v2.setRGB(0, 0, 1);
		v2.setST(1, 1);

		Vertex v3 = new Vertex();
		v3.setXYZ(0.5f, 0.5f, 0f);
		v3.setRGB(1, 1, 1);
		v3.setST(1, 0);

		Vertex[] vertices = new Vertex[] {v0, v1, v2, v3};
		// Put each 'Vertex' in one FloatBuffer the order depends on the shaders positions!
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
		for (int i = 0; i < vertices.length; i++) {
			verticesBuffer.put(vertices[i].getXYZW());
			verticesBuffer.put(vertices[i].getRGBA());
			verticesBuffer.put(vertices[i].getST());
		}
		verticesBuffer.flip();

		// OpenGL expects to draw vertices in counter clockwise order by default
		byte[] indices = {
				0, 1, 2,
				2, 3, 0
		};
		int indicesCount = indices.length;
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		// Create a new Vertex Array Object in memory and select it (bind)
		int vaoHandle = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoHandle);

		int positionAttrib = renderer.getVertexAttrib(AttributeHandle.POSITION);
		int colorAttrib = renderer.getVertexAttrib(AttributeHandle.COLOR);
		int textureAttrib = renderer.getVertexAttrib(AttributeHandle.TEXTURE_COORD);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		int vboVerticesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

		int size = Vertex.colorByteCount + Vertex.positionBytesCount + Vertex.textureByteCount;
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
				vboIndicesHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0, colorAttrib, positionAttrib, textureAttrib, textureId);
	}


	private int loadPNGTexture(String filename, int textureUnit) {
		int texId = 0;

		//InputStream inputStream = new FileInputStream(filename);
		try (InputStream inputStream = ClassLoader.class.getResourceAsStream(filename);) {

			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(inputStream);
			// Get the width and height of the texture
			int tWidth = decoder.getWidth();
			int tHeight = decoder.getHeight();
			// Decode the PNG file in a ByteBuffer
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
			buffer.flip();

			// Create a new texture object in memory and bind it
			texId = GL11.glGenTextures();
			GL13.glActiveTexture(textureUnit);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
			// All RGB bytes are aligned to each other and each component is 1 byte
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			// Upload the texture data and generate mip maps (for scaling)
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, tWidth, tHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			// Setup the ST coordinate system
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			// Setup what to do when the texture has to be scaled
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

		} catch (FileNotFoundException ex) {
			LOGGER.error("can't load texture image", ex);
		} catch (IOException ex) {
			LOGGER.error("can't load texture image", ex);
		}
		return texId;
	}

	private void applyRotationAndTranslation() {
/*		if (rotation != null) {
			for (Vector3f vec : vertices) {
				SimpleMath.mul(rotation, vec, vec);
			}
		}
		if (translation != null) {
			for (Vector3f vec : vertices) {
				SimpleMath.add(translation, vec, vec);
			}
		}
*/	}



	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}


	public void setTextureFilename(final String textureFilename) {
		this.textureFilename = textureFilename;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}

}