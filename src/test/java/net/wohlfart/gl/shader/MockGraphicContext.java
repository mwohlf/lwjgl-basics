package net.wohlfart.gl.shader;

/**
 * <p>MockGraphicContext class.</p>
 */
public class MockGraphicContext implements GraphicContextManager.IGraphicContext {

    /** {@inheritDoc} */
    @Override
    public int getLocation(ShaderAttributeHandle shaderAttributeHandle) {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public int getLocation(ShaderUniformHandle shaderUniformHandle) {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public void bind() {
    }

    /** {@inheritDoc} */
    @Override
    public void unbind() {
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
    }

}
