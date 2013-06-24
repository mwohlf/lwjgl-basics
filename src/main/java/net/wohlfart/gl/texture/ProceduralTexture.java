package net.wohlfart.gl.texture;

import java.awt.Color;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * <p>
 * Abstract ProceduralTexture class.
 * </p>
 */
public abstract class ProceduralTexture implements TextureBuffer {

    protected int width;
    protected int height;
    protected IntBuffer buffer;

    /**
     * <p>
     * Constructor for ProceduralTexture.
     * </p>
     *
     * @param width
     *            a int.
     * @param height
     *            a int.
     */
    public ProceduralTexture(int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = BufferUtils.createIntBuffer(width * height);
    }

    /** {@inheritDoc} */
    @Override
    public void init() {
        int[] data = new int[width * height];
        buffer.get(data);
        buffer.rewind();
        data = initialize(width, height, data);
        buffer.put(data);
        buffer.rewind();
    }

    /**
     * <p>
     * initialize.
     * </p>
     *
     * @param width
     *            a int.
     * @param height
     *            a int.
     * @param data
     *            an array of int.
     * @return an array of int.
     */
    protected abstract int[] initialize(int width, int height, int[] data);

    /**
     * <p>
     * setPixel.
     * </p>
     *
     * @param x
     *            a int.
     * @param y
     *            a int.
     * @param color
     *            a {@link java.awt.Color} object.
     * @param data
     *            an array of int.
     */
    protected void setPixel(int x, int y, Color color, int[] data) {
        y = height - y - 1;
        if (x < 0) {
            throw new IllegalArgumentException("x < 0");
        }
        if (y < 0) {
            throw new IllegalArgumentException("y < 0");
        }
        if (x > width - 1) {
            throw new IllegalArgumentException("x > width - 1");
        }
        if (y > height - 1) {
            throw new IllegalArgumentException("y > height - 1");
        }

        final int i = x + y * width;
        int value = 0;
        value = value | 0xff & color.getAlpha();
        value = value << 8;
        value = value | 0xff & color.getRed();
        value = value << 8;
        value = value | 0xff & color.getGreen();
        value = value << 8;
        value = value | 0xff & color.getBlue();
        // data[i] = color.getRGB();
        data[i] = value;
    }


    /** {@inheritDoc} */
    @Override
    public IntBuffer getBuffer() {
        return buffer;
    }

    /** {@inheritDoc} */
    @Override
    public int getWidth() {
        return width;
    }

    /** {@inheritDoc} */
    @Override
    public int getHeight() {
        return height;
    }

}
