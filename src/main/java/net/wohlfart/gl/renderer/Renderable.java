package net.wohlfart.gl.renderer;

/**
 * <p>Renderable interface implemented by anything that can be shown in a graphic context.</p>
 */
public interface Renderable extends org.lwjgl.util.Renderable {

    /** Constant <code>NULL</code>, a do nothing renderer,
     *  useful to avoid null checking */
    public Renderable NULL = new Renderable() {
        @Override
        public void render() {}
        @Override
        public void dispose() {}

    };

    /**
     * <p>Display this Renderable in the current graphic context.</p>
     */
    @Override
    public abstract void render();

    /**
     * <p>Called after this Renderable is no longer in use,
     * frees all resources needed by this renderable.</p>
     */
    public abstract void dispose();

}
