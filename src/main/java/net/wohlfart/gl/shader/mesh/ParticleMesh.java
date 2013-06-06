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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParticleMesh implements IsRenderable {

    private final int vaoHandle;
    private final int vboHandle;
    private final int verticesCount;
    private final int primitive;
    private final int textureId;


    protected ParticleMesh(int vaoHandle, int vboHandle, int verticesCount, int primitive, int textureId) {
        this.vaoHandle = vaoHandle;
        this.vboHandle = vboHandle;
        this.verticesCount = verticesCount;
        this.primitive = primitive;
        this.textureId = textureId;
    }

    @Override
    public void render() {
        // Bind the texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL30.glBindVertexArray(vaoHandle);
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.COLOR.getLocation());
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());
        GL20.glEnableVertexAttribArray(ShaderAttributeHandle.TEXTURE_COORD.getLocation());
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.NORMAL.getLocation());


        // Draw the vertices
        GL11.glDrawArrays(primitive, 0, verticesCount);

        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.COLOR.getLocation());
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());
        GL20.glDisableVertexAttribArray(ShaderAttributeHandle.TEXTURE_COORD.getLocation());
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboHandle);

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }



    public static final class Builder {  // FIXME: extend builder from one commin base builder with all the tools
        private static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        private String textureFilename;
        private Integer texId;

        private FloatBuffer verticesBuffer;
        private int count;

        public void setTextureFilename(final String textureFilename) {
            this.textureFilename = textureFilename;
        }

        public void setTextureId(int texId) {
            this.texId = texId;
        }

        public void setParticleCount(int count) {
            this.count = count;
        }

        public void setFlippedFloatBuffer(FloatBuffer verticesBuffer) {
            this.verticesBuffer = verticesBuffer;
        }

        public ParticleMesh build() {
            // load the texture if needed
            if (textureFilename != null) {
                texId  = loadPNGTexture(textureFilename, GL13.GL_TEXTURE0);
            }

            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);

            // Create a new Vertex Buffer Object in memory and select it (bind)
            final int vboHandle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboHandle);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

            GL20.glEnableVertexAttribArray(ShaderAttributeHandle.POSITION.getLocation());
            GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(),
                    Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);

            GL20.glEnableVertexAttribArray(ShaderAttributeHandle.COLOR.getLocation());
            GL20.glVertexAttribPointer(ShaderAttributeHandle.COLOR.getLocation(),
                    Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);

            GL20.glEnableVertexAttribArray(ShaderAttributeHandle.TEXTURE_COORD.getLocation());
            GL20.glVertexAttribPointer(ShaderAttributeHandle.TEXTURE_COORD.getLocation(),
                    Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);

            GL20.glDisableVertexAttribArray(ShaderAttributeHandle.NORMAL.getLocation());


            // unbind
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            // Deselect (bind to 0) the VAO
            GL30.glBindVertexArray(0);


            LOGGER.debug("bulding a new mesh with {} elements", count);
            return new ParticleMesh(vaoHandle, vboHandle, count * 6, GL11.GL_TRIANGLES, texId);
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


    }

}
