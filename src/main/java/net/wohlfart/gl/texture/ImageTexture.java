package net.wohlfart.gl.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

class ImageTexture implements ITexture {

    protected final IntBuffer texture;
    protected final int width;
    protected final int height;

    protected int id;

    public ImageTexture(final InputStream inputStream) throws IOException {
        final BufferedImage image = ImageIO.read(inputStream);
        width = image.getWidth();
        height = image.getHeight();

        final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        texture = BufferUtils.createIntBuffer(pixels.length);
        texture.put(pixels);
        texture.rewind();
    }

    // for subclassing
    protected ImageTexture(final IntBuffer texture, final int width, final int height) {
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }

    protected IntBuffer getTexture() {
        return texture;
    }

    @Override
    public void init() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        final IntBuffer buffer = BufferUtils.createIntBuffer(1);
        GL11.glGenTextures(buffer);
        id = buffer.get(0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, texture);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    @Override
    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

}
