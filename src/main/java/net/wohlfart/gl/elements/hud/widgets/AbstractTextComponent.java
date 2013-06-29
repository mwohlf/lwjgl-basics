package net.wohlfart.gl.elements.hud.widgets;

import java.util.HashSet;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.hud.Layer;

/**
 * <p>
 * Abstract AbstractTextComponent class.
 * </p>
 * 
 * 
 * 
 */
public abstract class AbstractTextComponent implements IsRenderable {

    private Layer layer;

    /**
     * <p>
     * Setter for the field <code>layer</code>.
     * </p>
     * 
     * @param layer
     *            a {@link net.wohlfart.gl.elements.hud.Layer} object.
     */
    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    /**
     * <p>
     * Getter for the field <code>layer</code>.
     * </p>
     * 
     * @return a {@link net.wohlfart.gl.elements.hud.Layer} object.
     */
    public Layer getLayer() {
        return layer;
    }

    /**
     * <p>
     * createMeshSet.
     * </p>
     * 
     * @param x
     *            a int.
     * @param y
     *            a int.
     * @param string
     *            a {@link java.lang.String} object.
     * @return a {@link java.util.HashSet} object.
     */
    protected HashSet<IsRenderable> createMeshSet(int x, int y, String string) {
        assert string != null : "string to create MeshSet is null";
        final CharAtlas atlas = layer.getCharacterAtlas();
        int d = 0;
        final HashSet<IsRenderable> characters = new HashSet<IsRenderable>();
        final char[] charArray = string.toCharArray();
        for (final char c : charArray) {
            CharInfo info = atlas.getCharInfo(c);
            if (info == null) {
                info = atlas.getCharInfo(CharAtlasBuilder.NULL_CHAR);
            }
            final IsRenderable renderable = createSingleCharMesh(x + d, y, info, atlas);
            d += info.getWidth();
            if (renderable != null) {
                characters.add(renderable);
            }
        }
        return characters;
    }

    private IsRenderable createSingleCharMesh(int x, int y, CharInfo info, CharAtlas characterAtlas) {
        final CharMeshBuilder builder = new CharMeshBuilder();
        builder.setCharAtlas(characterAtlas);
        builder.setCharInfo(info);
        builder.setScreenX(x);
        builder.setScreenY(y);
        return builder.build();
    }

}
