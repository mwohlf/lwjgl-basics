package net.wohlfart.gl.elements.hud;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.tools.FontRenderer;


// a label is a quad for each character
public class Label implements Renderable {

	private final int x;
	private final int y;
	private final String string;
	private LayerImpl layer;
	private Collection<IMesh> characters;

	public Label(int x, int y, String string) {
		this.x = x;
		this.y = y;
		this.string = string;
	}


	@Override
	public void render() {
		if (characters == null) {
			init();
		}
		for (IMesh mesh : characters) {
			mesh.draw();
		}
	}

	@Override
	public void dispose() {
		for (IMesh mesh : characters) {
			mesh.dispose();
		}
		characters = null;
	}


	public void setLayer(LayerImpl layer) {
		this.layer = layer;
	}


	private void init() {
		characters = new HashSet<IMesh>();
		char[] charArray = string.toCharArray();
		for (char c : charArray) {
			IMesh mesh = createMesh(c, layer.getCharacterAtlas());
			if (mesh != null) {
				characters.add(mesh);
			}
		}
	}


	private IMesh createMesh(char c, CharacterAtlas characterAtlas) {
		CharInfo info = characterAtlas.getCharInfo(c);
		if (info == null) {
			info = characterAtlas.getCharInfo(FontRenderer.NULL_CHAR);
		}
		CharacterMeshBuilder builder = new CharacterMeshBuilder();
		builder.setCharAtlas(characterAtlas);
		builder.setCharInfo(info);
		builder.setScreenX(x);
		builder.setScreenY(y);
	    return builder.build();
	}

}
