package net.wohlfart.gl.elements.hud;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.IsUpdateable;
import net.wohlfart.gl.elements.hud.Layer.Widget;

public interface Hud extends IsRenderable, IsUpdateable {

    void setup();

    @Override
    void destroy();

    void add(Widget label);

    @Override
    void update(float tpf);

}
