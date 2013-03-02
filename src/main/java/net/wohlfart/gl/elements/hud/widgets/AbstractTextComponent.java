package net.wohlfart.gl.elements.hud.widgets;

import java.util.HashSet;

import net.wohlfart.gl.elements.hud.Layer;
import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.mesh.IMesh;

public abstract class AbstractTextComponent implements Renderable {

    private Layer layer;

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }

    protected HashSet<IMesh> createMeshSet(int x, int y, String string) {
        assert string != null: "string to create MeshSet is null";
        final CharAtlas atlas = layer.getCharacterAtlas();
        int d = 0;
        HashSet<IMesh> characters = new HashSet<IMesh>();
        final char[] charArray = string.toCharArray();
        for (final char c : charArray) {
            CharInfo info = atlas.getCharInfo(c);
            if (info == null) {
                info = atlas.getCharInfo(CharAtlasBuilder.NULL_CHAR);
            }
            final IMesh mesh = createSingleCharMesh(x + d, y, info, atlas);
            d += info.getWidth();
            if (mesh != null) {
                characters.add(mesh);
            }
        }
        return characters;
    }

    private IMesh createSingleCharMesh(int x, int y, CharInfo info, CharAtlas characterAtlas) {
        final CharMeshBuilder builder = new CharMeshBuilder();
        builder.setCharAtlas(characterAtlas);
        builder.setCharInfo(info);
        builder.setScreenX(x);
        builder.setScreenY(y);
        return builder.build();
    }

}
