package net.wohlfart.gl;

import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.model.Avatar;

import com.google.common.eventbus.Subscribe;

public class MousePicker {

    GraphicContextManager ctxManager = GraphicContextManager.INSTANCE;
    private final Avatar avatar;

    private final RenderBucket elemBucket;

    public MousePicker(Avatar avatar, RenderBucket elemBucket) {
        this.avatar = avatar;
        this.elemBucket = elemBucket;
    }

    @Subscribe
    public void onMouseClick(CommandEvent.LeftClick clickEvent) {
        float x = clickEvent.getX();
        float y = clickEvent.getY();
        PickingRay ray = ctxManager.createPickingRay(x, y, avatar);
        elemBucket.add(Arrow.createLink(ray.getStart(), ray.getEnd()).lineWidth(1f));
    }

}
