package net.wohlfart.gl.elements.hud;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.hud.widgets.AbstractTextComponent;

public interface Hud extends IsRenderable {

    void setup();

    void dispose();

    void add(AbstractTextComponent label);

}
