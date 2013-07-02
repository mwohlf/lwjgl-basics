package net.wohlfart.gl.view;

import java.util.List;

import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.basic.container.ModelRenderSet;
import net.wohlfart.basic.container.RenderSet;
import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.input.PointEvent;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.spatial.Model;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.google.common.eventbus.Subscribe;

public class ElementPicker {

    private Matrix4f transformMatrix = new Matrix4f();

    private final RenderSet hasMatrices;

    private final float width;
    private final float height;

    private DefaultRenderSet renderSet;

    private ModelRenderSet modelBucket;


    public ElementPicker(DefaultRenderSet hasMatrices, float width, float height) {
        this.hasMatrices = hasMatrices;
        this.width = width;
        this.height = height;
    }

    // for rendering a debug arrow
    public void setRenderBucket(DefaultRenderSet renderSet) {
        this.renderSet = renderSet;
    }

    // for picking elements
    public void setModelBucket(ModelRenderSet modelBucket) {
        this.modelBucket = modelBucket;
    }


    @Subscribe
    public void onMouseClick(PointEvent clickEvent) {
        final float x = clickEvent.getX();
        final float y = clickEvent.getY();

        final PickingRay ray = createPickingRay(x, y, hasMatrices);
        renderSet.add(Arrow.createLink(ray.getStart(), ray.getEnd()));

        List<Model> picklist = modelBucket.pick(ray);

        System.out.println("picked: " + picklist);
    }


    public PickingRay createPickingRay(float x, float y, RenderSet elemBucket) {

        Matrix4f.mul(elemBucket.getProjectionMatrix(), elemBucket.getModelViewMatrix(), transformMatrix);
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
