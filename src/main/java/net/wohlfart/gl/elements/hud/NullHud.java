package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.Layer.LayerElement;

public enum NullHud implements Hud {
    INSTANCE;

    @Override
    public void render() {
        // do nothing
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void setup() {
        // do nothing
    }

    @Override
    public void add(LayerElement label) {
        throw new UnsupportedOperationException("can't add element to null hud");
    }

    @Override
    public void dispose() {
        // do nothing
    }

    @Override
    public void update(float tpf) {
        // do nothing
    }

}
