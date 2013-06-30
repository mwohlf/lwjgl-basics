package net.wohlfart.gl.elements.hud.widgets;

import java.util.HashSet;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.hud.Layer;


public abstract class AbstractTextComponent implements Layer.LayerElement {

    private Layer layer;


    public void setLayer(Layer layer) {
        this.layer = layer;
    }


    public Layer getLayer() {
        return layer;
    }


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
