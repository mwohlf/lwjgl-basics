package net.wohlfart.gl.renderer;

/**
 * <p>Renderable interface.</p>
 *
 *
 *
 */
public interface Renderable {

    // a do nothing renderer
    /** Constant <code>NULL</code> */
    public Renderable NULL = new Renderable() {
        @Override
        public void render() {}
        @Override
        public void dispose() {}

    };


    /**
     * <p>render.</p>
     */
    public abstract void render();

    /**
     * <p>dispose.</p>
     */
    public abstract void dispose();


}
