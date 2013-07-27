package net.wohlfart.basic.hud.widgets;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.hud.txt.AbstractCharComponent;


public class SimpleLabel extends AbstractCharComponent {

    final int x;
    final int y;
    private final String string;
    private IsRenderable characters;

    public SimpleLabel(int x, int y, String string) {
        this.x = x;
        this.y = y;
        this.string = string;
    }

    @Override
    public void render() {
        if (characters == null) {
            characters = getLayer().createCharElements(x, y, string);
        }
        characters.render();
    }

    @Override
    public void update(float timeInSec) {
    }

    @Override
    public void destroy() {
        characters.destroy();
        characters = null;
    }

}
