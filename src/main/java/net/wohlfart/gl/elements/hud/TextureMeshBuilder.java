package net.wohlfart.gl.elements.hud;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.TexturedFragmentMesh;
import net.wohlfart.gl.tools.Vertex;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * layer for testing the txt features
 *
 *
 *
 */
public class TextureMeshBuilder {
    /** Constant <code>LOGGER</code> */
    protected static final Logger LOGGER = LoggerFactory.getLogger(TextureMeshBuilder.class);

    private int texId;
    private Quaternion rotation = new Quaternion();
    private Vector3f translation = new Vector3f();


    /**
     * <p>build.</p>
     *
     * @return a {@link net.wohlfart.gl.shader.mesh.IMesh} object.
     */
    public IMesh build() {

        final Vector3f[] vectors = new Vector3f[] {
                new Vector3f(-0.5f,+0.5f, 0),
                new Vector3f(-0.5f,-0.5f, 0),
                new Vector3f(+0.5f,-0.5f, 0),
                new Vector3f(+0.5f,+0.5f, 0)
        };

        if (rotation != null) {
            for (final Vector3f vec : vectors) {
                SimpleMath.mul(rotation, vec, vec);
            }
        }
        if (translation != null) {
            for (final Vector3f vec : vectors) {
                SimpleMath.add(translation, vec, vec);
            }
        }


        final Vertex[] vertices = new Vertex[] {
                new Vertex() {{
                    setXYZ(vectors[0].x, vectors[0].y, vectors[0].z);
                    setRGB(1, 0, 0);
                    setST(0, 0);
                }},
                new Vertex() {{
                    setXYZ(vectors[1].x, vectors[1].y, vectors[1].z);
                    setRGB(0, 1, 0);
                    setST(0, 1);
                }},
                new Vertex() {{
                    setXYZ(vectors[2].x, vectors[2].y, vectors[2].z);
                    setRGB(0, 0, 1);
                    setST(1, 1);
                }},
                new Vertex() {{
                    setXYZ(vectors[3].x, vectors[3].y, vectors[3].z);
                    setRGB(1, 1, 1);
                    setST(1, 0);
                }}
        };


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

        // @formatter:off
        return new TexturedFragmentMesh(vaoHandle, vboVerticesHandle, vboIndicesHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE,
                indicesCount, 0, colorAttrib, positionAttrib, textureAttrib, texId);
        // @formatter:on
    }

    /**
     * <p>setTextureId.</p>
     *
     * @param texId a int.
     */
    public void setTextureId(int texId) {
        this.texId = texId;
    }

    /**
     * <p>Setter for the field <code>rotation</code>.</p>
     *
     * @param rotation a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    /**
     * <p>Setter for the field <code>translation</code>.</p>
     *
     * @param translation a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

}
