package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.widgets.AbstractTextComponent;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;

public interface Hud extends IsRenderable {

    void setGraphicContext(IGraphicContext hudContext);

    void add(AbstractTextComponent label);

}
