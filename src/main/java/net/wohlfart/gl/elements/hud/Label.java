package net.wohlfart.gl.elements.hud;

import java.util.Collection;

import net.wohlfart.gl.shader.mesh.IMesh;

// a label is a quad for each character
public class Label extends TextComponent {

    final int x;
    final int y;
    private final String string;
    private Collection<IMesh> characters;

    public Label(int x, int y, String string) {
        this.x = x;
        this.y = y;
        this.string = string;
    }

    @Override
    public void render() {
        if (characters == null) {
            characters = createMeshSet(x, y, string);
        }
        for (final IMesh mesh : characters) {
            mesh.draw();
        }
    }

    @Override
    public void dispose() {
        for (final IMesh mesh : characters) {
            mesh.dispose();
        }
        characters = null;
    }

}
