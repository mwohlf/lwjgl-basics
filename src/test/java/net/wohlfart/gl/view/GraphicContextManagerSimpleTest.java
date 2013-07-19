package net.wohlfart.gl.view;

import static org.junit.Assert.assertEquals;
import net.wohlfart.basic.Settings;
import net.wohlfart.gl.shader.GraphicContextHolder;
import net.wohlfart.gl.shader.PerspectiveProjectionFab;

import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Matrix4f;


public class GraphicContextManagerSimpleTest {

    GraphicContextHolder contxt;
    Settings settings;
    PickEvent pickEvent;


    @Before
    public void setup() {
        settings = createSettings();
        contxt = GraphicContextHolder.CONTEXT_HOLDER;
        contxt.setSettings(settings);
        pickEvent = new PickEvent(settings.getWidth(), settings.getHeight(), settings.getWidth() / 2f, settings.getHeight() / 2f);
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
    public void testTopRight() {
        // mouse origin is bottom left:
        pickEvent = new PickEvent(settings.getWidth(), settings.getHeight(), settings.getWidth(), settings.getHeight());
        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f modelViewMatrix = new PerspectiveProjectionFab().create(contxt.getSettings());
        PickingRay ray = pickEvent.createPickingRay(projectionMatrix, modelViewMatrix);

        assertEquals(0.059173370, ray.getStart().x, 0.01);
        assertEquals(0.041421358, ray.getStart().y, 0.01);
        assertEquals(-0.099800200, ray.getStart().z, 0.01);

        assertEquals(59.17337, ray.getEnd().x, 0.01);
        assertEquals(41.42136, ray.getEnd().y, 0.01);
        assertEquals(-99.99999, ray.getEnd().z, 0.01);
    }

    @Test
    public void testTopLeft() {
        // mouse origin is bottom left:
        pickEvent = new PickEvent(settings.getWidth(), settings.getHeight(), 0, 700);
        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f modelViewMatrix = new PerspectiveProjectionFab().create(contxt.getSettings());
        PickingRay ray = pickEvent.createPickingRay(projectionMatrix, modelViewMatrix);

        assertEquals(-0.059173370, ray.getStart().x, 0.01);
        assertEquals(0.041421358, ray.getStart().y, 0.01);
        assertEquals(-0.099800200, ray.getStart().z, 0.01);

        assertEquals(-59.17337, ray.getEnd().x, 0.01);
        assertEquals(41.42136, ray.getEnd().y, 0.01);
        assertEquals(-99.99999, ray.getEnd().z, 0.01);
    }

    @Test
    public void testBottomLeft() {
        // createPickingRay for: 0.0,0.0------------
        // final vectors: Vector3f[-0.05917337, -0.041421358, -0.0998002],Vector3f[-59.17337, -41.42136, -99.8002]------------

        // mouse origin is bottom left:
        pickEvent = new PickEvent(settings.getWidth(), settings.getHeight(), 0, 0);
        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f modelViewMatrix = new PerspectiveProjectionFab().create(contxt.getSettings());
        PickingRay ray = pickEvent.createPickingRay(projectionMatrix, modelViewMatrix);

        assertEquals(-0.059173370, ray.getStart().x, 0.01);
        assertEquals(-0.041421358, ray.getStart().y, 0.01);
        assertEquals(-0.099800200, ray.getStart().z, 0.01);

        assertEquals(-59.17337, ray.getEnd().x, 0.01);
        assertEquals(-41.42136, ray.getEnd().y, 0.01);
        assertEquals(-99.99999, ray.getEnd().z, 0.01);
    }

    @Test
    public void testBottomRigth() {
        // mouse origin is bottom left:
        pickEvent = new PickEvent(settings.getWidth(), settings.getHeight(), 1000, 0);
        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f modelViewMatrix = new PerspectiveProjectionFab().create(contxt.getSettings());
        PickingRay ray = pickEvent.createPickingRay(projectionMatrix, modelViewMatrix);

        assertEquals(0.059173370, ray.getStart().x, 0.01);
        assertEquals(-0.041421358, ray.getStart().y, 0.01);
        assertEquals(-0.099800200, ray.getStart().z, 0.01);

        assertEquals(59.17337, ray.getEnd().x, 0.01);
        assertEquals(-41.42136, ray.getEnd().y, 0.01);
        assertEquals(-99.99999, ray.getEnd().z, 0.01);
    }

    @Test
    public void testCenter() {
        // mouse origin is bottom left
        // picking the center of the screen should give us a solid line along the z axis:
        pickEvent = new PickEvent(settings.getWidth(), settings.getHeight(), settings.getWidth() / 2f, settings.getHeight() / 2f);
        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f modelViewMatrix = new PerspectiveProjectionFab().create(contxt.getSettings());
        PickingRay ray = pickEvent.createPickingRay(projectionMatrix, modelViewMatrix);

        assertEquals(0.0, ray.getStart().x, 0.01);
        assertEquals(0.0, ray.getStart().y, 0.01);
        assertEquals(-0.1, ray.getStart().z, 0.01);

        assertEquals(0.0, ray.getEnd().x, 0.01);
        assertEquals(0.0, ray.getEnd().y, 0.01);
        assertEquals(-100, ray.getEnd().z, 0.01);
    }

}
