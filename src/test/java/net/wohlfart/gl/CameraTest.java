package net.wohlfart.gl;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import net.wohlfart.tools.SimpleMath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>CameraTest class.</p>
 *
 * @author michael
 * @version $Id: $Id
 * @since 0.0.1
 */
public class CameraTest extends VectorTestBase {

    Camera camera;

    /**
     * <p>runBeforeEveryTest.</p>
     */
    @Before
    public void runBeforeEveryTest() {
        camera = new Camera();
    }

    /**
     * <p>runAfterEveryTest.</p>
     */
    @After
    public void runAfterEveryTest() {
        camera = null;
    }

    /**
     * <p>simpleCheck.</p>
     */
    @Test
    public void simpleCheck() {
        // right
        equals(new Vector3f(0f, 1f, 0f), camera.getUp(new Vector3f()));
        // view
        equals(new Vector3f(0f, 0f, 1f), camera.getDir(new Vector3f()));
    }

    /**
     * <p>simpleRotateXCheck.</p>
     */
    @Test
    public void simpleRotateXCheck() {
        // note the cam has an internal state
        equals(new Vector3f(1f, 0f, 0f), camera.getRght(new Vector3f()));
        equals(new Vector3f(0f, 1f, 0f), camera.getUp(new Vector3f()));
        equals(new Vector3f(0f, 0f, 1f), camera.getDir(new Vector3f()));

        camera.rotate(SimpleMath.PI, new Vector3f(1, 0, 0)); // 180 degree
        equals(new Vector3f(1f, 0f, 0f), camera.getRght(new Vector3f()));
        equals(new Vector3f(0f, -1f, 0f), camera.getUp(new Vector3f()));
        equals(new Vector3f(0f, 0f, -1f), camera.getDir(new Vector3f()));

        camera.rotate(0, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, 0f, -1f), camera.getDir(new Vector3f()));
        equals(new Vector3f(0f, -1f, 0f), camera.getUp(new Vector3f()));

        camera.rotate(SimpleMath.PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, 0f, 1f), camera.getDir(new Vector3f()));
        equals(new Vector3f(0f, 1f, 0f), camera.getUp(new Vector3f()));

        camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, (float) Math.sqrt(0.5), (float) Math.sqrt(0.5)), camera.getDir(new Vector3f()));
        equals(new Vector3f(0f, (float) Math.sqrt(0.5), -(float) Math.sqrt(0.5)), camera.getUp(new Vector3f()));

        camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, 1f, 0f), camera.getDir(new Vector3f()));
        equals(new Vector3f(0f, 0f, -1f), camera.getUp(new Vector3f()));

        camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, (float) Math.sqrt(0.5), -(float) Math.sqrt(0.5)), camera.getDir(new Vector3f()));
        equals(new Vector3f(0f, -(float) Math.sqrt(0.5), -(float) Math.sqrt(0.5)), camera.getUp(new Vector3f()));

        camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, 0f, -1f), camera.getDir(new Vector3f()));
        equals(new Vector3f(0f, -1f, 0f), camera.getUp(new Vector3f()));

        camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, -(float) Math.sqrt(0.5), -(float) Math.sqrt(0.5)), camera.getDir(new Vector3f()));
        equals(new Vector3f(0f, -(float) Math.sqrt(0.5), (float) Math.sqrt(0.5)), camera.getUp(new Vector3f()));

    }

    /**
     * <p>simpleDotProductCheck.</p>
     */
    @Test
    public void simpleDotProductCheck() {
        camera.rotate(SimpleMath.QUARTER_PI, camera.getRght(new Vector3f()));
        assertEquals(0, Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
        camera.rotate(SimpleMath.QUARTER_PI, camera.getUp(new Vector3f()));
        assertEquals(0, Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
        camera.rotate(SimpleMath.QUARTER_PI, camera.getRght(new Vector3f()));
        assertEquals(0, Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
        camera.rotate(SimpleMath.QUARTER_PI, camera.getUp(new Vector3f()));
        assertEquals(0, Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
        camera.rotate(SimpleMath.QUARTER_PI, camera.getRght(new Vector3f()));
        assertEquals(0, Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
        camera.rotate(SimpleMath.QUARTER_PI, camera.getUp(new Vector3f()));
        assertEquals(0, Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
        camera.rotate(SimpleMath.QUARTER_PI, camera.getRght(new Vector3f()));
        assertEquals(0, Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
        camera.rotate(SimpleMath.QUARTER_PI, camera.getUp(new Vector3f()));
        assertEquals(0, Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
    }

    /**
     * <p>randomizedRotationTest.</p>
     */
    @Test
    public void randomizedRotationTest() {
        final Random rand = new Random(1);
        final Vector3f up = camera.getUp(new Vector3f());
        final Vector3f dir = camera.getDir(new Vector3f());
        final Vector3f rght = camera.getRght(new Vector3f());
        // check if the axes stay orthogonal
        for (int i = 0; i < 1000000; i++) {
            assertEquals(0, Vector3f.dot(up, dir), 0.000001);
            assertEquals(0, Vector3f.dot(rght, dir), 0.000001);
            assertEquals(0, Vector3f.dot(up, rght), 0.000001);
            camera.rotate(rand.nextFloat(), new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
        }

    }

}
