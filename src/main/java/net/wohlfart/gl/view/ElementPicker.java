package net.wohlfart.gl.view;

import net.wohlfart.gl.input.PointEvent;
import net.wohlfart.gl.shader.GraphicContextManager;

import com.google.common.eventbus.Subscribe;

public class ElementPicker {

    private float width;
    private float height;


    public ElementPicker() {
    }

    @Subscribe
    public void onMouseClick(PointEvent clickEvent) {
        final float x = clickEvent.getX();
        final float y = clickEvent.getY();

        final PickEvent ray = new PickEvent(width, height, x, y);
        GraphicContextManager.INSTANCE.post(ray);
    }



    public void setup() {
        final GraphicContextManager ctxManager = GraphicContextManager.INSTANCE;
        this.width = ctxManager.getScreenWidth();
        this.height = ctxManager.getScreenHeight();
        GraphicContextManager.INSTANCE.register(this);
    }

    public void destroy() {
        GraphicContextManager.INSTANCE.unregister(this);

    }

}
