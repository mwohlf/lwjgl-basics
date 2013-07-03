package net.wohlfart.gl.view;

import net.wohlfart.basic.container.RenderSet;
import net.wohlfart.gl.input.PointEvent;
import net.wohlfart.gl.shader.GraphicContextManager;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.google.common.eventbus.Subscribe;

public class ElementPicker {

    private Matrix4f transformMatrix = new Matrix4f();

    private final float width;
    private final float height;
    private final Matrix4f projectionMatrix;
    private final RenderSet<?> renderSet;


    public ElementPicker(RenderSet<?> renderSet) {
        final GraphicContextManager ctxManager = GraphicContextManager.INSTANCE;
        this.projectionMatrix = ctxManager.getPerspectiveProjMatrix();
        this.renderSet = renderSet;
        this.width = ctxManager.getScreenWidth();
        this.height = ctxManager.getScreenHeight();
    }

    @Subscribe
    public void onMouseClick(PointEvent clickEvent) {
        final float x = clickEvent.getX();
        final float y = clickEvent.getY();

        final PickingRay ray = createPickingRay(x, y, renderSet.getModelViewMatrix());
        GraphicContextManager.INSTANCE.post(ray);

        System.out.println("ray: " + ray);
    }


    public PickingRay createPickingRay(float x, float y, Matrix4f modelViewMatrix) {

        Matrix4f.mul(projectionMatrix, modelViewMatrix, transformMatrix);
        transformMatrix = Matrix4f.invert(transformMatrix, transformMatrix);

        final Vector4f cameraSpaceNear = new Vector4f(x / width * 2f - 1f, y / height * 2f - 1f, -1.0f, 1.0f);
        final Vector4f cameraSpaceFar = new Vector4f(x / width * 2f - 1f, y / height * 2f - 1f, 1.0f, 1.0f);

        final Vector4f worldSpaceNear = new Vector4f();
        Matrix4f.transform(transformMatrix, cameraSpaceNear, worldSpaceNear);

        final Vector4f worldSpaceFar = new Vector4f();
        Matrix4f.transform(transformMatrix, cameraSpaceFar, worldSpaceFar);

        final Vector3f start = new Vector3f(worldSpaceNear.x / worldSpaceNear.w, worldSpaceNear.y / worldSpaceNear.w, worldSpaceNear.z / worldSpaceNear.w);
        final Vector3f end = new Vector3f(worldSpaceFar.x / worldSpaceFar.w, worldSpaceFar.y / worldSpaceFar.w, worldSpaceFar.z / worldSpaceFar.w);

        return new PickingRay(start, end);
    }

    public void setup() {
        GraphicContextManager.INSTANCE.register(this);
    }

}
