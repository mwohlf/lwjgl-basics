package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.widgets.AbstractTextComponent;

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
    public void add(AbstractTextComponent label) {
        // do nothing
    }

    @Override
    public void dispose() {
        // do nothing
    }

}
