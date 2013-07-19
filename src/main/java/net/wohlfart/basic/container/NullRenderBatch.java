package net.wohlfart.basic.container;

import net.wohlfart.basic.elements.IsRenderable;

public enum NullRenderBatch implements RenderBatch<IsRenderable> {
    INSTANCE;

    @Override
    public void setup() {
    }

    @Override
    public void render() {
    }

    @Override
    public void destroy() {
    }

}
