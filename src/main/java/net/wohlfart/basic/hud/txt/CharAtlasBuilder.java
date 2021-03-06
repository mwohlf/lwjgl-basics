package net.wohlfart.basic.hud.txt;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import net.wohlfart.basic.GenericGameException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CharAtlasBuilder {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CharAtlasBuilder.class);

    private static final float DEFAULT_FONT_SIZE = 12f;
    //private static final String DEFAULT_FONT_FILE = "/fonts/alphbeta.ttf";
    //private static final String DEFAULT_FONT_FILE = "/fonts/Greyscale_Basic_Regular.ttf";
    //private static final String DEFAULT_FONT_FILE = "/fonts/AeroviasBrasilNF.ttf";
    private static final String DEFAULT_FONT_FILE = "/fonts/VeraMono.ttf";

    public static final char NULL_CHAR = '_';
    private static final String CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789;:,.-_#+?!\"()";
    {
        if (!CHARS.contains(String.valueOf(NULL_CHAR))) {
            throw new IllegalStateException("need NULL_CHAR in char sequence");
        }
    };

    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    private boolean borderOn = false;
    private float fontSize = DEFAULT_FONT_SIZE;
    private String fontFile = DEFAULT_FONT_FILE;


    public CharAtlas build() {
        try (InputStream inputStream = ClassLoader.class.getResourceAsStream(fontFile);) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = font.deriveFont(fontSize); // size
            return createCharacterAtlas(font);
        } catch (FontFormatException | IOException ex) {
            throw new GenericGameException("can't create font from file '" + fontFile + "'", ex);
        }
    }


    public CharAtlasBuilder setBorderOn(boolean borderOn) {
        this.borderOn = borderOn;
        return this;
    }

    public CharAtlasBuilder setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public CharAtlasBuilder setFontFile(String fontFile) {
        this.fontFile = fontFile;
        return this;
    }

    private CharAtlas createCharacterAtlas(Font font) {
        final CharAtlas atlas = new CharAtlas();

        final BufferedImage buffImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = (Graphics2D) buffImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(new Color(0f, 0f, 0f, 0f)); // transparent
        g.fillRect(0, 0, WIDTH, HEIGHT);
        final FontMetrics fontMetrics = g.getFontMetrics();
        final LineMetrics lineMetrics = fontMetrics.getLineMetrics(CHARS, g);

        final float height = lineMetrics.getHeight();
        final float ascent = lineMetrics.getAscent();
        float x = 0;
        float y = 0;
        for (final char c : CHARS.toCharArray()) {
            final float width = fontMetrics.charWidth(c);
            if (x + width > WIDTH) { // new line
                x = 0;
                y += height;
                if (y + height > HEIGHT) {
                    throw new IllegalStateException("chars don't fit into the atlas");
                }
            }
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(c), x, y + ascent);
            atlas.put(c, x, y, width, height);
            if (borderOn) {
                g.setColor(Color.RED);
                g.drawRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
            }
            x += width;
        }
        atlas.setupImage(buffImage);
        return atlas;
    }

}
