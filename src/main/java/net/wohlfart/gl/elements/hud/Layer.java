package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.widgets.CharAtlas;
import net.wohlfart.gl.renderer.Renderable;

/**
 * <p>Layer interface.</p>
 *
 *
 *
 */
public interface Layer extends Renderable {

    /**
     * <p>getCharacterAtlas.</p>
     *
     * @return a {@link net.wohlfart.gl.elements.hud.widgets.CharAtlas} object.
     */
    CharAtlas getCharacterAtlas();

}
