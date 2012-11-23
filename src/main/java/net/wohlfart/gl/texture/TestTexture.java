package net.wohlfart.gl.texture;

import java.io.IOException;


public class TestTexture extends ImageTexture {
	private static final String TEXTURE_FILENAME = "texture.jpg";

	public TestTexture() throws IOException {
		super(TestTexture.class.getResourceAsStream(TEXTURE_FILENAME));
	}

}
