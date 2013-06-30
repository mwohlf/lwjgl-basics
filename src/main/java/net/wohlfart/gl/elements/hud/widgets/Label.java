package net.wohlfart.gl.elements.hud.widgets;

import java.util.Collection;

import net.wohlfart.basic.elements.IsRenderable;


public class Label extends AbstractTextComponent {

    final int x;
    final int y;
    private final String string;
    private Collection<IsRenderable> characters;

    public Label(int x, int y, String string) {
        this.x = x;
        this.y = y;
        this.string = string;
    }

    @Override
    public void render() {
        if (characters == null) {
            characters = createMeshSet(x, y, string);
        }
        for (final IsRenderable renderable : characters) {
            renderable.render();
        }
    }

    @Override
    public void update(float timeInSec) {

    }

    @Override
    public void destroy() {
        for (final IsRenderable renderable : characters) {
            renderable.destroy();
        }
        characters = null;
    }

}
