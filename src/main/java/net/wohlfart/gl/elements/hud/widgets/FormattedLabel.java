package net.wohlfart.gl.elements.hud.widgets;

import java.text.MessageFormat;
import java.util.Collection;

import net.wohlfart.basic.elements.IsRenderable;

// a label is a quad for each character
/**
 * <p>
 * FormattedLabel class.
 * </p>
 * 
 * 
 * 
 */
public class FormattedLabel extends AbstractTextComponent {

    final int x;
    final int y;
    private Collection<IsRenderable> characters;
    private final MessageFormat format;
    private Object[] arguments;

    /**
     * <p>
     * Constructor for FormattedLabel.
     * </p>
     * 
     * @param x
     *            a int.
     * @param y
     *            a int.
     * @param pattern
     *            a {@link java.lang.String} object.
     */
    public FormattedLabel(int x, int y, String pattern) {
        this.x = x;
        this.y = y;
        this.format = new MessageFormat(pattern);
    }

    /**
     * <p>
     * setValue.
     * </p>
     * 
     * @param arguments
     *            an array of {@link java.lang.Object} objects.
     */
    public void setValue(Object[] arguments) {
        this.arguments = arguments;
        disposeCharacters();
    }

    /**
     * <p>
     * setValue.
     * </p>
     * 
     * @param argument
     *            a float.
     */
    public void setValue(float argument) {
        this.arguments = new Object[] { argument };
        disposeCharacters();
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        if (characters == null) {
            final String string = format.format(arguments, new StringBuffer(), null).toString();
            characters = createMeshSet(x, y, string);
        }
        for (final IsRenderable renderable : characters) {
            renderable.render();
        }
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

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        disposeCharacters();
    }

}
