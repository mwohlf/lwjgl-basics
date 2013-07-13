package net.wohlfart.gl.shader.mesh;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.Vertex;
import net.wohlfart.tools.PNGDecoder;
import net.wohlfart.tools.PNGDecoder.Format;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
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
    private final int indicesCount;
    private final int indexOffset;
    private final int indexElemSize;
    private final int indicesType;
    private final int textureId;


    private TexturedMesh(int vaoHandle, int indicesType, int indexElemSize, int indicesCount, int indexOffset, int textureId) {
        this.vaoHandle = vaoHandle;
        this.indicesType = indicesType;
        this.indexElemSize = indexElemSize;
        this.indicesCount = indicesCount;
        this.indexOffset = indexOffset;
        this.textureId = textureId;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glDrawElements(indicesType, indicesCount, indexElemSize, indexOffset);
        GL30.glBindVertexArray(0);
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }


    public static class Builder extends AbstractMeshBuilder {

        protected static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        private String textureFilename;
        private Integer texId;
        private float size = 1f;                              // length of one side of the texture
        private int textureWrap = GL11.GL_REPEAT;


        private final Quaternion rotation = Quaternion.setIdentity(new Quaternion());
        private final Vector3f translation = new Vector3f(0,0,0);


        @Override
        public IsRenderable build() {
            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);

            if (textureFilename != null) {
                texId  = loadPNGTexture(textureFilename, GL13.GL_TEXTURE0);
            }

            createVboHandle(createStream());

            byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
            createIdxBufferHandle(indices);

            final int[] offset = {0};
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                    + ShaderAttributeHandle.COLOR.getByteCount()
                    + ShaderAttributeHandle.TEXTURE_COORD.getByteCount()
                    ;
            ShaderAttributeHandle.POSITION.enable(stride, offset);
            ShaderAttributeHandle.COLOR.enable(stride, offset);
            ShaderAttributeHandle.TEXTURE_COORD.enable(stride, offset);
            ShaderAttributeHandle.NORMAL.disable();

            // done with the VAO
            GL30.glBindVertexArray(0);

            return new TexturedMesh(vaoHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indices.length, 0, texId);
        }

        protected float[] createStream() {

            final Vector3f[] vectors = new Vector3f[] { // @formatter:off
                    new Vector3f(-(size/2f), +(size/2f), 0),
                    new Vector3f(-(size/2f), -(size/2f), 0),
                    new Vector3f(+(size/2f), -(size/2f), 0),
                    new Vector3f(+(size/2f), +(size/2f), 0),
            };  // @formatter:on

            for (final Vector3f vec : vectors) {
                SimpleMath.mul(rotation, vec, vec);
            }

            for (final Vector3f vec : vectors) {
                SimpleMath.add(translation, vec, vec);
            }

            final Vertex[] vertices =
                    new Vertex[] { new Vertex() {{
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


            // Put each 'Vertex' in one FloatBuffer the order depends on the shaders positions!
            float[] stream = new float[vertices.length * (3 + 4 + 2)];
            int i = 0;
            for (Vertex vertex : vertices) {
                stream[i++] = vertex.getXYZ()[0];
                stream[i++] = vertex.getXYZ()[1];
                stream[i++] = vertex.getXYZ()[2];
                stream[i++] = vertex.getRGBA()[0];
                stream[i++] = vertex.getRGBA()[1];
                stream[i++] = vertex.getRGBA()[2];
                stream[i++] = vertex.getRGBA()[3];
                stream[i++] = vertex.getST()[0];
                stream[i++] = vertex.getST()[1];
            }
            return stream;
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
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, textureWrap);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, textureWrap);
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

        public void setTextureFilename(final String textureFilename) {
            this.textureFilename = textureFilename;
        }

        public void setTextureId(int texId) {
            this.texId = texId;
        }


        @Override
        public void setRotation(Quaternion rotation) {
            this.rotation.set(rotation);
        }

        @Override
        public void setTranslation(Vector3f translation) {
            this.translation.set(translation);
        }

        public void setSize(float size) {
            this.size = size;
        }

        public void setTextureWrap(int textureWrap) {
            this.textureWrap = textureWrap;
        }

    }

}
