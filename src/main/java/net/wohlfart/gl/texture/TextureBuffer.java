package net.wohlfart.gl.texture;

import java.nio.IntBuffer;

/**
 * <p>TextureBuffer interface.</p>
 *
 *
 *
 */
public interface TextureBuffer {

    /**
     * <p>init.</p>
     */
    void init();

    /**
     * <p>getBuffer.</p>
     *
     * @return a {@link java.nio.IntBuffer} object.
     */
    IntBuffer getBuffer();

    /**
     * <p>getWidth.</p>
     *
     * @return a int.
     */
    int getWidth();

    /**
     * <p>getHeight.</p>
     *
     * @return a int.
     */
    int getHeight();

}
