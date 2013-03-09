package net.wohlfart.gl.shader.mesh;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

/**
 * <p>ByteTriangles class.</p>
 *
 *
 *
 */
public class ByteTriangles implements Indices<Byte> {

    private final List<Byte> content;

    /**
     * <p>Constructor for ByteTriangles.</p>
     *
     * @param content an array of {@link java.lang.Byte} objects.
     */
    public ByteTriangles(final Byte[] content) {
        this.content = Arrays.asList(content);
    }

    /**
     * <p>Constructor for ByteTriangles.</p>
     *
     * @param content a {@link java.util.List} object.
     */
    public ByteTriangles(List<Byte> content) {
        this.content = content;
    }

    /** {@inheritDoc} */
    @Override
    public List<Byte> getContent() {
        return content;
    }

    /** {@inheritDoc} */
    @Override
    public int getStructure() {
        return GL11.GL_TRIANGLE_STRIP;
    }

    /** {@inheritDoc} */
    @Override
    public int getElemSize() {
        // TODO Auto-generated method stub
        return 0;
    }

}
