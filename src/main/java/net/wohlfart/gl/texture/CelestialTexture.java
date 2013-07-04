package net.wohlfart.gl.texture;

import java.awt.Color;
import java.nio.IntBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;


// see: http://www.java-gaming.org/index.php?topic=25516.0
public class CelestialTexture implements ITexture {
    final long seed;
    final CelestialType celestialType;
    protected final int width;
    protected final int height;
    private final int textureUnit;

    protected int texId;


    public CelestialTexture(float radius, CelestialType celestialType, long seed, int textureUnit) {
        this.width = (int) (radius * 2f * (float) Math.PI + 0.5f);
        this.height = (int) (radius * 2f * (float) Math.PI + 0.5f);
        this.celestialType = celestialType;
        this.seed = seed;
        this.textureUnit = textureUnit;
    }


    public CelestialTexture(int width, int height, CelestialType celestialType, long seed, int textureUnit) {
        this.width = width;
        this.height = height;
        this.celestialType = celestialType;
        this.seed = seed;
        this.textureUnit = textureUnit;
    }

    @Override
    public void init() {

        IntBuffer intBuffer = BufferUtils.createIntBuffer(width * height);


        // random for texture variation
        final float textureVariant = new Random(seed).nextFloat();

        final int[] data = new int[width * height]; // 4 byte
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Vector3f vector = getNormalVector(x, y);
                // vector.scale(255f);
                Color color = celestialType.getColor(vector.x, vector.y, vector.z, textureVariant);
                //color = Color.BLUE;
                setPixel(x, y, color, width, height, data);
            }
        }
        intBuffer.put(data);
        intBuffer.flip();
        intBuffer.rewind();

        // Create a new texture object in memory and bind it
        texId = GL11.glGenTextures();   // generate texture ID
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);   // bind texture ID

        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        // Upload the texture data and generate mip maps (for scaling)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, intBuffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // Setup the ST coordinate system
        //Setup wrap mode
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        // All RGB bytes are aligned to each other and each component is 1 byte
        //GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        // GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, texture);
        //GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
        //GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, intBuffer);
    }

    /**
     * this does a 2D to 3D transformation
     * 0/0 is top left, the whole texture is wrapped around the celestial object
     *
     * @return a vector with each element [0..1]
     */
    final Vector3f getNormalVector(final int x, final int y) {
        final int yRange = height - 1;
        final int xRange = width - 1;
        final float latitude = (float) Math.PI * ((float) y / (float) yRange); // [0 .. PI] (north-south)
        final float longitude = (float) Math.PI * 2 * ((float) x / (float) xRange); // [0 .. TWO_PI]

        final float xx = (float) Math.sin(longitude) * (float) Math.sin(latitude); // 0 -> 0; 1/2pi -> 1 ; pi -> 0
        final float yy = (float) Math.cos(latitude); // 0 -> 1; 1/2pi -> 0 ; pi -> -1
        final float zz = (float) Math.cos(longitude) * (float) Math.sin(latitude); // 0 -> 1;...

        return new Vector3f(xx, yy, zz);
    }

    private void setPixel(int x, int y, Color color, int width, int height, int[] data) {
        y = height - y - 1;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x > width - 1) {
            x = width - 1;
        }
        if (y > height - 1) {
            y = height - 1;
        }

        final int i = x + y * width;
        int value = 0;
        value = value | 0xff & color.getAlpha();
        value = value << 8;
        value = value | 0xff & color.getBlue();
        value = value << 8;
        value = value | 0xff & color.getGreen();
        value = value << 8;
        value = value | 0xff & color.getRed();
        //value = value << 8;

        // int value = (byte) color.getRed();
        // value >>= 8;
        // value |= (byte) color.getGreen();
        // value <<= 8;
        // value |= (byte) color.getBlue();
        // value <<= 8;
        // value |= (byte) color.getAlpha();

        data[i] = value;

        // data[i + 0] = (byte) color.getRed(); // r
        // data[i + 1] = (byte) color.getGreen(); // g
        // data[i + 2] = (byte) color.getBlue(); // b
        // data[i + 3] = (byte) color.getAlpha(); // a
    }

    @Override
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
    }

    @Override
    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureId() {
        return texId;
    }

}
