package net.wohlfart.gl.elements.hud.widgets;

import net.wohlfart.gl.elements.hud.Layer;
import net.wohlfart.gl.input.PointEvent;
import net.wohlfart.gl.shader.GraphicContextManager;

import com.google.common.eventbus.Subscribe;


public class MouseClickLabel extends FormattedLabel {


    @Override
    public void setLayer(Layer layer) {
        super.setLayer(layer);
        GraphicContextManager.INSTANCE.register(this);
    }

    public MouseClickLabel(int x, int y) {
        super(x, y, "Mouse Click at: {0, number, ###},{1, number, ###}");
    }

    @Subscribe
    public synchronized void onMouseMove(PointEvent positionEvent) {
        setValue(new Object[] {positionEvent.getX(), positionEvent.getY()});
    }

    @Override
    public void destroy() {
        super.destroy();
        GraphicContextManager.INSTANCE.unregister(this);
    }
}
