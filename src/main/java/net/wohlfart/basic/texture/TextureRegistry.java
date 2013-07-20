package net.wohlfart.basic.texture;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import net.wohlfart.tools.PNGDecoder;
import net.wohlfart.tools.PNGDecoder.Format;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum TextureRegistry {
    TEXTURE_REGISTRY;
    static final Logger LOGGER = LoggerFactory.getLogger(TextureRegistry.class);


    HashMap<String, Integer> storage = new HashMap<>();

    public int getTextureHandle(String key, int textureUnit) {
        return getTextureHandle(key, textureUnit, GL11.GL_REPEAT);
    }

    public int getTextureHandle(String key, int textureUnit, int textureWrap) {
        if (!storage.containsKey(key)) {
            storage.put(key, createTextureHandle(key, textureUnit, textureWrap));
        }
        return (storage.get(key));
    }


    private int createTextureHandle(String filename, int textureUnit, int textureWrap) {
        int handle = 0;

        System.err.println("loading: " + filename);

        // InputStream inputStream = new FileInputStream(filename);
        try (InputStream inputStream = ClassLoader.class.getResourceAsStream(filename);) {

            // Link the PNG decoder to this stream
            final PNGDecoder decoder = new PNGDecoder(inputStream);
            // Get the width and height of the texture
            final int tWidth = decoder.getWidth();
            final int tHeight = decoder.getHeight();
            // Decode the PNG file in a ByteBuffer
            final ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
            buffer.flip();

            // Create a new texture object in memory and bind it
            handle = GL11.glGenTextures();
            GL13.glActiveTexture(textureUnit);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);

            // All RGB bytes are aligned to each other and each component is 1 byte
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            // Upload the texture data and generate mip maps (for scaling)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, tWidth, tHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            // Setup the ST coordinate system
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, textureWrap);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, textureWrap);
            // Setup what to do when the texture has to be scaled
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        } catch (final FileNotFoundException ex) {
            LOGGER.error("can't load texture image", ex);
        } catch (final IOException ex) {
            LOGGER.error("can't load texture image", ex);
        }
        return handle;
    }



}
