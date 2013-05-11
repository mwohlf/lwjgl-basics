package net.wohlfart.gl.elements.skybox;

import net.wohlfart.gl.renderer.IsRenderable;

public interface Skybox extends IsRenderable {

    public void setup();

    public void dispose();

}
