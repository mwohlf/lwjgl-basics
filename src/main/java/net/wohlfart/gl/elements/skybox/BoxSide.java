package net.wohlfart.gl.elements.skybox;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.wohlfart.gl.elements.skybox.SkyboxParameters.PerlinNoiseParameters;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.tools.ColorGradient;
import net.wohlfart.gl.tools.SimplexNoise;
import net.wohlfart.gl.tools.Vertex;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

enum BoxSide {
    PLUS_Y {
        {
            translation = new Vector3f(0, +dist, 0);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }
    },
    MINUS_Y {
        {
            translation = new Vector3f(0, -dist, 0);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }
    },
    PLUS_X {
        {
            translation = new Vector3f(+dist, 0, 0);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }
    },
    MINUS_X {
        {
            translation = new Vector3f(-dist, 0, 0);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }
    },
    PLUS_Z {
        {
            translation = new Vector3f(0, 0, +dist);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }
    },
    MINUS_Z {
        {
            translation = new Vector3f(0, 0, -dist);
            rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, -dist), translation, new Quaternion());
        }
    };

    private static final float dist = 1f; // distance from the origin to the wall of the skybox

    protected Quaternion rotation;
    protected Vector3f translation;

    // main entry point
    BoxSideMesh build(SkyboxParameters parameters) {

        final Vertex[] vertices = new Vertex[] {
                // We'll define our quad using 4 vertices of the custom 'Vertex'
                // class
                new Vertex() {
                    {
                        setXYZ(translate(rotate(new Vector3f(-dist, +dist, 0f))));
                        setRGB(1, 1, 1);
                        setST(0, 0);
                    }
                }, new Vertex() {
                    {
                        setXYZ(translate(rotate(new Vector3f(-dist, -dist, 0f))));
                        setRGB(1, 1, 1);
                        setST(0, 1);
                    }
                }, new Vertex() {
                    {
                        setXYZ(translate(rotate(new Vector3f(+dist, -dist, 0f))));
                        setRGB(1, 1, 1);
                        setST(1, 1);
                    }
                }, new Vertex() {
                    {
                        setXYZ(translate(rotate(new Vector3f(+dist, +dist, 0f))));
                        setRGB(1, 1, 1);
                        setST(1, 0);
                    }
                } };

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

        final int positionAttrib = ShaderAttributeHandle.POSITION.getLocation();
        final int colorAttrib = ShaderAttributeHandle.COLOR.getLocation();
        final int textureAttrib = ShaderAttributeHandle.TEXTURE_COORD.getLocation();

        // Create a new Vertex Buffer Object in memory and select it (bind)
        final int vboVerticesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        // Put the positions in attribute list 0
        GL20.glVertexAttribPointer(positionAttrib, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
        // Put the colors in attribute list 1
        GL20.glVertexAttribPointer(colorAttrib, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
        // Put the texture in attribute list 2
        GL20.glVertexAttribPointer(textureAttrib, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
        // unbind
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);

        // Create a new VBO for the indices and select it (bind) - INDICES
        final int vboIndicesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        // load the texture
        final int textureId = createAndLoadTexture(GL13.GL_TEXTURE0, parameters); // FIXME: check if we coudl use a different texture unit

        return new BoxSideMesh(vaoHandle, vboVerticesHandle, vboIndicesHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0, colorAttrib,
                positionAttrib, textureAttrib, textureId, translation.negate(new Vector3f()));
    }

    protected int createAndLoadTexture(int textureUnit, SkyboxParameters parameters) {
        final int width = parameters.getSize();
        final int height = parameters.getSize();
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

    // translate from the plane coords to 3d
    protected Vector3f getVector(int x, int y, int width, int height) {
        // normalize
        final float xx = +(x / (width / (dist * 2f)) - dist);
        final float yy = -(y / (height / (dist * 2f)) - dist);
        // need to normalize to stay on the sphere
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

    protected double createNoise(final float x, final float y, final float z, final float w, final float persistence, final int octaves) {
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

    protected double createNoise(final float x, final float y, final float z, final float w, final float amplitude, final float frequency) {
        // the noise returns [-1 .. +1]
        final double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency, w * frequency);
        return amplitude * noise;
    }

}
