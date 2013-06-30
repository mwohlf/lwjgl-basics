package net.wohlfart.gl.elements.hud;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.IsUpdateable;
import net.wohlfart.gl.elements.hud.widgets.CharAtlas;

/**
 * <p>
 * Layer interface.
 * </p>
 *
 *
 *
 */
public interface Layer extends IsRenderable, IsUpdateable {

    interface LayerElement extends IsUpdateable, IsRenderable {

        void setLayer(Layer layer);

        @Override
        void render();

        @Override
        void update(float tpf);

        @Override
        void destroy();

    }


    CharAtlas getCharacterAtlas();

    boolean add(LayerElement label);

    @Override
    void render();

    @Override
    void update(float tpf);

    @Override
    void destroy();



}
