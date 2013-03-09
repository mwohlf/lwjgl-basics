package net.wohlfart.gl.shader;

import static org.junit.Assert.assertEquals;
import net.wohlfart.basic.Settings;
import net.wohlfart.gl.view.HasCamProjectionModelViewMatrices;
import net.wohlfart.gl.view.MousePicker;
import net.wohlfart.gl.view.PickingRay;

import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Matrix4f;

/**
 * <p>GraphicContextManagerSimpleTest class.</p>
 */
public class GraphicContextManagerSimpleTest {

    GraphicContextManager contxt;
    Settings settings;
    MousePicker mousePicker;

    HasCamProjectionModelViewMatrices matrices = new HasCamProjectionModelViewMatrices() {

        @Override
        public Matrix4f getProjectionMatrix() {
            return GraphicContextManager.INSTANCE.getPerspectiveProjMatrix();
        }

        @Override
        public Matrix4f getModelViewMatrix() {
            Matrix4f m = new Matrix4f();
            return m;
        }

    };

    /**
     * <p>setup.</p>
     */
    @Before
    public void setup() {
        settings = createSettings();
        contxt = GraphicContextManager.INSTANCE;
        contxt.setSettings(settings);
        mousePicker = new MousePicker(null, settings.getWidth(), settings.getHeight());
    }

    Settings createSettings() {
        settings = new Settings();
        settings.setWidth(1000);
        settings.setHeight(700);
        settings.setFullscreen(false);
        settings.setFieldOfView(45);
        settings.setNearPlane(0.1f);
        settings.setFarPlane(100);
        return settings;
    }


    /**
     * <p>testTopRight.</p>
     */
    @Test
    public void testTopRight() {
        // createPickingRay for: 1000.0,700.0------------
        //final vectors: Vector3f[0.05917337, 0.041421358, -0.0998002],Vector3f[59.17337, 41.42136, -99.8002]------------
        PickingRay ray;

        // mouse origin is bottom left:
        ray = mousePicker.createPickingRay(1000, 700, matrices);

        assertEquals( 0.059173370, ray.getStart().x, 0.01);
        assertEquals( 0.041421358, ray.getStart().y, 0.01);
        assertEquals(-0.099800200, ray.getStart().z, 0.01);

        assertEquals( 59.17337, ray.getEnd().x, 0.01);
        assertEquals( 41.42136, ray.getEnd().y, 0.01);
        assertEquals(-99.99999, ray.getEnd().z, 0.01);
    }

    /**
     * <p>testTopLeft.</p>
     */
    @Test
    public void testTopLeft() {
        // createPickingRay for: 1000.0,700.0------------
        //final vectors: Vector3f[0.05917337, 0.041421358, -0.0998002],Vector3f[59.17337, 41.42136, -99.8002]------------
        PickingRay ray;

        // mouse origin is bottom left:
        ray = mousePicker.createPickingRay(0, 700, matrices);

        assertEquals(-0.059173370, ray.getStart().x, 0.01);
        assertEquals( 0.041421358, ray.getStart().y, 0.01);
        assertEquals(-0.099800200, ray.getStart().z, 0.01);

        assertEquals(-59.17337, ray.getEnd().x, 0.01);
        assertEquals( 41.42136, ray.getEnd().y, 0.01);
        assertEquals(-99.99999, ray.getEnd().z, 0.01);
    }

    /**
     * <p>testBottomLeft.</p>
     */
    @Test
    public void testBottomLeft() {
        // createPickingRay for: 0.0,0.0------------
        // final vectors: Vector3f[-0.05917337, -0.041421358, -0.0998002],Vector3f[-59.17337, -41.42136, -99.8002]------------
        PickingRay ray;

        // mouse origin is bottom left:
        ray = mousePicker.createPickingRay(0, 0, matrices);

        assertEquals(-0.059173370, ray.getStart().x, 0.01);
        assertEquals(-0.041421358, ray.getStart().y, 0.01);
        assertEquals(-0.099800200, ray.getStart().z, 0.01);

        assertEquals(-59.17337, ray.getEnd().x, 0.01);
        assertEquals(-41.42136, ray.getEnd().y, 0.01);
        assertEquals(-99.99999, ray.getEnd().z, 0.01);
    }

    /**
     * <p>testBottomRigth.</p>
     */
    @Test
    public void testBottomRigth() {
        // createPickingRay for: 0.0,0.0------------
        // final vectors: Vector3f[-0.05917337, -0.041421358, -0.0998002],Vector3f[-59.17337, -41.42136, -99.8002]------------
        PickingRay ray;

        // mouse origin is bottom left:
        ray = mousePicker.createPickingRay(1000, 0, matrices);

        assertEquals( 0.059173370, ray.getStart().x, 0.01);
        assertEquals(-0.041421358, ray.getStart().y, 0.01);
        assertEquals(-0.099800200, ray.getStart().z, 0.01);

        assertEquals( 59.17337, ray.getEnd().x, 0.01);
        assertEquals(-41.42136, ray.getEnd().y, 0.01);
        assertEquals(-99.99999, ray.getEnd().z, 0.01);
    }

    /**
     * <p>testCenter.</p>
     */
    @Test
    public void testCenter() {
        // createPickingRay for: 500.0,350.0
        //final vectors: Vector3f[0.0, 0.0, -0.0998002],Vector3f[0.0, 0.0, -99.8002]
        PickingRay ray;

        // mouse origin is bottom left
        // picking the center of the screen should give us a solid line along the z axis:
        ray = mousePicker.createPickingRay(settings.getWidth()/2f, settings.getHeight()/2f, matrices);

        assertEquals( 0.0, ray.getStart().x, 0.01);
        assertEquals( 0.0, ray.getStart().y, 0.01);
        assertEquals(-0.1, ray.getStart().z, 0.01);

        assertEquals( 0.0, ray.getEnd().x, 0.01);
        assertEquals( 0.0, ray.getEnd().y, 0.01);
        assertEquals(-100, ray.getEnd().z, 0.01);
    }



}
