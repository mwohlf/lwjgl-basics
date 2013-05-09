package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.widgets.AbstractTextComponent;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;

public class NullHud implements Hud {

    @Override
    public void render() {
        // do nothing
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void setGraphicContext(IGraphicContext hudContext) {
        // do nothing
    }

    @Override
    public void add(AbstractTextComponent label) {
        // do nothing
    }

}
