package net.wohlfart.gl.shader.mesh;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.tools.Vertex;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColoredMeshBuilder {
	protected static final Logger LOGGER = LoggerFactory.getLogger(WireframeMeshBuilder.class);

	private Renderer renderer;

	private Vector3f translation;
	private Quaternion rotation;


	public IMesh build() {

		// We'll define our quad using 4 vertices of the custom 'Vertex' class
		Vertex v0 = new Vertex(); v0.setXYZ(-0.5f, 0.5f, 0f); v0.setRGB(1, 0, 0);
		Vertex v1 = new Vertex(); v1.setXYZ(-0.5f, -0.5f, 0f); v1.setRGB(0, 1, 0);
		Vertex v2 = new Vertex(); v2.setXYZ(0.5f, -0.5f, 0f); v2.setRGB(0, 0, 1);
		Vertex v3 = new Vertex(); v3.setXYZ(0.5f, 0.5f, 0f); v3.setRGB(1, 1, 1);

		Vertex[] vertices = new Vertex[] {v0, v1, v2, v3};
		// Put each 'Vertex' in one FloatBuffer the order depends on the shaders positions!
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
		for (int i = 0; i < vertices.length; i++) {
			verticesBuffer.put(vertices[i].getXYZW());
			verticesBuffer.put(vertices[i].getRGBA());
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

		int positionAttrib = renderer.getVertexAttrib(ShaderAttributeHandle.POSITION);
		int colorAttrib = renderer.getVertexAttrib(ShaderAttributeHandle.COLOR);

		// Create a new Vertex Buffer Object in memory and bind it
		int vboVerticesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		// Put the positions in attribute list 0
		GL20.glVertexAttribPointer(positionAttrib, 4, GL11.GL_FLOAT, false, Vertex.colorByteCount + Vertex.positionBytesCount, Vertex.positionByteOffset);
		// Put the colors in attribute list 1
		GL20.glVertexAttribPointer(colorAttrib, 4, GL11.GL_FLOAT, false, Vertex.colorByteCount + Vertex.positionBytesCount, Vertex.colorByteOffset);
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind) - INDICES
		int vboIndicesHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);


		return new ColoredFragmentMesh(vaoHandle, vboVerticesHandle, vboIndicesHandle,
				GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0, colorAttrib, positionAttrib);
	}



	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

}
