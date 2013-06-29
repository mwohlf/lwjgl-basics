package net.wohlfart.basic.container;

import net.wohlfart.basic.elements.IsRenderable;



@SuppressWarnings("serial")
public class CelestialBodyBucket extends DefaultRenderSet<IsRenderable> {

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
