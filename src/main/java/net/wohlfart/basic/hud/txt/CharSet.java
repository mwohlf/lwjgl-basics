package net.wohlfart.basic.hud.txt;

import java.util.HashSet;

import net.wohlfart.basic.elements.IsRenderable;

@SuppressWarnings("serial")
public class CharSet extends HashSet<IsRenderable> implements IsRenderable {

    @Override
    public void render() {
        for (IsRenderable isRenderable : this) {
            isRenderable.render();
        }
    }

    @Override
    public void destroy() {
        for (IsRenderable isRenderable : this) {
            isRenderable.destroy();
        }
    }

}
