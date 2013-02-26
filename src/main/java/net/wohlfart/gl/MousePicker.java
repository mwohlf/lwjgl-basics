package net.wohlfart.gl;

import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.eventbus.Subscribe;

public class MousePicker {

    GraphicContextManager ctxManager = GraphicContextManager.INSTANCE;
    private final float width;
    private final float height;
    private final float nearPlane;
    private final Avatar avatar;

    private Vector3f dir = new Vector3f();
    private Vector3f right = new Vector3f();
    private Vector3f up = new Vector3f();

    private final Vector3f ray = new Vector3f();
    private final RenderBucket elemBucket;

    public MousePicker(Avatar avatar, RenderBucket elemBucket) {
        this.avatar = avatar;
        this.elemBucket = elemBucket;
        width = ctxManager.getScreenWidth();
        height = ctxManager.getScreenHeight();
        nearPlane = ctxManager.getNearPlane();
    }

    @Subscribe
    public synchronized void onMouseClick(CommandEvent.LeftClick clickEvent) {
        float x = clickEvent.getX();
        float y = clickEvent.getY();

        // translate mouse coordinates so that the origin lies in the center of the view port
        x -= width / 2f;
        y -= height / 2f;

        // scale mouse coordinates so that half the view port width and height becomes 1
        x /= width;
        y /= height;

        dir = avatar.getDir(dir);
        right = avatar.getRght(right);
        up = avatar.getUp(up);

        System.out.println(""
                + " x: " +  x + " y: " + y
                + " dir: " + dir + " right: " + right + " up: " + up);

        SimpleMath.mul(dir, nearPlane);
        SimpleMath.mul(right, x);
        SimpleMath.mul(up, y);

        SimpleMath.sum(ray, dir, right, up);
        SimpleMath.mul(ray, 100f);

        elemBucket.add(new Arrow(ray));

    }

}
