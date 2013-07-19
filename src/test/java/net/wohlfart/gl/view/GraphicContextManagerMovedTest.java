package net.wohlfart.gl.view;

import static org.junit.Assert.assertEquals;
import net.wohlfart.basic.Settings;
import net.wohlfart.gl.shader.GraphicContextHolder;
import net.wohlfart.gl.shader.PerspectiveProjectionFab;

import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Matrix4f;

public class GraphicContextManagerMovedTest {

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
    public void testCenter() {
        Matrix4f projectionMatrix = new Matrix4f();;
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
