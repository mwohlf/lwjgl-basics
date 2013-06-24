package net.wohlfart.gl.spatial;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.tools.PNGDecoder;
import net.wohlfart.tools.PNGDecoder.Format;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CelestialBodyMesh implements IsRenderable {

    private final int vaoHandle;
    private final int textureHandle;

    private final int indicesCount;
    private final int indexElemSize;
    private final int trianglePrimitive;

    CelestialBodyMesh(int vaoHandle, int textureHandle, int trianglePrimitive, int indexElemSize, int indicesCount) {
        this.vaoHandle = vaoHandle;
        this.textureHandle = textureHandle;
        this.trianglePrimitive = trianglePrimitive;
        this.indexElemSize = indexElemSize;
        this.indicesCount = indicesCount;
    }

    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        GL11.glDrawElements(trianglePrimitive, indicesCount, indexElemSize, 0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }



    public static class Builder {
        private static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        // the start data (LOD: 0)
        float t = (1.0f + SimpleMath.sqrt(5.0f)) / 2.0f;
        private final List<Vector3f> initialVertices = new ArrayList<Vector3f>(Arrays.<Vector3f> asList(new Vector3f[] {// @formatter:off
                new Vector3f(-1f, t, 0),
                new Vector3f(+1f, t, 0),
                new Vector3f(-1f,-t, 0),
                new Vector3f(+1f,-t, 0),

                new Vector3f( 0f,-1, t),
                new Vector3f( 0f, 1, t),
                new Vector3f( 0f,-1,-t),
                new Vector3f( 0f, 1,-t),

                new Vector3f( t,  0,-1),
                new Vector3f( t,  0, 1),
                new Vector3f(-t,  0,-1),
                new Vector3f(-t,  0, 1), }));

        private final List<Integer> initialIndices = Arrays.asList(new Integer[] {
                 0, 11,  5,
                 0,  5,  1,
                 0,  1,  7,
                 0,  7, 10,
                 0, 10, 11,
                 1,  5,  9,
                 5, 11,  4,
                11, 10,  2,
                10,  7,  6,
                 7,  1,  8,
                 3,  9,  4,
                 3,  4,  2,
                 3,  2,  6,
                 3,  6,  8,
                 3,  8,  9,
                 4,  9,  5,
                 2,  4, 11,
                 6,  2, 10,
                 8,  6,  7,
                 9,  8,  1,
        }); // @formatter:on

        private float radius;

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public IsRenderable build() {

            for (final Vector3f vec : initialVertices) {
                final float l = vec.length();
                vec.scale(radius / l);
            }

            // we need to create this:
            float[] stream;
            int[] indices;

            int elemSize = 3 + 3 + 2;
            stream = new float[initialVertices.size() * elemSize];
            for (int i = 0; i < (initialVertices.size()); i++) {
                // coords:
                Vector3f point = new Vector3f(initialVertices.get(i));
                stream[i* elemSize + 0] = point.getX();
                stream[i* elemSize + 1] = point.getY();
                stream[i* elemSize + 2] = point.getZ();
                // normal:
                point.normalise();
                stream[i* elemSize + 3] = point.getX();
                stream[i* elemSize + 4] = point.getY();
                stream[i* elemSize + 5] = point.getZ();
                // texture:
                Vector2f loc = SimpleMath.getPositionVector(initialVertices.get(i));
                stream[i* elemSize + 6] = loc.x;
                stream[i* elemSize + 7] = loc.y;
            }


            indices = new int[initialIndices.size()];
            for (int i = 0; i < initialIndices.size(); i++) {
                indices[i] = initialIndices.get(i);
            }


            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle); // @formatter:off

            final int textureHandle = createTextureHandle("/models/cube/cube.png", GL13.GL_TEXTURE0);  // also binds the texture
            createVboHandle(stream); // this also binds the GL15.GL_ARRAY_BUFFER
            createIdxBufferHandle(indices); // this also binds the GL15.GL_ELEMENT_ARRAY_BUFFER

            int offset;
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                             + ShaderAttributeHandle.NORMAL.getByteCount()
                             + ShaderAttributeHandle.TEXTURE_COORD.getByteCount();

            offset = 0;
            ShaderAttributeHandle.POSITION.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(),
                                       ShaderAttributeHandle.POSITION.getFloatCount(), GL11.GL_FLOAT,
                                       false, stride, offset);

            offset += ShaderAttributeHandle.POSITION.getByteCount();
            ShaderAttributeHandle.NORMAL.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.NORMAL.getLocation(),
                                       ShaderAttributeHandle.NORMAL.getFloatCount(), GL11.GL_FLOAT,
                                       false, stride, offset);

            offset += ShaderAttributeHandle.NORMAL.getByteCount();
            ShaderAttributeHandle.TEXTURE_COORD.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.TEXTURE_COORD.getLocation(),
                                       ShaderAttributeHandle.TEXTURE_COORD.getFloatCount(), GL11.GL_FLOAT,
                                       false, stride, offset);

            ShaderAttributeHandle.COLOR.disable();

            // we are done with the VAO state
            GL30.glBindVertexArray(0); // @formatter:on

            final int indexElemSize = GL11.GL_UNSIGNED_INT;
            final int indicesCount = indices.length;
            final int primitiveType = GL11.GL_TRIANGLES;

            return new CelestialBodyMesh(vaoHandle, textureHandle, primitiveType, indexElemSize, indicesCount);
        }

        private int createIdxBufferHandle(int[] indices) {
            final IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
            indicesBuffer.put(indices);
            indicesBuffer.flip();
            final int idxBufferHandle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idxBufferHandle);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
            return idxBufferHandle;
        }

        private int createVboHandle(float[] floatBuff) {
            final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
            verticesBuffer.put(floatBuff);
            verticesBuffer.flip();
            final int vboVerticesHandle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
            return vboVerticesHandle;
        }

        // FIXME: ugly hack to keep the same textureID
        private static int staticTextId = 0;

        private int createTextureHandle(String filename, int textureUnit) {
            int texId = 0;

            if (staticTextId != 0) {
                return staticTextId;
            }

            // InputStream inputStream = new FileInputStream(filename);
            try (InputStream inputStream = ClassLoader.class.getResourceAsStream(filename);) {

                // Link the PNG decoder to this stream
                final PNGDecoder decoder = new PNGDecoder(inputStream);
                // Get the width and height of the texture
                final int tWidth = decoder.getWidth();
                final int tHeight = decoder.getHeight();
                System.out.println(" tWidth: " + tWidth + " tHeight: " + tHeight);
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
            staticTextId = texId;
            return texId;
        }

    }



}
