package net.wohlfart.gl.shader;

import static org.junit.Assert.assertEquals;
import net.wohlfart.basic.Settings;
import net.wohlfart.gl.PickingRay;

import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Matrix4f;

public class GraphicContextManagerMovedTest {

    GraphicContextManager contxt;
    private Settings settings;

    @Before
    public void setup() {
        settings = createSettings();
        contxt = GraphicContextManager.INSTANCE;
        contxt.setSettings(settings);
        // use projection matrix
        Matrix4f projectionMatrix = contxt.getPerspectiveProjMatrix();
        contxt.setProjectionMatrix(projectionMatrix);
        // no cam move or rotation for now:
        Matrix4f modelViewMatrix = new Matrix4f();
        modelViewMatrix.m30 = -10;                    // move 10 in x direction
        contxt.setModelViewMatrix(modelViewMatrix);
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

    @Test
    public void testCenter() {
        // createPickingRay for: 500.0,350.0
        //final vectors: Vector3f[0.0, 0.0, -0.0998002],Vector3f[0.0, 0.0, -99.8002]

        // ------------createPickingRay for: 1000.0,700.0------------
        // ------------final vectors: Vector3f[10.059174, 0.041421358, -0.0998002],Vector3f[69.17337, 41.42136, -99.8002]------------

        PickingRay ray;

        // mouse origin is bottom left
        // picking the center of the screen should give us a solid line along the z axis:
        ray = contxt.createPickingRay(settings.getWidth()/2f, settings.getHeight()/2f);

        assertEquals(10.0, ray.getStart().x, 0.01);
        assertEquals( 0.0, ray.getStart().y, 0.01);
        assertEquals(-0.1, ray.getStart().z, 0.01);

        assertEquals(10.0, ray.getEnd().x, 0.01);
        assertEquals( 0.0, ray.getEnd().y, 0.01);
        assertEquals(-100, ray.getEnd().z, 0.01);
    }

}
