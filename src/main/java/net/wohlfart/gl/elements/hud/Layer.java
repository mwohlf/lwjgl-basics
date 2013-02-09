package net.wohlfart.gl.elements.hud;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.tools.CharacterAtlas;
import net.wohlfart.tools.FontRenderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// see: http://www.java-gaming.org/index.php?topic=25516.0
// read: http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
// and: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
// and: http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2005.html
public class Layer implements Renderable {
	protected static final Logger LOGGER = LoggerFactory.getLogger(Layer.class);


	protected IMesh meshData;

	protected IMesh setupMesh() {

		CharacterAtlas atlas = new FontRenderer().init().getCharacterAtlas();
		BufferedImage image = atlas.getImage();
		int texId = image2Texture(image, GL13.GL_TEXTURE0);
		LayerMeshBuilder builder = new LayerMeshBuilder();
		builder.setTextureId(texId);
		return builder.build();
	}

	protected void resetMeshData() {
		meshData = null;
	}

	@Override
	public void render() {
		if (meshData == null) {
			meshData = setupMesh();
		}
		meshData.draw();
	}

	@Override
	public void dispose() {
		meshData.dispose();
		meshData = null;
	}



	protected int image2Texture(Image im, int textureUnit) {
		int texId = 0;

		int width = im.getWidth(null);
		int height = im.getHeight(null);
	    BufferedImage bufImage = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics bg = bufImage.getGraphics();
	    bg.drawImage(im, 0, 0, null);
	    bg.dispose();

		int[] pixels = new int[width * height];
		bufImage.getRGB(0, 0, width, height, pixels, 0, width);

	    ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4); //4 for RGBA, 3 for RGB
	    for(int y = 0; y < height; y++){
	    	for(int x = 0; x < width; x++){
	    		int pixel = pixels[y * width + x];

	    		buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
	    		buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
	    		buffer.put((byte) (pixel & 0xFF));               // Blue component
	    		buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
	    	}
	    }
	    buffer.flip();

		// Create a new texture object in memory and bind it
		texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		// Setup the ST coordinate system
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		// Setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		return texId;
	}

}
