package net.wohlfart.gl.elements.hud.widgets;

import java.util.Collection;

import net.wohlfart.gl.shader.mesh.IMesh;

// a label is a quad for each character
/**
 * <p>TextLabel class.</p>
 *
 *
 *
 */
public class TextLabel extends AbstractTextComponent {

    final int x;
    final int y;
    private Collection<IMesh> characters;
    private String text;


    /**
     * <p>Constructor for TextLabel.</p>
     *
     * @param x a int.
     * @param y a int.
     */
    public TextLabel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * <p>Setter for the field <code>text</code>.</p>
     *
     * @param text a {@link java.lang.String} object.
     */
    public void setText(String text) {
        this.text = text;
        disposeCharacters();
    }



    /** {@inheritDoc} */
    @Override
    public void render() {
        if (text == null) {
            return;
        }
        if (characters == null) {
            characters = createMeshSet(x, y, text);
        }
        for (final IMesh mesh : characters) {
            mesh.draw();
        }
    }

    private void disposeCharacters() {
        if (characters == null) {
            return;
        }
        for (final IMesh mesh : characters) {
            mesh.dispose();
        }
        characters = null;
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        disposeCharacters();
    }

}
