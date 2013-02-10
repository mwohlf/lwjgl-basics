package net.wohlfart.gl.elements.hud;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

public class CharacterAtlas {

	HashMap<Character, CharInfo> map = new HashMap<Character, CharInfo>();
	BufferedImage buffImage;
	Integer texId;


	public void put(char c, float x, float y, float w, float h) {
		map.put(c, new CharInfo(c, x, y, w, h));
	}

	public void setImage(BufferedImage buffImage) {
		this.buffImage = buffImage;
	}

	public BufferedImage getImage() {
		return buffImage;
	}

	public CharInfo getCharInfo(char c) {
		return map.get(c);
	}

	public int getTextureId() {
		return texId;
	}


	protected void setupTexture(int textureUnit) {

		int width = buffImage.getWidth(null);
		int height = buffImage.getHeight(null);

		int[] pixels = buffImage.getRGB(0, 0, width, height, new int[width * height], 0, width);
		ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);
		for(int i = 0; i < pixels.length; i++){
			int pixel = pixels[i];
			buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
			buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
			buffer.put((byte) (pixel & 0xFF));             // Blue component
			buffer.put((byte) ((pixel >> 24) & 0xFF));     // Alpha component. Only for RGBA
		}
		buffer.flip();

		// create a new texture object in memory and bind it
		texId = GL11.glGenTextures();
		// not sure if we need that here
		GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_SRC_ALPHA);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		// all RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		// setup the ST coordinate system
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		// setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
	}

	public void init() {
		setupTexture(GL13.GL_TEXTURE1);
	}

}
