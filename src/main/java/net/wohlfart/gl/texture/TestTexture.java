package net.wohlfart.gl.texture;

import java.io.IOException;

/**
 * <p>TestTexture class.</p>
 *
 *
 *
 */
public class TestTexture extends ImageTexture {
    private static final String TEXTURE_FILENAME = "texture.jpg";

    /**
     * <p>Constructor for TestTexture.</p>
     *
     * @throws java.io.IOException if any.
     */
    public TestTexture() throws IOException {
        super(TestTexture.class.getResourceAsStream(TEXTURE_FILENAME));
    }

}
