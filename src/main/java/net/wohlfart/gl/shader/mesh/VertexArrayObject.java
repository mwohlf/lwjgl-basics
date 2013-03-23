package net.wohlfart.gl.shader.mesh;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

// see: http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2005.html
/**
 * <p>VertexArrayObject class.</p>
 *
 *
 *
 */
public class VertexArrayObject implements IsRenderable {

    private final int vaoHandle;
    private final int indicesCount;

    private final int vboVerticesHandle;
    private final int vboIndicesHandle;
    private final int vboColorHandle;

    private VertexArrayObject(final Builder builder) {

        if (builder.rotation != null) {
            for (final Vector3f vec : builder.vertices) {
                SimpleMath.mul(builder.rotation, vec, vec);
            }
        }
        if (builder.translation != null) {
            for (final Vector3f vec : builder.vertices) {
                SimpleMath.add(builder.translation, vec, vec);
            }
        }

        vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        // color
        vboColorHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboColorHandle);
        final float[] colorBuff = builder.getColors();
        final FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorBuff.length);
        colorBuffer.put(colorBuff);
        colorBuffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);

        final int colorAttrib = ShaderAttributeHandle.COLOR.getLocation();
        GL20.glEnableVertexAttribArray(colorAttrib);
        GL20.glVertexAttribPointer(colorAttrib, Builder.COLOR_SIZE, GL11.GL_FLOAT, false, 0, 0);

        // vertices
        vboVerticesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
        final float[] floatBuff = builder.getVertices();
        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(floatBuff.length);
        verticesBuffer.put(floatBuff);
        verticesBuffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        final int positionAttrib = ShaderAttributeHandle.POSITION.getLocation();
        GL20.glEnableVertexAttribArray(positionAttrib);
        GL20.glVertexAttribPointer(positionAttrib, Builder.VERTEX_SIZE, GL11.GL_FLOAT, false, 0, 0);

        // indices
        vboIndicesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        final byte[] byteBuff = builder.getIndices();
        final ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(byteBuff.length);
        indicesBuffer.put(byteBuff);
        indicesBuffer.flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

        GL11.glLineWidth(Builder.LINE_WIDTH);

        GL30.glBindVertexArray(0);

        indicesCount = byteBuff.length;
    }

    public static class Builder {
        private static final int VERTEX_SIZE = 4;
        private static final int COLOR_SIZE = 4;
        private static final float LINE_WIDTH = 5f;
        private final List<Vector3f> vertices = new ArrayList<Vector3f>();
        private List<ReadableColor> colors = new ArrayList<ReadableColor>();
        private final List<Byte> indices = new ArrayList<Byte>();
        private Vector3f translation;
        private Quaternion rotation;

        public VertexArrayObject build() {
            return new VertexArrayObject(this);
        }

        public void setVertices(final List<Vector3f> vertices) {
            this.vertices.addAll(vertices);
        }

        public void setIndices(final List<Byte> indices) {
            this.indices.addAll(indices);
        }

        public void setRotation(final Quaternion quaternion) {
            this.rotation = quaternion;
        }

        public void setTranslation(final Vector3f translation) {
            this.translation = translation;
        }

        public void setColor(final List<ReadableColor> colors) {
            this.colors = colors;
        }

        public float[] getVertices() {
            final float[] result = new float[vertices.size() * VERTEX_SIZE];
            int i = 0;
            for (final Vector3f v : vertices) {
                result[i++] = v.x;
                result[i++] = v.y;
                result[i++] = v.z;
                result[i++] = 1f;
            }
            return result;
        }

        public byte[] getIndices() {
            final byte[] result = new byte[indices.size()];
            int i = 0;
            for (final Byte b : indices) {
                result[i++] = b;
            }
            return result;
        }

        public float[] getColors() {
            final float[] result = new float[colors.size() * COLOR_SIZE];
            int i = 0;
            for (final ReadableColor c : colors) {
                result[i++] = c.getRed() / 256f;
                result[i++] = c.getGreen() / 256f;
                result[i++] = c.getBlue() / 256f;
                result[i++] = c.getAlpha() / 256f;
            }
            return result;
        }

    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL11.glDrawElements(GL11.GL_LINE_STRIP, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
        GL30.glBindVertexArray(0);
    }



    /** {@inheritDoc} */
    @Override
    public void destroy() {
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);

        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboIndicesHandle);

        // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboVerticesHandle);

        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }

}
