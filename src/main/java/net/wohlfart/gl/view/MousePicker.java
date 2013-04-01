package net.wohlfart.gl.view;

import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.renderer.RenderableBucket;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.google.common.eventbus.Subscribe;

/**
 * <p>MousePicker class.</p>
 *
 */
public class MousePicker {


    private Matrix4f transformMatrix = new Matrix4f();

    private final RenderableBucket elemBucket;

    private final float width;
    private final float height;


    /**
     * <p>Constructor for MousePicker.</p>
     *
     * @param elemBucket a {@link net.wohlfart.gl.renderer.RenderableBucket} object.
     * @param width a float.
     * @param height a float.
     */
    public MousePicker(RenderableBucket elemBucket, float width, float height) {
        this.elemBucket = elemBucket;
        this.width = width;
        this.height = height;
    }

    /**
     * <p>onMouseClick.</p>
     *
     * @param clickEvent a {@link net.wohlfart.gl.input.CommandEvent.LeftClick} object.
     */
    @Subscribe
    public void onMouseClick(CommandEvent.LeftClick clickEvent) {
        float x = clickEvent.getX();
        float y = clickEvent.getY();

        PickingRay ray = createPickingRay(x, y, elemBucket);
        elemBucket.add(Arrow.createLink(ray.getStart(), ray.getEnd()));
    }



    // see: http://gamedev.stackexchange.com/questions/8974/converting-a-mouse-click-to-a-ray
    /**
     * <p>createPickingRay.</p>
     *
     * @param x a float.
     * @param y a float.
     * @param hasMatrices a {@link net.wohlfart.gl.view.HasCamProjectionModelViewMatrices} object.
     * @return a {@link net.wohlfart.gl.view.PickingRay} object.
     */
    public PickingRay createPickingRay(float x, float y, HasCamProjectionModelViewMatrices hasMatrices){

        Matrix4f.mul(hasMatrices.getProjectionMatrix(), hasMatrices.getModelViewMatrix(), transformMatrix);
        transformMatrix = Matrix4f.invert(transformMatrix, transformMatrix);


        Vector4f cameraSpaceNear = new Vector4f(x / width * 2f - 1f, y / height * 2f - 1f, -1.0f, 1.0f);
        Vector4f cameraSpaceFar = new Vector4f(x / width * 2f - 1f, y / height * 2f - 1f,  1.0f, 1.0f);

        Vector4f worldSpaceNear = new Vector4f();
        Matrix4f.transform(transformMatrix, cameraSpaceNear, worldSpaceNear);

        Vector4f worldSpaceFar = new Vector4f();
        Matrix4f.transform(transformMatrix, cameraSpaceFar, worldSpaceFar);

        Vector3f start = new Vector3f(worldSpaceNear.x / worldSpaceNear.w,
                                      worldSpaceNear.y / worldSpaceNear.w,
                                      worldSpaceNear.z / worldSpaceNear.w);
        Vector3f end = new Vector3f(worldSpaceFar.x / worldSpaceFar.w,
                                    worldSpaceFar.y / worldSpaceFar.w,
                                    worldSpaceFar.z / worldSpaceFar.w);

        return new PickingRay(start, end);
    }

}
