package net.wohlfart.gl.renderer;

public interface Renderable {

    public abstract void render();

    public abstract void dispose();

    // do nothing renderer
    public Renderable NULL = new Renderable() {
        @Override
        public void render() {}
        @Override
        public void dispose() {}

    };

}
