package net.wohlfart.basic.hud;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.IsUpdatable;


public interface Layer extends IsRenderable, IsUpdatable {

    interface Widget extends IsUpdatable, IsRenderable {

        void setLayer(Layer layer);

        @Override
        void render();

        @Override
        void update(float tpf);

        @Override
        void destroy();

    }


    IsRenderable createCharElements(int x, int y, String string);

    boolean add(Widget widget);

    @Override
    void render();

    @Override
    void update(float tpf);

    @Override
    void destroy();

}
