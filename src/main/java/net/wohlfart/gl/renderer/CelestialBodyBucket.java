package net.wohlfart.gl.renderer;

import java.util.HashSet;
import java.util.Set;



public class CelestialBodyBucket extends AbstractRenderBucket implements RenderBucket {

    protected final Set<IsRenderable> content = new HashSet<>(10100);

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
