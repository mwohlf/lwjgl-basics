package net.wohlfart.gl.elements.skybox;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.wohlfart.gl.elements.skybox.SkyboxParameters.PerlinNoiseParameters;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.tools.ColorGradient;
import net.wohlfart.tools.SimpleMath;
import net.wohlfart.tools.SimplexNoise;
import net.wohlfart.tools.Vertex;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

// FIXME: no color needed
enum BoxSide {// @formatter:off
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
    private static final int SIZE = 1024;

    // distance from the origin to the wall of the skybox and also the length of the edge
    protected float dist = SimpleMath.sqrt(SimpleMath.sqare(GraphicContextManager.INSTANCE.getNearPlane())
            + SimpleMath.sqare(GraphicContextManager.INSTANCE.getNearPlane()));
    protected Vector3f translation;
    protected Quaternion rotation;

    // main entry point
    BoxSideMesh build(SkyboxParameters parameters) {

        final Vertex[] vertices = new Vertex[] {
// @formatter:off
                new Vertex() {{
                        setXYZ(translate(rotate(new Vector3f(-dist, +dist, 0f))));
                        setRGB(1, 1, 1);
                        setST(0, 0);
                    }},
                new Vertex() {{
                        setXYZ(translate(rotate(new Vector3f(-dist, -dist, 0f))));
                        setRGB(1, 1, 1);
                        setST(0, 1);
                    }},
                new Vertex() {{
                        setXYZ(translate(rotate(new Vector3f(+dist, -dist, 0f))));
                        setRGB(1, 1, 1);
                        setST(1, 1);
                    }},
                new Vertex() {{
                        setXYZ(translate(rotate(new Vector3f(+dist, +dist, 0f))));
                        setRGB(1, 1, 1);
                        setST(1, 0);
                    }} // @formatter:on
        };

        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
        for (int i = 0; i < vertices.length; i++) {
            verticesBuffer.put(vertices[i].getXYZW());
            verticesBuffer.put(vertices[i].getRGBA());
            verticesBuffer.put(vertices[i].getST());
        }
        verticesBuffer.flip();

        // OpenGL expects to draw vertices in counter clockwise order by default
        final byte[] indices = { 0, 1, 2, 2, 3, 0 };
        final int indicesCount = indices.length;
        final ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        // Create a new Vertex Array Object in memory and select it (bind)
        final int vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        // Create a new VBO for the indices and select it (bind) - INDICES
        final int vboIndicesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        final int vboVerticesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        ShaderAttributeHandle.POSITION.enable();
        GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(),
                Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);

        ShaderAttributeHandle.COLOR.enable();
        GL20.glVertexAttribPointer(ShaderAttributeHandle.COLOR.getLocation(),
                Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);

        ShaderAttributeHandle.TEXTURE_COORD.enable();
        GL20.glVertexAttribPointer(ShaderAttributeHandle.TEXTURE_COORD.getLocation(),
                Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);

        ShaderAttributeHandle.NORMAL.disable();

        GL30.glBindVertexArray(0);

        // load the texture
        final int textureId = createAndLoadTexture(GL13.GL_TEXTURE0, parameters); // FIXME: check if we could use a different texture unit

        return new BoxSideMesh(vaoHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0, textureId, translation.negate(new Vector3f()));
    }

    /**
     * <p>
     * createAndLoadTexture.
     * </p>
     *
     * @param textureUnit
     *            a int.
     * @param parameters
     *            a {@link net.wohlfart.gl.elements.skybox.SkyboxParameters} object.
     * @return a int.
     */
    protected int createAndLoadTexture(int textureUnit, SkyboxParameters parameters) {
        final int width = SIZE;
        final int height = SIZE;
        final int[] canvas = new int[width * height];
        createClouds(canvas, width, height, parameters.getNoiseParamClouds());
        createStars(canvas, width, height, parameters.getNoiseParamStars());

        final IntBuffer buffer = BufferUtils.createIntBuffer(width * height);
        buffer.put(canvas);
        buffer.rewind();

        // see: http://lwjgl.org/forum/index.php?topic=2578.0
        final int texId = GL11.glGenTextures();
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ZERO);

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        return texId;
    }

    /**
     * <p>
     * createClouds.
     * </p>
     *
     * @param data
     *            an array of int.
     * @param width
     *            a int.
     * @param height
     *            a int.
     * @param param
     *            a {@link net.wohlfart.gl.elements.skybox.SkyboxParameters.PerlinNoiseParameters} object.
     * @return an array of int.
     */
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

    /**
     * <p>
     * createStars.
     * </p>
     *
     * @param data
     *            an array of int.
     * @param width
     *            a int.
     * @param height
     *            a int.
     * @param param
     *            a {@link net.wohlfart.gl.elements.skybox.SkyboxParameters.PerlinNoiseParameters} object.
     * @return an array of int.
     */
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

    //
    /**
     * <p>
     * translate from the 2D plane coords to 3D
     * </p>
     *
     * @param x
     *            a int.
     * @param y
     *            a int.
     * @param width
     *            a int.
     * @param height
     *            a int.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    protected Vector3f getVector(int x, int y, int width, int height) {
        final float xx = +(x / (width / (dist * 2f)) - dist);
        final float yy = -(y / (height / (dist * 2f)) - dist);
        // normalize to stay on a sphere
        return rotate(new Vector3f(xx, yy, -dist).normalise(new Vector3f()));
    }

    /**
     * <p>
     * rotate.
     * </p>
     *
     * @param in
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    protected Vector3f rotate(Vector3f in) {
        final Vector3f result = new Vector3f();
        SimpleMath.mul(rotation, in, result);
        return result;
    }

    /**
     * <p>
     * translate.
     * </p>
     *
     * @param in
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    protected Vector3f translate(Vector3f in) {
        final Vector3f result = new Vector3f();
        SimpleMath.add(translation, in, result);
        return result;
    }

    /**
     * <p>
     * createNoise.
     * </p>
     *
     * @param x
     *            a float.
     * @param y
     *            a float.
     * @param z
     *            a float.
     * @param w
     *            a float.
     * @param persistence
     *            a float.
     * @param octaves
     *            a int.
     * @return a double.
     */
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

    /**
     * <p>
     * createNoise.
     * </p>
     *
     * @param x
     *            a float.
     * @param y
     *            a float.
     * @param z
     *            a float.
     * @param w
     *            a float.
     * @param amplitude
     *            a float.
     * @param frequency
     *            a float.
     * @return a double.
     */
    protected double createNoise(float x, float y, float z, float w, float amplitude, float frequency) {
        // the noise returns [-1 .. +1]
        final double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency, w * frequency);
        return amplitude * noise;
    }

}
