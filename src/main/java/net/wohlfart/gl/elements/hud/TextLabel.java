package net.wohlfart.gl.elements.hud;

import java.util.Collection;

import net.wohlfart.gl.shader.mesh.IMesh;

// a label is a quad for each character
public class TextLabel extends TextComponent {

    final int x;
    final int y;
    private Collection<IMesh> characters;
    private String text;


    public TextLabel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setText(String text) {
        this.text = text;
        disposeCharacters();
    }



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

    @Override
    public void dispose() {
        disposeCharacters();
    }

}
