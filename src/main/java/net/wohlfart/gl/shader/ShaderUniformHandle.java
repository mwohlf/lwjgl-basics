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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * All handlers for uniforms used in any shader, this is not typesafe, you should really know
 * what you are doing when setting uniform parameters in the shaders.
 * NOte that the uniforms are optimized out at compile time if they are not used in the shader,
 * in this case their location will be -1.
 * </p>
 */
public enum ShaderUniformHandle {// @formatter:off
    MODEL_TO_WORLD("modelToWorldMatrix"),
    WORLD_TO_CAM("worldToCameraMatrix"),  // model view matrix
    CAM_TO_CLIP("cameraToClipMatrix"),    // projection matrix
    NORMAL_MATRIX("normalMatrix"),
    LIGHT_POSITION("lightPosition"),  // FIXME: where is the texture?
    LIGHTS(null) {
        @Override
        Map<String, Integer> getLocationMap(IShaderProgram shaderProgram) {
            HashMap<String, Integer> result = new HashMap<String, Integer>();
            for (int i = 0; i < MAX_LIGHT_SOURCES; i++) {
                result.putAll(super.getLocationMap(shaderProgram,
                        "lights[" + i + "].attenuation",
                        "lights[" + i + "].diffuse",
                        "lights[" + i + "].position"));
            }
            return result;
        }
    },
    ;
    // @formatter:on
    private static final Logger LOGGER = LoggerFactory.getLogger(ShaderUniformHandle.class);
    private static final int MAX_LIGHT_SOURCES = 10;  // must match with the shader

    private final String lookupString;

    // FIXME: remove these:
    private final FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4);
    private final FloatBuffer matrix4Buffer = BufferUtils.createFloatBuffer(16);
    private final FloatBuffer matrix3Buffer = BufferUtils.createFloatBuffer(9);


    ShaderUniformHandle(String lookupString) {
        this.lookupString = lookupString;
    }

    Map<String, Integer> getLocationMap(IShaderProgram shaderProgram) {
        return getLocationMap(shaderProgram, lookupString);
    }

    private Map<String, Integer> getLocationMap(IShaderProgram shaderProgram, String... lookupStrings) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        for (String lookupString : lookupStrings) {
            final int location = GL20.glGetUniformLocation(shaderProgram.getProgramId(), lookupString);
            result.put(lookupString, location);
            if (location < 0) {
                LOGGER.debug("location for UniformHandle '{}' is '{}' wich is <0, the shaderProgram is '{}'",
                        new Object[] { lookupString, location, shaderProgram });
            } else {
                LOGGER.debug("uniformMap lookup: '{}' => '{}'", new Object[] { lookupString, location });
            }
        }
        return result;
    }

    // see: http://www.lwjgl.org/wiki/index.php?title=GLSL_Tutorial:_Communicating_with_Shaders
    public void set(VertexLight vertexLight, int index) {
        Integer location;

        location = INSTANCE.getUniformLocation("lights[" + index + "].attenuation");
        if (location != null) {
            GL20.glUniform1f(location, vertexLight.attenuation);
        }

        location = INSTANCE.getUniformLocation("lights[" + index + "].diffuse");
        if (location != null) {
            GL20.glUniform4f(location, vertexLight.diffuse.x, vertexLight.diffuse.y, vertexLight.diffuse.z, vertexLight.diffuse.w);
        }

        location = INSTANCE.getUniformLocation("lights[" + index + "].position");
        if (location != null) {
            // FIXME: we need to transform the light's position here, its done in the shader rigth now
            // INSTANCE.getPerspectiveProjMatrix()
            GL20.glUniform3f(location, vertexLight.position.x, vertexLight.position.y, vertexLight.position.z);
        }

    }

    public void set(Matrix4f matrix) {
        matrix.store(matrix4Buffer);
        matrix4Buffer.flip();
        // NPE here could mean that the graphic context was not yet initialized
        GL20.glUniformMatrix4(INSTANCE.getUniformLocation(lookupString), false, matrix4Buffer);  // todo: there is a 4f version?
    }

    public void set(Matrix3f matrix) {
        matrix.store(matrix3Buffer);
        matrix3Buffer.flip();
        // NPE here could mean that the graphic context was not yet initialized
        GL20.glUniformMatrix3(INSTANCE.getUniformLocation(lookupString), false, matrix3Buffer);
    }

    public void set(Vector3f vector) {
        GL20.glUniform3f(INSTANCE.getUniformLocation(lookupString), vector.x, vector.y, vector.z);
    }

    public void set(ReadableColor readableColor) {
        colorBuffer.put(new float[] {// @formatter:off
                readableColor.getRed() / 255f,
                readableColor.getGreen() / 255f,
                readableColor.getBlue() / 255f,
                readableColor.getAlpha() / 255f, }); // @formatter:on
        colorBuffer.flip();
        set(colorBuffer);
    }

    public void set(FloatBuffer colorBuffer) {
        GL20.glUniform4(INSTANCE.getUniformLocation(lookupString), colorBuffer);
    }

}
