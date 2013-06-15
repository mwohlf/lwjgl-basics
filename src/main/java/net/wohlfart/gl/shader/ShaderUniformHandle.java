package net.wohlfart.gl.shader;

import static net.wohlfart.gl.shader.GraphicContextManager.INSTANCE;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * All handlers for uniforms used in any shader, this is not typesafe, you should really know
 * what you are doing when setting uniform parameters in the shaders...
 * </p>
 */
public enum ShaderUniformHandle {// @formatter:off
    MODEL_TO_WORLD("modelToWorldMatrix"),
    WORLD_TO_CAM("worldToCameraMatrix"),  // model view matrix
    CAM_TO_CLIP("cameraToClipMatrix"),    // projection matrix
    NORMAL_MATRIX("normalMatrix"),
    LIGHT_POSITION("lightPosition"),
    LIGHTS("lights"),
    ;
    // @formatter:on

    private static final Logger LOGGER = LoggerFactory.getLogger(PerspectiveProjection.class);


    private final String lookupString;

    private final FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4);
    private final FloatBuffer matrix4Buffer = BufferUtils.createFloatBuffer(16);
    private final FloatBuffer matrix3Buffer = BufferUtils.createFloatBuffer(9);
    private final FloatBuffer lightSourceBuffer = BufferUtils.createFloatBuffer(15);


    private class LightSource {
        Vector4f ambient;
        Vector4f diffuse;
        Vector4f specular;
        Vector3f position;
    };

    ShaderUniformHandle(String lookupString) {
        this.lookupString = lookupString;
    }

    public int getLocation() {
        return INSTANCE.getUniformLocation(lookupString);
    }


    // see: http://www.lwjgl.org/wiki/index.php?title=GLSL_Tutorial:_Communicating_with_Shaders
    public void set(LightSource lightSource) {

    }


    /**
     * <p>
     * set.
     * </p>
     *
     * @param matrix
     *            a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    public void set(Matrix4f matrix) {
        matrix.store(matrix4Buffer);
        matrix4Buffer.flip();
        GL20.glUniformMatrix4(getLocation(), false, matrix4Buffer);  // todo: there is a 4f version
    }

    /**
     * <p>
     * set.
     * </p>
     *
     * @param matrix
     *            a {@link org.lwjgl.util.vector.Matrix3f} object.
     */
    public void set(Matrix3f matrix) {
        matrix.store(matrix3Buffer);
        matrix3Buffer.flip();
        GL20.glUniformMatrix3(getLocation(), false, matrix3Buffer);
    }

    /**
     * <p>
     * set.
     * </p>
     *
     * @param matrix
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void set(Vector3f vector) {
        GL20.glUniform3f(getLocation(), vector.x, vector.y, vector.z);
    }

    /**
     * <p>
     * set.
     * </p>
     *
     * @param readableColor
     *            a {@link org.lwjgl.util.ReadableColor} object.
     */
    public void set(ReadableColor readableColor) {
        colorBuffer.put(new float[] {// @formatter:off
                readableColor.getRed() / 255f,
                readableColor.getGreen() / 255f,
                readableColor.getBlue() / 255f,
                readableColor.getAlpha() / 255f, }); // @formatter:on
        colorBuffer.flip();
        set(colorBuffer);
    }

    /**
     * <p>
     * set.
     * </p>
     *
     * @param colorBuffer
     *            a {@link java.nio.FloatBuffer} object.
     */
    public void set(FloatBuffer colorBuffer) {
        GL20.glUniform4(getLocation(), colorBuffer);
    }

    public Map<String, Integer> getLocationMap(IShaderProgram shaderProgram) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        final int location = GL20.glGetUniformLocation(shaderProgram.getProgramId(), lookupString);
        result.put(lookupString, location);
        if (location < 0) {
            LOGGER.debug("location for UniformHandle '{}' is '{}' wich is <0, the shaderProgram is '{}'",
                    new Object[] { lookupString, location, shaderProgram });
        } else {
            LOGGER.debug("uniformMap lookup: '{}' => '{}'", new Object[] { lookupString, location });
        }
        return result;
    }

}
