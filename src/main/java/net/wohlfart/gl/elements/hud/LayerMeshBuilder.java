package net.wohlfart.gl.elements.hud;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.TexturedFragmentMesh;
import net.wohlfart.gl.tools.Vertex;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LayerMeshBuilder {
    protected static final Logger LOGGER = LoggerFactory.getLogger(LayerMeshBuilder.class);

    private int texId;

    public IMesh build() {

        // We'll define our quad using 4 vertices of the custom 'Vertex' class
        final Vertex v0 = new Vertex();
        v0.setXYZ(-0.5f, 0.5f, 0f);
        v0.setRGB(1, 0, 0);
        v0.setST(0, 0);

        final Vertex v1 = new Vertex();
        v1.setXYZ(-0.5f, -0.5f, 0f);
        v1.setRGB(0, 1, 0);
        v1.setST(0, 1);

        final Vertex v2 = new Vertex();
        v2.setXYZ(0.5f, -0.5f, 0f);
        v2.setRGB(0, 0, 1);
        v2.setST(1, 1);

        final Vertex v3 = new Vertex();
        v3.setXYZ(0.5f, 0.5f, 0f);
        v3.setRGB(1, 1, 1);
        v3.setST(1, 0);

        final Vertex[] vertices = new Vertex[] { v0, v1, v2, v3 };
        // Put each 'Vertex' in one FloatBuffer the order depends on the shaders positions!
        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
        for (int i = 0; i < vertices.length; i++) {
            verticesBuffer.put(vertices[i].getXYZW());
            verticesBuffer.put(vertices[i].getRGBA());
            verticesBuffer.put(vertices[i].getST());
        }
        verticesBuffer.flip();

        // OpenGL expects to draw vertices in counter clockwise order by default
        final byte[] indices = { 0, 1, 2, 2, 3, 0 };
        final int indicesCount = indices.length;
        final ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        // Create a new Vertex Array Object in memory and select it (bind)
        final int vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        final int positionAttrib = ShaderAttributeHandle.POSITION.getLocation();
        final int colorAttrib = ShaderAttributeHandle.COLOR.getLocation();
        final int textureAttrib = ShaderAttributeHandle.TEXTURE_COORD.getLocation();

        // Create a new Vertex Buffer Object in memory and select it (bind)
        final int vboVerticesHandle = GL15.glGenBuffers();
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

        // deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);

        // Create a new VBO for the indices and select it (bind) - INDICES
        final int vboIndicesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        return new TexturedFragmentMesh(vaoHandle, vboVerticesHandle, vboIndicesHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0, colorAttrib,
                positionAttrib, textureAttrib, texId);
    }

    public void setTextureId(int texId) {
        this.texId = texId;
    }

}
