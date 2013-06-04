package net.wohlfart.gl.shader.mesh;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.tools.PNGDecoder;
import net.wohlfart.tools.PNGDecoder.Format;
import net.wohlfart.tools.Vertex;

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

/**
 * <p>
 * TexturedFragmentMesh class.
 * </p>
 *
 *
 *
 */
public class TexturedMesh implements IsRenderable {

    private final int vaoHandle;
    private final int vboVerticesHandle;
    private final int vboIndicesHandle;
    private final int indicesCount;
    private final int indexOffset;
    private final int indexElemSize;
    private final int indicesType;

    private final int colorAttrib;
    private final int positionAttrib;
    private final int textureAttrib;

    private final int textureId;

    // package private, created by the builder
    /**
     * <p>
     * Constructor for TexturedFragmentMesh.
     * </p>
     *
     * @param vaoHandle
     *            a int.
     * @param vboVerticesHandle
     *            a int.
     * @param vboIndicesHandle
     *            a int.
     * @param indicesType
     *            a int.
     * @param indexElemSize
     *            a int.
     * @param indicesCount
     *            a int.
     * @param indexOffset
     *            a int.
     * @param colorAttrib
     *            a int.
     * @param positionAttrib
     *            a int.
     * @param textureAttrib
     *            a int.
     * @param textureId
     *            a int.
     */
    public TexturedMesh(int vaoHandle, int vboVerticesHandle,
    // index
            int vboIndicesHandle, int indicesType, int indexElemSize, int indicesCount, int indexOffset,
            // attrib pos
            int colorAttrib, int positionAttrib, int textureAttrib, int textureId) {

        this.vaoHandle = vaoHandle;
        this.vboVerticesHandle = vboVerticesHandle;
        // index
        this.vboIndicesHandle = vboIndicesHandle;
        this.indicesType = indicesType;
        this.indexElemSize = indexElemSize;
        this.indicesCount = indicesCount;
        this.indexOffset = indexOffset;
        // attr pos
        this.colorAttrib = colorAttrib;
        this.positionAttrib = positionAttrib;
        this.textureAttrib = textureAttrib;

        this.textureId = textureId;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        // Bind the texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL30.glBindVertexArray(vaoHandle);
        GL20.glEnableVertexAttribArray(colorAttrib);
        GL20.glEnableVertexAttribArray(positionAttrib);
        GL20.glEnableVertexAttribArray(textureAttrib);
        // Bind to the index VBO that has all the information about the order of the vertices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        // Draw the vertices
        GL11.glDrawElements(indicesType, indicesCount, indexElemSize, indexOffset);
        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(colorAttrib);
        GL20.glDisableVertexAttribArray(positionAttrib);
        GL20.glDisableVertexAttribArray(textureAttrib);
        GL30.glBindVertexArray(0);
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);
        // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboVerticesHandle);
        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboIndicesHandle);
        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }


    public static class Builder {
        /** Constant <code>LOGGER</code> */
        protected static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        private String textureFilename;

        private Quaternion rotation;

        private Vector3f translation;

        /**
         * <p>
         * build.
         * </p>
         *
         * @return a {@link net.wohlfart.gl.shader.mesh.IRenderable} object.
         */
        public IsRenderable build() {

            // load the texture
            final int textureId = loadPNGTexture(textureFilename, GL13.GL_TEXTURE0);

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

            final int size = Vertex.colorByteCount + Vertex.positionBytesCount + Vertex.textureByteCount;
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
            final int vboIndicesHandle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

            return new TexturedMesh(vaoHandle, vboVerticesHandle, vboIndicesHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0, colorAttrib,
                    positionAttrib, textureAttrib, textureId);
        }

        private int loadPNGTexture(String filename, int textureUnit) {
            int texId = 0;

            // InputStream inputStream = new FileInputStream(filename);
            try (InputStream inputStream = ClassLoader.class.getResourceAsStream(filename)) {

                // Link the PNG decoder to this stream
                final PNGDecoder decoder = new PNGDecoder(inputStream);
                // Get the width and height of the texture
                final int tWidth = decoder.getWidth();
                final int tHeight = decoder.getHeight();
                // Decode the PNG file in a ByteBuffer
                final ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
                decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
                buffer.flip();

                // Create a new texture object in memory and bind it
                texId = GL11.glGenTextures();
                GL13.glActiveTexture(textureUnit);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
                // All RGB bytes are aligned to each other and each component is 1 byte
                GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                // Upload the texture data and generate mip maps (for scaling)
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, tWidth, tHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
                GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
                // Setup the ST coordinate system
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
                // Setup what to do when the texture has to be scaled
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

            } catch (final FileNotFoundException ex) {
                LOGGER.error("can't load texture image", ex);
            } catch (final IOException ex) {
                LOGGER.error("can't load texture image", ex);
            }
            return texId;
        }

        private void applyRotationAndTranslation() {
            /*
             * if (rotation != null) { for (Vector3f vec : vertices) { SimpleMath.mul(rotation, vec, vec); } } if (translation != null) { for (Vector3f vec :
             * vertices) { SimpleMath.add(translation, vec, vec); } }
             */}

        /**
         * <p>
         * Setter for the field <code>textureFilename</code>.
         * </p>
         *
         * @param textureFilename
         *            a {@link java.lang.String} object.
         */
        public void setTextureFilename(final String textureFilename) {
            this.textureFilename = textureFilename;
        }

        /**
         * <p>
         * Setter for the field <code>rotation</code>.
         * </p>
         *
         * @param rotation
         *            a {@link org.lwjgl.util.vector.Quaternion} object.
         */
        public void setRotation(Quaternion rotation) {
            this.rotation = rotation;
        }

        /**
         * <p>
         * Setter for the field <code>translation</code>.
         * </p>
         *
         * @param translation
         *            a {@link org.lwjgl.util.vector.Vector3f} object.
         */
        public void setTranslation(Vector3f translation) {
            this.translation = translation;
        }

    }
}
