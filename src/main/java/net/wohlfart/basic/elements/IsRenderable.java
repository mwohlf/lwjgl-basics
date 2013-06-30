package net.wohlfart.basic.elements;

import org.lwjgl.util.Renderable;

/**
 * The IsRenderable interface is implemented by anything
 * that can be shown in a graphic context.
 * It also might have some kind of resources that
 * need to be freed by calling destroy().
 */
public interface IsRenderable extends Renderable, HasResouces {

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


    @Override
    void render();

}
