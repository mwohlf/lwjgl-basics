package net.wohlfart.gl.texture;

import java.awt.Color;
import java.nio.IntBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * CelestialTexture class.
 * </p>
 */
public class CelestialTexture implements ITexture {
    final long seed;
    final CelestialType celestialType;
    protected final int width;
    protected final int height;
    protected final IntBuffer texture;

    protected int id;

    /**
     * <p>
     * Constructor for CelestialTexture.
     * </p>
     *
     * @param radius
     *            a float.
     * @param celestialType
     *            a {@link net.wohlfart.gl.texture.CelestialType} object.
     * @param seed
     *            a long.
     */
    public CelestialTexture(final float radius, final CelestialType celestialType, final long seed) {
        this.width = (int) (radius * 2f * (float) Math.PI + 0.5f);
        this.height = (int) (radius * 2f * (float) Math.PI + 0.5f);
        this.celestialType = celestialType;
        this.seed = seed;
        texture = BufferUtils.createIntBuffer(width * height);
    }

    /**
     * <p>
     * Constructor for CelestialTexture.
     * </p>
     *
     * @param width
     *            a int.
     * @param height
     *            a int.
     * @param celestialType
     *            a {@link net.wohlfart.gl.texture.CelestialType} object.
     * @param seed
     *            a long.
     */
    public CelestialTexture(final int width, final int height, CelestialType celestialType, final long seed) {
        this.width = width;
        this.height = height;
        this.celestialType = celestialType;
        this.seed = seed;
        texture = BufferUtils.createIntBuffer(width * height);
    }

    /** {@inheritDoc} */
    @Override
    public void init() {

        // random for texture variation
        final float textureVariant = new Random(seed).nextFloat();
        final int[] data = new int[width * height];
        texture.get(data);
        // getTexture().mark();
        texture.rewind();

        Vector3f vector;
        Color color;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                vector = getNormalVector(x, y);
                // vector.scale(255f);
                color = celestialType.getColor(vector.x, vector.y, vector.z, textureVariant);
                // color = Color.BLUE;
                setPixel(x, y, color, width, height, data);
            }
        }
        texture.put(data);
        texture.rewind();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        final IntBuffer buffer = BufferUtils.createIntBuffer(1);
        GL11.glGenTextures(buffer);
        id = buffer.get(0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        // GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, texture);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    /**
     * this does a 2D to 3D transformation
     * 0/0 is top left, the whole texture is wrapped around the celestial object
     *
     * @return a vector with each element [0..1]
     */
    final Vector3f getNormalVector(final int x, final int y) {
        final int yRange = height - 1;
        final int xRange = width - 1;
        final float latitude = (float) Math.PI * ((float) y / (float) yRange); // [0 .. PI] (north-south)
        final float longitude = (float) Math.PI * 2 * ((float) x / (float) xRange); // [0 .. TWO_PI]

        final float xx = (float) Math.sin(longitude) * (float) Math.sin(latitude); // 0 -> 0; 1/2pi -> 1 ; pi -> 0
        final float yy = (float) Math.cos(latitude); // 0 -> 1; 1/2pi -> 0 ; pi -> -1
        final float zz = (float) Math.cos(longitude) * (float) Math.sin(latitude); // 0 -> 1;...

        return new Vector3f(xx, yy, zz);
    }

    private void setPixel(int x, int y, Color color, int width, int height, int[] data) {
        y = height - y - 1;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x > width - 1) {
            x = width - 1;
        }
        if (y > height - 1) {
            y = height - 1;
        }

        final int i = x + y * width;
        int value = 0;
        value = value | 0xff & color.getRed();
        value = value << 8;
        value = value | 0xff & color.getGreen();
        value = value << 8;
        value = value | 0xff & color.getBlue();
        value = value << 8;

        // int value = (byte) color.getRed();
        // value >>= 8;
        // value |= (byte) color.getGreen();
        // value <<= 8;
        // value |= (byte) color.getBlue();
        // value <<= 8;
        // value |= (byte) color.getAlpha();

        data[i] = color.getRGB();

        // data[i + 0] = (byte) color.getRed(); // r
        // data[i + 1] = (byte) color.getGreen(); // g
        // data[i + 2] = (byte) color.getBlue(); // b
        // data[i + 3] = (byte) color.getAlpha(); // a
    }

    /** {@inheritDoc} */
    @Override
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    /** {@inheritDoc} */
    @Override
    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    /**
     * <p>
     * Getter for the field <code>width</code>.
     * </p>
     *
     * @return a int.
     */
    public int getWidth() {
        return width;
    }

    /**
     * <p>
     * Getter for the field <code>height</code>.
     * </p>
     *
     * @return a int.
     */
    public int getHeight() {
        return height;
    }

}
