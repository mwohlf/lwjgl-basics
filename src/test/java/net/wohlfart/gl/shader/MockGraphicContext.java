package net.wohlfart.gl.shader;

/**
 * MockGraphicContext class.
 */
public class MockGraphicContext implements GraphicContextHolder.IGraphicContext {

    @Override
    public int getAttributeLocation(String lookupString) {
        return 0;
    }

    @Override
    public int getUniformLocation(String lookupString) {
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
