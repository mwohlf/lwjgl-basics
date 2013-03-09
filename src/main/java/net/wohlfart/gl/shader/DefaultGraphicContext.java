package net.wohlfart.gl.shader;

import java.util.Arrays;

import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * modeling an OpenGL Context: - shader - attribute location - uniform locations
 *
 * see: http://www.opengl.org/wiki/OpenGL_Context
 *
 *
 *
 */
public class DefaultGraphicContext implements GraphicContextManager.IGraphicContext {
    /** Constant <code>LOGGER</code> */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultGraphicContext.class);

    private final IShaderProgram shaderProgram;

    private final int[] attributeMap = new int[ShaderAttributeHandle.values().length];
    private final int[] uniformMap = new int[ShaderUniformHandle.values().length];

    /**
     * <p>Constructor for DefaultGraphicContext.</p>
     *
     * @param shaderProgram a {@link net.wohlfart.gl.shader.IShaderProgram} object.
     */
    public DefaultGraphicContext(IShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
        this.shaderProgram.setup();
        this.shaderProgram.bind();
        initMaps();
        this.shaderProgram.unbind();
    }

    // read uniforms and attribute locations and buffer them
    private void initMaps() {
        final int programId = shaderProgram.getProgramId();
        LOGGER.debug("init gfx context, shader.programId: '{}'", programId);

        for (final ShaderAttributeHandle attributeHandle : ShaderAttributeHandle.values()) {
            // int location = attributeHandle.getLocation(shaderProgram);
            final int location = GL20.glGetAttribLocation(programId, attributeHandle.getLookupString());
            attributeMap[attributeHandle.ordinal()] = location;
            if (location < 0) {
                LOGGER.warn("location for AttributeHandle '{}' is '{}' wich is <0, the programId is '{}'",
                        new Object[] { attributeHandle, location, programId });
            } else {
                LOGGER.debug("attributeMap lookup: '{}({})' to '{}'", new Object[] { attributeHandle.name(), attributeHandle.ordinal(), location });
            }
        }
        LOGGER.debug("attributeMap setup: '{}'", Arrays.toString(attributeMap));

        for (final ShaderUniformHandle matrixHandle : ShaderUniformHandle.values()) {
            // int location = matrixHandle.getLocation(shaderProgram);
            final int location = GL20.glGetUniformLocation(programId, matrixHandle.getLookupString());
            uniformMap[matrixHandle.ordinal()] = location;
            if (location < 0) {
                LOGGER.warn("location for UniformHandle '{}' is '{}' wich is <0, the programId is '{}'", new Object[] { matrixHandle, location, programId });
            } else {
                LOGGER.debug("matrixMap lookup: '{}({})' to '{}'", new Object[] { matrixHandle.name(), matrixHandle.ordinal(), location });
            }
        }
        LOGGER.debug("matrixMap setup: '{}'", Arrays.toString(uniformMap));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "DefaultGraphicContext with shader: " + shaderProgram;
    }

    /** {@inheritDoc} */
    @Override
    public void bind() {
        shaderProgram.bind();
    }

    /** {@inheritDoc} */
    @Override
    public void unbind() {
        shaderProgram.unbind();
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        shaderProgram.unbind();
        shaderProgram.dispose();
    }

    /** {@inheritDoc} */
    @Override
    public int getLocation(ShaderAttributeHandle shaderAttributeHandle) {
        return attributeMap[shaderAttributeHandle.ordinal()];
    }

    /** {@inheritDoc} */
    @Override
    public int getLocation(ShaderUniformHandle shaderUniformHandle) {
        return uniformMap[shaderUniformHandle.ordinal()];
    }

}
