package net.wohlfart.gl.texture;

import java.awt.Color;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>Abstract ProceduralTexture class.</p>
 *
 *
 *
 */
public abstract class ProceduralTexture implements TextureBuffer {

    protected long seed;
    protected int width;
    protected int height;
    protected IntBuffer buffer;
    protected int id;

    /**
     * <p>Constructor for ProceduralTexture.</p>
     *
     * @param width a int.
     * @param height a int.
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
     * <p>initialize.</p>
     *
     * @param width a int.
     * @param height a int.
     * @param data an array of int.
     * @return an array of int.
     */
    protected abstract int[] initialize(int width, int height, int[] data);

    /**
     * <p>setPixel.</p>
     *
     * @param x a int.
     * @param y a int.
     * @param color a {@link java.awt.Color} object.
     * @param data an array of int.
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

    /**
     * 0/0 is top left, the whole texture is wrapped around a sphere
     *
     * @return a vector with each element [0..1]
     * @param x a int.
     * @param y a int.
     */
    protected Vector3f getNormalVector(final int x, final int y) {
        final int yRange = height - 1;
        final int xRange = width - 1;
        final float latitude = (float) Math.PI * ((float) y / (float) yRange); // [0 .. PI] (north-south)
        final float longitude = (float) Math.PI * 2 * ((float) x / (float) xRange); // [0 .. TWO_PI]

        final float xx = (float) Math.sin(longitude) * (float) Math.sin(latitude); // 0 -> 0; 1/2pi -> 1 ; pi -> 0
        final float yy = (float) Math.cos(latitude); // 0 -> 1; 1/2pi -> 0 ; pi -> -1
        final float zz = (float) Math.cos(longitude) * (float) Math.sin(latitude); // 0 -> 1;...

        return new Vector3f(xx, yy, zz);
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
