package net.wohlfart.gl.elements.skybox;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.view.CanRotate;

public interface Skybox extends IsRenderable {

    public void setGraphicContext(IGraphicContext graphicContext);

    public void setCamera(CanRotate camera);

}
