package net.wohlfart.gl.elements.skybox;

public enum NullSkybox implements Skybox {
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
    public void dispose() {
        // do nothing
    }

}
