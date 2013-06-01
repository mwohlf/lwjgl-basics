package net.wohlfart.gl.renderer;

import net.wohlfart.gl.view.HasMatrices;

public interface RenderBucket extends IsRenderable, IsUpdateable, HasMatrices {

    public void addContent(IsRenderable renderable);

    public void setup();

}
