package net.wohlfart.gl.elements.skybox;

import java.awt.Color;
import java.nio.IntBuffer;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.skybox.SkyboxParameters.PerlinNoiseParameters;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.mesh.TexturedMesh;
import net.wohlfart.tools.ColorGradient;
import net.wohlfart.tools.SimpleMath;
import net.wohlfart.tools.SimplexNoise;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

// FIXME: no color needed
enum BoxSide implements IsRenderable {// @formatter:off
    PLUS_Y {{
            translation = new Vector3f(0, +dist, 0);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }},
    MINUS_Y {{
            translation = new Vector3f(0, -dist, 0);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }},
    PLUS_X {{
            translation = new Vector3f(+dist, 0, 0);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }},
    MINUS_X {{
            translation = new Vector3f(-dist, 0, 0);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }},
    PLUS_Z {{
            translation = new Vector3f(0, 0, +dist);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }},
    MINUS_Z {{
            translation = new Vector3f(0, 0, -dist);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }};  // @formatter:on

    // FIXME: screensize, texture size and dist parameter need to match so we don't cut off parts of the background
    public static final float DOT_PROD_LIMIT = 0.0f; // FIXME: this also depends on the view angle
    public static final float EPSILON = 0.1f; // add a min value to get behind the zNear clip range
    private static final int SIZE = 1024;

    // distance from the origin to any corner of the view zNear frustum, this becomes the distance to the wall of the skybox
    // and also half the length of the edge of the skybox since the skybox is a cube, getting closer means
    // we would cut off a piece of the skybox because it would get closer than zNear
    protected float dist = SimpleMath.sqrt(2* SimpleMath.sqare(GraphicContextManager.INSTANCE.getNearPlane()));


    protected Vector3f translation;
    protected Quaternion rotation;


    protected IsRenderable delegate;
    protected Vector3f normal;


    @Override
    public void render() {
        delegate.render();
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }

    public Vector3f getNormal() {
        return normal;
    }


    void setup(SkyboxParameters parameters) {

        final int textureId = createAndLoadTexture(GL13.GL_TEXTURE0, parameters);

        final TexturedMesh.Builder builder = new TexturedMesh.Builder();
        builder.setTextureId(textureId);
        builder.setRotation(rotation);
        builder.setSize(dist * 2f);
        builder.setTextureWrap(GL12.GL_CLAMP_TO_EDGE);
        builder.setTranslation(translation);

        delegate = builder.build();
        normal = translation.negate(new Vector3f());
    }



    protected int createAndLoadTexture(int textureUnit, SkyboxParameters parameters) {
        final int width = SIZE;
        final int height = SIZE;
        final int[] canvas = new int[width * height];
        createClouds(canvas, width, height, parameters.getNoiseParamClouds());
        createStars(canvas, width, height, parameters.getNoiseParamStars());

        final IntBuffer buffer = BufferUtils.createIntBuffer(width * height);
        buffer.put(canvas);
        buffer.rewind();

        final int texId = GL11.glGenTextures();
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, buffer);

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        return texId;
    }

    protected int[] createClouds(int[] data, int width, int height, PerlinNoiseParameters param) {
        final float persistence = param.getPersistence();
        final int octaves = param.getOctaves();
        final float frequency = param.getFrequency();
        final float w = param.getW();

        final ColorGradient gradient = new ColorGradient(Color.BLACK, Color.BLACK, Color.GRAY);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Vector3f vector = getVector(x, y, width, height);
                final double noise = createNoise(vector.x * frequency, vector.y * frequency, vector.z * frequency, w * frequency, persistence, octaves);
                final Color color = gradient.getColor(noise);
                data[x + y * width] = color.getRGB() << 8;
            }
        }
        return data;
    }

    protected int[] createStars(int[] data, int width, int height, PerlinNoiseParameters param) {
        final float persistence = param.getPersistence();
        final int octaves = param.getOctaves();
        final float frequency = param.getFrequency();
        final float w = param.getW();

        final ColorGradient gradient = new ColorGradient(Color.BLACK, Color.WHITE, Color.BLACK);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Vector3f vector = getVector(x, y, width, height);
                final double noise = createNoise(vector.x * frequency, vector.y * frequency, vector.z * frequency, w * frequency, persistence, octaves);
                if (noise > -0.0001 && noise < +0.0001) {
                    final Color color = gradient.getColor(noise * 1000);
                    data[x + y * width] = color.getRGB() << 8;
                }
            }
        }
        return data;
    }

    protected Vector3f getVector(int x, int y, int width, int height) {
        final float xx = +(x / (width / (dist * 2f)) - dist);
        final float yy = -(y / (height / (dist * 2f)) - dist);
        // normalize to stay on a sphere
        return rotate(new Vector3f(xx, yy, -dist).normalise(new Vector3f()));
    }

    protected Vector3f rotate(Vector3f in) {
        final Vector3f result = new Vector3f();
        SimpleMath.mul(rotation, in, result);
        return result;
    }

    protected Vector3f translate(Vector3f in) {
        final Vector3f result = new Vector3f();
        SimpleMath.add(translation, in, result);
        return result;
    }

    protected double createNoise(float x, float y, float z, float w, float persistence, int octaves) {
        double result = 0;
        float max = 0;
        for (int i = 0; i < octaves; i++) {
            final float frequency = (float) Math.pow(2, i);
            final float amplitude = (float) Math.pow(persistence, i);
            result += createNoise(x, y, z, w, amplitude, frequency);
            max += amplitude;
        }
        return result / max;
    }

    protected double createNoise(float x, float y, float z, float w, float amplitude, float frequency) {
        // the noise returns [-1 .. +1]
        final double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency, w * frequency);
        return amplitude * noise;
    }

}
