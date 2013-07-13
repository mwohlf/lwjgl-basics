package net.wohlfart.gl.elements.hud.widgets;

import java.text.MessageFormat;
import java.util.Collection;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.hud.txt.AbstractCharComponent;


public class FormattedLabel extends AbstractCharComponent {

    private final int x;
    private final int y;
    private final MessageFormat format;
    private Collection<IsRenderable> characters;
    private Object[] arguments;


    public FormattedLabel(int x, int y, String pattern) {
        this.x = x;
        this.y = y;
        this.format = new MessageFormat(pattern);
    }

    public void setValue(Object[] arguments) {
        this.arguments = arguments;
        disposeCharacters();
    }

    public void setValue(float argument) {
        this.arguments = new Object[] { argument };
        disposeCharacters();
    }

    @Override
    public void render() {
        if (characters == null) {
            final String string = format.format(arguments, new StringBuffer(), null).toString();
            characters = createCharElements(x, y, string);
        }
        for (final IsRenderable renderable : characters) {
            renderable.render();
        }
    }

    @Override
    public void update(float timeInSec) {
    }

    private void disposeCharacters() {
        if (characters == null) {
            return;
        }
        for (final IsRenderable renderable : characters) {
            renderable.destroy();
        }
        characters = null;
    }

    @Override
    public void destroy() {
        disposeCharacters();
    }

}
