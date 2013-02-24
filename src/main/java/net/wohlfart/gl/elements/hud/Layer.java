package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.widgets.CharAtlas;
import net.wohlfart.gl.renderer.Renderable;

public interface Layer extends Renderable {

    CharAtlas getCharacterAtlas();

}
