package net.wohlfart.basic.hud;

import net.wohlfart.basic.hud.Layer.Widget;

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
    public void add(Widget label) {
        throw new UnsupportedOperationException("can't add element to null hud");
    }

    @Override
    public void update(float tpf) {
        // do nothing
    }

}
