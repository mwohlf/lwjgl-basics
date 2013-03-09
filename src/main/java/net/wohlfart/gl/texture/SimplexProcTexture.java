package net.wohlfart.gl.texture;

import java.awt.Color;

import net.wohlfart.gl.tools.ColorGradient;
import net.wohlfart.gl.tools.SimplexNoise;

import org.lwjgl.util.vector.Vector3f;

/**
 * <p>SimplexProcTexture class.</p>
 *
 *
 *
 */
public class SimplexProcTexture extends ProceduralTexture {

    private final ColorGradient gradient = new ColorGradient(Color.WHITE, Color.BLACK, Color.BLACK);
    private int octaves = 5;
    private float persistence = 0.5f;
    private float w = 5;

    /**
     * <p>Constructor for SimplexProcTexture.</p>
     *
     * @param width a int.
     * @param height a int.
     */
    public SimplexProcTexture(int width, int height) {
        super(width, height);
    }

    /**
     * <p>Getter for the field <code>octaves</code>.</p>
     *
     * @return a int.
     */
    public int getOctaves() {
        return octaves;
    }

    /**
     * <p>Getter for the field <code>persistence</code>.</p>
     *
     * @return a float.
     */
    public float getPersistence() {
        return persistence;
    }

    /**
     * <p>Getter for the field <code>w</code>.</p>
     *
     * @return a float.
     */
    public float getW() {
        return w;
    }

    /**
     * <p>Setter for the field <code>octaves</code>.</p>
     *
     * @param octaves a int.
     */
    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    /**
     * <p>Setter for the field <code>persistence</code>.</p>
     *
     * @param persistence a float.
     */
    public void setPersistence(float persistence) {
        this.persistence = persistence;
    }

    /**
     * <p>Setter for the field <code>w</code>.</p>
     *
     * @param w a float.
     */
    public void setW(float w) {
        this.w = w;
    }

    /** {@inheritDoc} */
    @Override
    protected int[] initialize(int width, int height, int[] data) {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Vector3f vector = getNormalVector(x, y);
                final double noise = createNoise(vector.x, vector.y, vector.z, w, persistence, octaves);
                final Color color = gradient.getColor(noise);
                setPixel(x, y, color, data);
            }
        }
        return data;
    }

    // adding octaves
    /**
     * <p>createNoise.</p>
     *
     * @param x a float.
     * @param y a float.
     * @param z a float.
     * @param w a float.
     * @param persistence a float.
     * @param octaves a int.
     * @return a double.
     */
    protected double createNoise(final float x, final float y, final float z, final float w, final float persistence, final int octaves) {
        double result = 0;
        float max = 0;
        for (int i = 0; i < octaves; i++) {
            final float frequency = (float) Math.pow(2, i);
            final float amplitude = (float) Math.pow(persistence, i);
            result += createNoise(x, y, z, w, amplitude, frequency);
            max += amplitude;
        }
        return result / max;
    }

    // calling the noise
    /**
     * <p>createNoise.</p>
     *
     * @param x a float.
     * @param y a float.
     * @param z a float.
     * @param w a float.
     * @param amplitude a float.
     * @param frequency a float.
     * @return a double.
     */
    protected double createNoise(final float x, final float y, final float z, final float w, final float amplitude, final float frequency) {
        // the noise returns [-1 .. +1]
        final double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency, w * frequency);
        // for testing
        // double noise = gridnoise(x,y,z);
        return amplitude * noise;
    }

    /**
     * <p>gridnoise.</p>
     *
     * @param x a float.
     * @param y a float.
     * @param z a float.
     * @return a double.
     */
    protected double gridnoise(float x, float y, float z) {
        // @formatter:off
        if (isAlmost(x, 0f)
                || isAlmost(x, 0.1f)
                || isAlmost(x, 0.2f)
                || isAlmost(x, 0.3f)
                || isAlmost(x, 0.4f)
                || isAlmost(x, 0.5f)
                || isAlmost(x, 0.6f)
                || isAlmost(x, 0.7f)
                || isAlmost(x, 0.8f)
                || isAlmost(x, 0.9f)
                || isAlmost(x, 1f)) {
            return 1;
            // @formatter:on
        }
        return -1;
    }

    private boolean isAlmost(float a, float value) {
        return a < value + 0.004 && a > value - 0.004;
    }

}
