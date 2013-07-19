package net.wohlfart.gl.view;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;
import net.wohlfart.basic.Settings;
import net.wohlfart.gl.input.PointEvent;

import com.google.common.eventbus.Subscribe;


// converts point events into pick events
public class ElementPicker {

    private float width;
    private float height;


    public ElementPicker() {
    }

    public void setup() {
        final Settings settings = CONTEXT_HOLDER.getSettings();
        this.width = settings.getWidth();
        this.height = settings.getHeight();
        CONTEXT_HOLDER.register(this);
    }

    @Subscribe
    public void onMouseClick(PointEvent clickEvent) {
        final float x = clickEvent.getX();
        final float y = clickEvent.getY();
        final PickEvent ray = new PickEvent(width, height, x, y);
        CONTEXT_HOLDER.post(ray);
    }

    public void destroy() {
        CONTEXT_HOLDER.unregister(this);
    }

}
