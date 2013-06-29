package net.wohlfart.gl.shader;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @formatter:off modeling an OpenGL Context: - shader - attribute location - uniform locations
 * @formatter:on
 *
 *               see: http://www.opengl.org/wiki/OpenGL_Context
 *
 */
public class DefaultGraphicContext implements GraphicContextManager.IGraphicContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGraphicContext.class);

    private final IShaderProgram shaderProgram;

    private final HashMap<String, Integer> attributeMap = new HashMap<String, Integer>();
    private final HashMap<String, Integer> uniformMap = new HashMap<String, Integer>();

    private boolean isInitialized = false;

    /**
     * <p>
     * Constructor for DefaultGraphicContext.
     * </p>
     *
     * @param shaderProgram
     *            a {@link net.wohlfart.gl.shader.IShaderProgram} object.
     */
    public DefaultGraphicContext(IShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    /**
     * this needs to be called after the OpenGL setup is done
     * this method might be called multiple times if the same shader is used in multiple states
     */
    @Override
    public void setup() {
        if (isInitialized) {
            return;
        }
        this.shaderProgram.setup();
        this.shaderProgram.bind();
        initMaps();
        this.shaderProgram.unbind();
        isInitialized = true;
    }

    // read uniforms and attribute locations and buffer them
    private void initMaps() {
        LOGGER.debug("init gfx context, shader: '{}'", shaderProgram);

        attributeMap.clear();
        for (final ShaderAttributeHandle attributeHandle : ShaderAttributeHandle.values()) {
            attributeMap.putAll(attributeHandle.getLocationMap(shaderProgram));
        }
        LOGGER.debug("finished attributeMap setup for shader {}: '{}'", shaderProgram, attributeMap);

        uniformMap.clear();
        for (final ShaderUniformHandle uniformHandle : ShaderUniformHandle.values()) {
            uniformMap.putAll(uniformHandle.getLocationMap(shaderProgram));
        }

        LOGGER.debug("finished uniformMap setup for shader {}: '{}'", shaderProgram, attributeMap);
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public String toString() {
        return "DefaultGraphicContext with shader: " + shaderProgram;
    }

    @Override
    public void bind() {
        shaderProgram.bind();
    }

    @Override
    public void unbind() {
        shaderProgram.unbind();
    }

    @Override
    public void dispose() {
        shaderProgram.unbind();
        shaderProgram.dispose();
    }

    @Override
    public Integer getAttributeLocation(String attributeName) {
        return attributeMap.get(attributeName);
    }

    @Override
    public Integer getUniformLocation(String uniformName) {
        return uniformMap.get(uniformName);
    }

}
