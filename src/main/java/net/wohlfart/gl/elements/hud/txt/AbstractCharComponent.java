package net.wohlfart.gl.elements.hud.txt;

import java.util.HashSet;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.hud.Layer;


public abstract class AbstractCharComponent implements Layer.Widget {

    private Layer layer;


    @Override
    public void setLayer(Layer layer) {
        this.layer = layer;
    }


    public Layer getLayer() {
        return layer;
    }


    protected HashSet<IsRenderable> createCharElements(int x, int y, String string) {
        assert string != null : "string to create MeshSet is null";
        final HashSet<IsRenderable> characters = new HashSet<IsRenderable>();

        final CharAtlas atlas = layer.getCharacterAtlas();
        final CharMeshBuilder builder = new CharMeshBuilder();
        builder.setCharAtlas(atlas);

        int d = 0;
        final char[] charArray = string.toCharArray();
        for (final char c : charArray) {
            CharInfo info = atlas.getCharInfo(c);
            if (info == null) {
                info = atlas.getCharInfo(CharAtlasBuilder.NULL_CHAR);
            }
            builder.setCharInfo(info);
            builder.setScreenX(x + d);
            builder.setScreenY(y);
            final IsRenderable renderable = builder.build();
            d += info.getWidth();
            if (renderable != null) {
                characters.add(renderable);
            }
        }
        return characters;
    }

}
