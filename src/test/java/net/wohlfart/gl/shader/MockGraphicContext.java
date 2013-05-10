package net.wohlfart.gl.shader;

/**
 * <p>MockGraphicContext class.</p>
 */
public class MockGraphicContext implements GraphicContextManager.IGraphicContext {

    @Override
    public int getLocation(ShaderAttributeHandle shaderAttributeHandle) {
        return 0;
    }

    @Override
    public int getLocation(ShaderUniformHandle shaderUniformHandle) {
        return 0;
    }

    @Override
    public void bind() {
    }

    @Override
    public void unbind() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void setup() {

    }

}
