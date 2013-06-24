package net.wohlfart.gl.renderer;

import org.lwjgl.util.Renderable;

/**
 * <p>
 * The IsRenderable interface is implemented by anything that can be shown in a graphic context.<br/>
 * <br/>
 * It also might have some kind of resources that need to be freed by calling destroy().
 * </p>
 */
public interface IsRenderable extends Renderable {

    /**
     * Constant <code>NULL</code> is a do "nothing renderable", which is useful to avoid null checking
     */
    public IsRenderable NULL = new IsRenderable() {
        @Override
        public void render() {
        }

        @Override
        public void destroy() {
        }
    };

    /**
     * <p>
     * Display this IsRenderable in the current graphic context.
     * </p>
     */
    @Override
    public void render();

    /**
     * <p>
     * Called after this IsRenderable is no longer in use, frees all resources needed by this renderable.
     * </p>
     */
    public void destroy();

}
