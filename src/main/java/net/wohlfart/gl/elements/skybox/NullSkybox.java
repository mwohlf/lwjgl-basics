package net.wohlfart.gl.elements.skybox;

import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.view.CanRotate;

public class NullSkybox implements Skybox {

    @Override
    public void render() {
        // do nothing
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void setGraphicContext(IGraphicContext graphicContext) {
        // do nothing
    }

    @Override
    public void setCamera(CanRotate camera) {
        // do nothing
    }

}
