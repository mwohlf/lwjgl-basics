package net.wohlfart.gl.elements.hud;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.IsUpdateable;
import net.wohlfart.gl.elements.hud.Layer.LayerElement;

public interface Hud extends IsRenderable, IsUpdateable {

    void setup();

    void dispose();

    void add(LayerElement label);

    @Override
    void update(float tpf);

}
