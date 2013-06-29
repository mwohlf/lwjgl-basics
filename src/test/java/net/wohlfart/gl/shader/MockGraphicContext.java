package net.wohlfart.gl.shader;

/**
 * <p>
 * MockGraphicContext class.
 * </p>
 */
public class MockGraphicContext implements GraphicContextManager.IGraphicContext {

    @Override
    public Integer getAttributeLocation(String lookupString) {
        return 0;
    }

    @Override
    public Integer getUniformLocation(String lookupString) {
        return 0;
    }

    @Override
    public void bind() {
    }

    @Override
    public boolean isInitialized() {
        return true;
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
