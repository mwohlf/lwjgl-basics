package net.wohlfart.gl.elements.hud;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.IsUpdateable;
import net.wohlfart.gl.elements.hud.txt.CharAtlas;


public interface Layer extends IsRenderable, IsUpdateable {

    interface Widget extends IsUpdateable, IsRenderable {

        void setLayer(Layer layer);

        @Override
        void render();

        @Override
        void update(float tpf);

        @Override
        void destroy();

    }


    CharAtlas getCharacterAtlas();

    boolean add(Widget widget);

    @Override
    void render();

    @Override
    void update(float tpf);

    @Override
    void destroy();



}
