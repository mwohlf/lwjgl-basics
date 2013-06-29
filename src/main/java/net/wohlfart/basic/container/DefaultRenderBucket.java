package net.wohlfart.basic.container;

import java.util.Collection;

import net.wohlfart.basic.elements.IsRenderable;

/**
 * a set of IsRenderable object that use the same graphicContext and camera object
 */
@SuppressWarnings("serial")
public class DefaultRenderBucket extends DefaultRenderSet<IsRenderable> {


    public void setContent(Collection<IsRenderable> newContent) {
        for (final IsRenderable isRenderable : this) {
            isRenderable.destroy();
        }
        clear();
        addAll(newContent);
    }

    /*
    @Override
    public void render() {
        super.prepareRender();
        for (final IsRenderable isRenderable : this) {
            isRenderable.render();
        }
    }
    */
    @Override
    public void destroy() {
        for (final IsRenderable isRenderable : this) {
            isRenderable.destroy();
        }
        clear();
    }

}
