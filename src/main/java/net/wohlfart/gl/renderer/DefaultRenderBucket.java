package net.wohlfart.gl.renderer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * a set of IsRenderable object that use the same graphicContext and camera object
 */
public class DefaultRenderBucket extends AbstractRenderBucket implements RenderBucket {

    protected final Set<IsRenderable> content = new HashSet<>(10100);

    public void setContent(Collection<IsRenderable> newContent) {
        for (final IsRenderable isRenderable : content) {
            isRenderable.destroy();
        }
        content.clear();
        content.addAll(newContent);
    }

    public void addContent(Collection<IsRenderable> newContent) {
        content.addAll(newContent);
    }

    @Override
    public void addContent(IsRenderable renderable) {
        content.add(renderable);
    }

    @Override
    public void render() {
        super.prepareRender();
        for (final IsRenderable isRenderable : content) {
            isRenderable.render();
        }
    }

    @Override
    public void destroy() {
        for (final IsRenderable isRenderable : content) {
            isRenderable.destroy();
        }
        content.clear();
    }

}
