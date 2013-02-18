package net.wohlfart.gl.elements.hud;

import java.util.HashSet;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.tools.FontRenderer;

public abstract class TextComponent implements Renderable {

    private Layer layer;

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }

    protected HashSet<IMesh> createMeshSet(int x, int y, String string) {
        final CharacterAtlas atlas = layer.getCharacterAtlas();
        int d = 0;
        HashSet<IMesh> characters = new HashSet<IMesh>();
        final char[] charArray = string.toCharArray();
        for (final char c : charArray) {
            CharInfo info = atlas.getCharInfo(c);
            if (info == null) {
                info = atlas.getCharInfo(FontRenderer.NULL_CHAR);
            }
            final IMesh mesh = createMesh(x + d, y, info, atlas);
            d += info.getWidth();
            if (mesh != null) {
                characters.add(mesh);
            }
        }
        return characters;
    }

    private IMesh createMesh(int x, int y, CharInfo info, CharacterAtlas characterAtlas) {
        final CharacterMeshBuilder builder = new CharacterMeshBuilder();
        builder.setCharAtlas(characterAtlas);
        builder.setCharInfo(info);
        builder.setScreenX(x);
        builder.setScreenY(y);
        return builder.build();
    }

}
