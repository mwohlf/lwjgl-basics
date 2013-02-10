package net.wohlfart.gl.elements.hud;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.mesh.CharacterMesh;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.tools.Vertex;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharacterMeshBuilder {
	protected static final Logger LOGGER = LoggerFactory.getLogger(CharacterMeshBuilder.class);


	private CharacterAtlas atlas;
	private CharInfo info;
	private float screenX;
	private float screenY;

	public IMesh build() {

		float x1 = screenX/ 512f;  // assuming a screen size of 1000x1000
		float y1 = screenY;
		float x2 = x1 + info.getWidth()/atlas.getImage().getWidth();
		float y2 = y1 - info.getHeight()/atlas.getImage().getHeight();

		float s1 = info.getX()/atlas.getImage().getWidth();
		float t1 = info.getY()/atlas.getImage().getHeight();
		float s2 = (info.getX() + info.getWidth())/atlas.getImage().getWidth();
		float t2 = (info.getY() + info.getHeight())/atlas.getImage().getHeight();

		// We'll define our quad using 4 vertices of the custom 'Vertex' class
		Vertex v0 = new Vertex();
		v0.setXYZ(x1, y1, 0f);
		v0.setST(s1, t1);

		Vertex v1 = new Vertex();
		v1.setXYZ(x1, y2, 0f);
		v1.setST(s1, t2);

		Vertex v2 = new Vertex();
		v2.setXYZ(x2, y2, 0f);
		v2.setST(s2, t2);

		Vertex v3 = new Vertex();
		v3.setXYZ(x2, y1, 0f);
		v3.setST(s2, t1);

		Vertex[] vertices = new Vertex[] {v0, v1, v2, v3};
		// Put each 'Vertex' in one FloatBuffer the order depends on the shaders positions!
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
		for (int i = 0; i < vertices.length; i++) {
			verticesBuffer.put(vertices[i].getXYZW());
			verticesBuffer.put(vertices[i].getRGBA());  // FIXME: color not needed
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

		int positionAttrib = ShaderAttributeHandle.POSITION.getLocation();
		//int colorAttrib = ShaderAttributeHandle.COLOR.getLocation();
		int textureAttrib = ShaderAttributeHandle.TEXTURE_COORD.getLocation();

		// Create a new Vertex Buffer Object in memory and select it (bind)
		int vboVerticesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

		// Put the positions in attribute list 0
		GL20.glVertexAttribPointer(positionAttrib, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		// Put the texture in attribute list 2
		GL20.glVertexAttribPointer(textureAttrib, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind) - INDICES
		int vboIndicesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		int texId = atlas.getTextureId();
		return new CharacterMesh(vaoHandle, vboVerticesHandle,
				vboIndicesHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0, positionAttrib, textureAttrib, texId);
	}

	public void setCharInfo(CharInfo info) {
		this.info = info;
	}

	public void setCharAtlas(CharacterAtlas atlas) {
		this.atlas = atlas;
	}

	public void setScreenX(float x) {
		this.screenX = x;
	}

	public void setScreenY(float y) {
		this.screenY = y;
	}

}
