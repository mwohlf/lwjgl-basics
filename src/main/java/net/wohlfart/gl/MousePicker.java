package net.wohlfart.gl;

import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.eventbus.Subscribe;

public class MousePicker {

    GraphicContextManager ctxManager = GraphicContextManager.INSTANCE;
    private final float width;
    private final float height;
    private final float nearPlane;
    private final float farPlane;
    private final float fieldOfView;
    private final Avatar avatar;

    private final Vector3f dir = new Vector3f();
    private final Vector3f right = new Vector3f();
    private final Vector3f up = new Vector3f();

    private final Vector3f ray = new Vector3f();
    private final RenderBucket elemBucket;
    private final Matrix4f m;

    public MousePicker(Avatar avatar, RenderBucket elemBucket) {
        this.avatar = avatar;
        this.elemBucket = elemBucket;
        m = ctxManager.getPerspectiveProjMatrix();
        width = ctxManager.getScreenWidth();
        height = ctxManager.getScreenHeight();
        nearPlane = ctxManager.getNearPlane();
        farPlane = ctxManager.getFarPlane();
        fieldOfView = ctxManager.getFieldOfView();
    }

    @Subscribe
    public void onMouseClick(CommandEvent.LeftClick clickEvent) {
        float x = clickEvent.getX();
        float y = clickEvent.getY();

        float dx = SimpleMath.tan(SimpleMath.deg2rad(fieldOfView)) * (x/(width*0.5f) - 1f);
        float dy = SimpleMath.tan(SimpleMath.deg2rad(fieldOfView)) * (y/(height*0.5f) - 1f);

        Vector3f start = new Vector3f(dx * nearPlane/m.m00, dy * nearPlane/m.m11, nearPlane/m.m22);
        Vector3f end = new Vector3f(dx * farPlane/m.m00, dy * farPlane/m.m11, farPlane/m.m22);

        Vector3f pos = avatar.getPosition();

        start.x += pos.x;
        start.y += pos.y;
        start.z += pos.z;
        end.x += pos.x;
        end.y += pos.y;
        end.z += pos.z;

        elemBucket.add(Arrow.createLink(start, end).lineWidth(1f));
    }

}
