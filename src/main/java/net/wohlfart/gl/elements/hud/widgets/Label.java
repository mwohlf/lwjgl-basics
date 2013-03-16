package net.wohlfart.gl.elements.hud.widgets;

import java.util.Collection;

import net.wohlfart.gl.shader.mesh.IMesh;

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
    private Collection<IMesh> characters;

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
        for (final IMesh mesh : characters) {
            mesh.draw();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        for (final IMesh mesh : characters) {
            mesh.dispose();
        }
        characters = null;
    }

}
