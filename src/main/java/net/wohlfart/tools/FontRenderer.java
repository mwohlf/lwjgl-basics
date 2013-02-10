package net.wohlfart.tools;

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

import net.wohlfart.gl.elements.hud.CharacterAtlas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FontRenderer {
    protected static final Logger LOGGER = LoggerFactory.getLogger(FontRenderer.class);

    public static final char NULL_CHAR = '_';
    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789;:,.-_#+?!\"()";
    {
        if (!(chars.contains(new String("" + NULL_CHAR)))) {
            throw new IllegalStateException("need NULL_CHAR in char sequence");
        }
        ;
    };

    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    private static final boolean borderOn = false;

    // private static final String FONT_FILE = "/fonts/alphbeta.ttf";
    private static final String FONT_FILE = "/fonts/Greyscale_Basic_Regular.ttf";
    // private static final String FONT_FILE = "/fonts/AeroviasBrasilNF.ttf";

    private CharacterAtlas atlas;

    public FontRenderer init() {
        final String filename = FONT_FILE;
        try (InputStream inputStream = ClassLoader.class.getResourceAsStream(filename);) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = font.deriveFont(16f); // size
            atlas = createCharacterAtlas(font);
        } catch (FontFormatException | IOException ex) {
            LOGGER.error("can't create font from file '" + filename + "', the atlas will be null, expect more errors", ex);
        }
        return this;
    }

    public CharacterAtlas getCharacterAtlas() {
        return atlas;
    }

    CharacterAtlas createCharacterAtlas(Font font) {
        final CharacterAtlas atlas = new CharacterAtlas();

        final BufferedImage buffImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = (Graphics2D) buffImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(new Color(0f, 0f, 0f, 0f)); // transparent
        g.fillRect(0, 0, WIDTH, HEIGHT);
        final FontMetrics fontMetrics = g.getFontMetrics();
        final LineMetrics lineMetrics = fontMetrics.getLineMetrics(chars, g);

        final float height = lineMetrics.getHeight();
        final float ascent = lineMetrics.getAscent();
        float x = 0;
        float y = 0;
        for (final char c : chars.toCharArray()) {
            final float width = fontMetrics.charWidth(c);
            if ((x + width) > WIDTH) { // new line
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
                g.drawRect((int) x, (int) y, (int) width, (int) height);
            }
            x += width;
        }
        atlas.setImage(buffImage);
        atlas.init();
        return atlas;
    }

}
