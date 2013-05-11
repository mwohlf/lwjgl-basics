package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.widgets.AbstractTextComponent;
import net.wohlfart.gl.renderer.IsRenderable;

public interface Hud extends IsRenderable {

    void setup();

    void dispose();

    void add(AbstractTextComponent label);

}
