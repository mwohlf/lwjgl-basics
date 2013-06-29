package net.wohlfart.gl.elements.hud;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.hud.widgets.CharAtlas;

/**
 * <p>
 * Layer interface.
 * </p>
 * 
 * 
 * 
 */
public interface Layer extends IsRenderable {

    /**
     * <p>
     * getCharacterAtlas.
     * </p>
     * 
     * @return a {@link net.wohlfart.gl.elements.hud.widgets.CharAtlas} object.
     */
    CharAtlas getCharacterAtlas();

}
