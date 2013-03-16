package net.wohlfart.gl.elements.hud.widgets;

import java.util.Collection;

import net.wohlfart.gl.renderer.IsRenderable;


// a label is a quad for each character
/**
 * <p>Label class.</p>
 *
 *
 *
 */
public class Label extends AbstractTextComponent {

    final int x;
    final int y;
    private final String string;
    private Collection<IsRenderable> characters;

    /**
     * <p>Constructor for Label.</p>
     *
     * @param x a int.
     * @param y a int.
     * @param string a {@link java.lang.String} object.
     */
    public Label(int x, int y, String string) {
        this.x = x;
        this.y = y;
        this.string = string;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        if (characters == null) {
            characters = createMeshSet(x, y, string);
        }
        for (final IsRenderable renderable : characters) {
            renderable.render();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        for (final IsRenderable renderable : characters) {
            renderable.destroy();
        }
        characters = null;
    }

}
