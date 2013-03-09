package net.wohlfart.gl.texture;

import java.awt.Color;

/**
 * <p>SimpleProcTexture class.</p>
 *
 *
 *
 */
public class SimpleProcTexture extends ProceduralTexture {

    /**
     * <p>Constructor for SimpleProcTexture.</p>
     *
     * @param width a int.
     * @param height a int.
     */
    public SimpleProcTexture(int width, int height) {
        super(width, height);
    }

    /** {@inheritDoc} */
    @Override
    protected int[] initialize(int width, int height, int[] data) {
        for (int x = 0; x < width; x++) {
            setPixel(x, height / 2, Color.BLUE, data);
        }
        return data;
    }

}
