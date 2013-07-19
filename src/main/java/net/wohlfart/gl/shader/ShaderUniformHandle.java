package net.wohlfart.gl.shader;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All handlers for uniforms used in any shader, this is not typesafe, you should really know
 * what you are doing when setting uniform parameters in the shaders.
 * note that the uniforms are optimized out at compile time if they are not used in the shader,
 * in this case their location will be -1.
 *
 * getLocationMap is called when initializing the graphic context and is supposed to
 * return the name of the uniform mapped to the location
 */
public enum ShaderUniformHandle {// @formatter:off

    MODEL_TO_WORLD("modelToWorldMatrix") {},

    WORLD_TO_CAM("worldToCameraMatrix") {},  // model view matrix

    CAM_TO_CLIP("cameraToClipMatrix") {},    // projection matrix

    NORMAL_MATRIX("normalMatrix") {},

    LIGHT_POSITION("lightPosition") {},

    MATERIAL() {
        @Override Map<String, Integer> getLocationMap(IShaderProgram shaderProgram) {
            return super.getLocationMap(shaderProgram,
                    MATERIAL_AMBIENT, MATERIAL_DIFFUSE, MATERIAL_SPECULAR, MATERIAL_SHININESS);
        }
    },


    LIGHTS() {
        @Override Map<String, Integer> getLocationMap(IShaderProgram shaderProgram) {
            HashMap<String, Integer> result = new HashMap<String, Integer>();
            for (int i = 0; i < MAX_LIGHT_SOURCES; i++) {
                result.putAll(super.getLocationMap(shaderProgram,
                        LIGHT_ATTENTUATION_KEYS[i],
                        LIGHT_DIFFUSE_KEYS[i],
                        LIGHT_POSITION_KEYS[i]));
            }
            return result;
        }
    }
    ;
    // @formatter:on
    private static final Logger LOGGER = LoggerFactory.getLogger(ShaderUniformHandle.class);
    private static final int MAX_LIGHT_SOURCES = 10;  // must match with the shader

    private static final String[] LIGHT_ATTENTUATION_KEYS = new String[MAX_LIGHT_SOURCES];
    private static final String[] LIGHT_DIFFUSE_KEYS = new String[MAX_LIGHT_SOURCES];
    private static final String[] LIGHT_POSITION_KEYS = new String[MAX_LIGHT_SOURCES];

    static { // setting up the strings so we don't need to concat at runtime in the render loop
        for (int i = 0; i < MAX_LIGHT_SOURCES; i++) {
            LIGHT_ATTENTUATION_KEYS[i] = "lights[" + i + "].attenuation";
            LIGHT_DIFFUSE_KEYS[i] = "lights[" + i + "].diffuse";
            LIGHT_POSITION_KEYS[i] = "lights[" + i + "].position";
        }
    }

    private static final String MATERIAL_AMBIENT = "material.ambient";
    private static final String MATERIAL_DIFFUSE = "material.diffuse";
    private static final String MATERIAL_SPECULAR = "material.specular";
    private static final String MATERIAL_SHININESS = "material.shininess";



    private final String lookupString;

    ShaderUniformHandle() {
        this.lookupString = null;
    }

    ShaderUniformHandle(String lookupString) {
        this.lookupString = lookupString;
    }



    // resolve all locations for this uniform, a uniform might have multiple locations
    // for example an array or struct uses multiple names mapped to different locations
    // this must be overridden if a handle has multipl  locations
    Map<String, Integer> getLocationMap(IShaderProgram shaderProgram) {
        return getLocationMap(shaderProgram, lookupString);
    }

    // this is the main method to resolve uniforms to their positions in the shader
    // returns a map from uniform name to position in the shader,
    private Map<String, Integer> getLocationMap(IShaderProgram shaderProgram, String... lookupStrings) {
        final HashMap<String, Integer> result = new HashMap<String, Integer>();
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


    // ---------------------------------------------------------------------------------------------------------
    //                       setter for uniform objects
    // ---------------------------------------------------------------------------------------------------------



    // see: http://www.lwjgl.org/wiki/index.php?title=GLSL_Tutorial:_Communicating_with_Shaders
    public void set(VertexLight vertexLight, int index) {
        int location;

        location = CONTEXT_HOLDER.getUniformLocation(LIGHT_ATTENTUATION_KEYS[index]);
        if (location > 0) {
            GL20.glUniform1f(location, vertexLight.attenuation);
        }

        location = CONTEXT_HOLDER.getUniformLocation(LIGHT_DIFFUSE_KEYS[index]);
        if (location > 0) {
            GL20.glUniform4f(location, vertexLight.diffuse.x, vertexLight.diffuse.y, vertexLight.diffuse.z, vertexLight.diffuse.w);
        }

        location = CONTEXT_HOLDER.getUniformLocation(LIGHT_POSITION_KEYS[index]);
        if (location > 0) {
            // FIXME: for performance reasons we could transform the light's position here, its done in the shader right now
            // INSTANCE.getPerspectiveProjMatrix()
            GL20.glUniform3f(location, vertexLight.position.x, vertexLight.position.y, vertexLight.position.z);
        }
    }

    public void set(Material material) {
        int location;

        location = CONTEXT_HOLDER.getUniformLocation(MATERIAL_AMBIENT);
        if (location > 0) {
            GL20.glUniform3f(location, material.ambient.x, material.ambient.y, material.ambient.z);
        }
        location = CONTEXT_HOLDER.getUniformLocation(MATERIAL_DIFFUSE);
        if (location > 0) {
            GL20.glUniform4f(location, material.diffuse.x, material.diffuse.y, material.diffuse.z, material.diffuse.w);
        }
        location = CONTEXT_HOLDER.getUniformLocation(MATERIAL_SPECULAR);
        if (location > 0) {
            GL20.glUniform3f(location, material.specular.x, material.specular.y, material.specular.z);
        }
        location = CONTEXT_HOLDER.getUniformLocation(MATERIAL_SHININESS);
        if (location > 0) {
            GL20.glUniform1f(location, material.shininess);
        }
    }



    private final static ThreadLocal<FloatBuffer> matrix2Buffer = new ThreadLocal<FloatBuffer>() {{ set(BufferUtils.createFloatBuffer(4)); }};
    public void set(Matrix2f matrix) {
        matrix.store(matrix2Buffer.get());
        GL20.glUniformMatrix3(CONTEXT_HOLDER.getUniformLocation(lookupString), false, (FloatBuffer)matrix2Buffer.get().flip());
    }

    private final static ThreadLocal<FloatBuffer> matrix3Buffer = new ThreadLocal<FloatBuffer>() {{ set(BufferUtils.createFloatBuffer(9)); }};
    public void set(Matrix3f matrix) {
        matrix.store(matrix3Buffer.get());
        GL20.glUniformMatrix3(CONTEXT_HOLDER.getUniformLocation(lookupString), false, (FloatBuffer)matrix3Buffer.get().flip());
    }

    private final static ThreadLocal<FloatBuffer> matrix4Buffer = new ThreadLocal<FloatBuffer>() {{ set(BufferUtils.createFloatBuffer(16)); }};
    public void set(Matrix4f matrix) {
        matrix.store(matrix4Buffer.get());
        GL20.glUniformMatrix4(CONTEXT_HOLDER.getUniformLocation(lookupString), false, (FloatBuffer)matrix4Buffer.get().flip());
    }



    public void set(Vector2f vector) {
        GL20.glUniform2f(CONTEXT_HOLDER.getUniformLocation(lookupString), vector.x, vector.y);
    }

    public void set(Vector3f vector) {
        GL20.glUniform3f(CONTEXT_HOLDER.getUniformLocation(lookupString), vector.x, vector.y, vector.z);
    }

    public void set(Vector4f vector) {
        GL20.glUniform4f(CONTEXT_HOLDER.getUniformLocation(lookupString), vector.x, vector.y, vector.z, vector.w);
    }

    public void set(ReadableColor readableColor) {
        GL20.glUniform4f(CONTEXT_HOLDER.getUniformLocation(lookupString),
                readableColor.getRed() / 255f,
                readableColor.getGreen() / 255f,
                readableColor.getBlue() / 255f,
                readableColor.getAlpha() / 255f );
    }

}
