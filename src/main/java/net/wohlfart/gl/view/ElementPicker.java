package net.wohlfart.gl.view;

import net.wohlfart.gl.input.PointEvent;
import net.wohlfart.gl.shader.GraphicContextHolder;

import com.google.common.eventbus.Subscribe;


// converts point events into pick events
public class ElementPicker {

    private float width;
    private float height;


    public ElementPicker() {
    }

    public void setup() {
        final GraphicContextHolder ctxManager = GraphicContextHolder.CONTEXT_HOLDER;
        this.width = ctxManager.getScreenWidth();
        this.height = ctxManager.getScreenHeight();
        GraphicContextHolder.CONTEXT_HOLDER.register(this);
    }

    @Subscribe
    public void onMouseClick(PointEvent clickEvent) {
        final float x = clickEvent.getX();
        final float y = clickEvent.getY();

        final PickEvent ray = new PickEvent(width, height, x, y);
        GraphicContextHolder.CONTEXT_HOLDER.post(ray);
    }

    public void destroy() {
        GraphicContextHolder.CONTEXT_HOLDER.unregister(this);

    }

}
