package net.wohlfart.basic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import net.wohlfart.tools.PNGDecoder;

/**
 * A manager to handle binary resources.
 */
public final class ResourceManager {  // REVIEWED

    protected String fontDirectory = "resources/fonts/";
    protected String gfxDirectory = "resources/gfx/";
    protected String modelsDirectory = "resources/models/";
    protected String shadersDirectory = "resources/shaders/";

    public void setFontDirectory(String fontDirectory) {
        this.fontDirectory = fontDirectory;
    }

    public void setGfxDirectory(String gfxDirectory) {
        this.gfxDirectory = gfxDirectory;
    }

    public void setModelsDirectory(String modelsDirectory) {
        this.modelsDirectory = modelsDirectory;
    }

    public void setShadersDirectory(String shadersDirectory) {
        this.shadersDirectory = shadersDirectory;
    }

    public URL getGfxUrl(String string) {
        final URL result = getClass().getResource(gfxDirectory + string);
        if (result == null) {
            throw new IllegalArgumentException("can't find resource '" + string + "'" + " in directory '" + gfxDirectory + "'");
        }
        return result;
    }

    public ByteBuffer loadIcon(URL url) throws IOException {
        try (InputStream is = url.openStream()) {
            final PNGDecoder decoder = new PNGDecoder(is);
            final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(byteBuffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            byteBuffer.flip();
            return byteBuffer;
        }
    }

}
