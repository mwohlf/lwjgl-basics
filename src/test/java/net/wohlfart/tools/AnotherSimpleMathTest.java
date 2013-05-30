package net.wohlfart.tools;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import net.wohlfart.gl.VectorTestBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * CameraTest class.
 * </p>
 */
public class AnotherSimpleMathTest extends VectorTestBase {

    Quaternion q;

    /**
     * <p>
     * runBeforeEveryTest.
     * </p>
     */
    @Before
    public void runBeforeEveryTest() {
        q = new Quaternion();
    }

    /**
     * <p>
     * runAfterEveryTest.
     * </p>
     */
    @After
    public void runAfterEveryTest() {
        q = null;
    }

    /**
     * <p>
     * simpleCheck.
     * </p>
     */
    @Test
    public void simpleCheck() {
        // right
        equals(new Vector3f(0f, 1f, 0f), SimpleMath.getUp(q, new Vector3f()));
        // view
        equals(new Vector3f(0f, 0f, 1f), SimpleMath.getDir(q, new Vector3f()));
    }

    /**
     * <p>
     * simpleRotateXCheck.
     * </p>
     */
    @Test
    public void simpleRotateXCheck() {
        // note the cam has an internal state
        equals(new Vector3f(1f, 0f, 0f), SimpleMath.getRght(q, new Vector3f()));
        equals(new Vector3f(0f, 1f, 0f), SimpleMath.getUp(q, new Vector3f()));
        equals(new Vector3f(0f, 0f, 1f), SimpleMath.getDir(q, new Vector3f()));

        SimpleMath.rotate(q, SimpleMath.PI, new Vector3f(1, 0, 0)); // 180 degree
        equals(new Vector3f(1f, 0f, 0f), SimpleMath.getRght(q, new Vector3f()));
        equals(new Vector3f(0f, -1f, 0f), SimpleMath.getUp(q, new Vector3f()));
        equals(new Vector3f(0f, 0f, -1f), SimpleMath.getDir(q, new Vector3f()));

        SimpleMath.rotate(q, 0, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, 0f, -1f), SimpleMath.getDir(q, new Vector3f()));
        equals(new Vector3f(0f, -1f, 0f), SimpleMath.getUp(q, new Vector3f()));

        SimpleMath.rotate(q, SimpleMath.PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, 0f, 1f), SimpleMath.getDir(q, new Vector3f()));
        equals(new Vector3f(0f, 1f, 0f), SimpleMath.getUp(q, new Vector3f()));

        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, (float) Math.sqrt(0.5), (float) Math.sqrt(0.5)), SimpleMath.getDir(q, new Vector3f()));
        equals(new Vector3f(0f, (float) Math.sqrt(0.5), -(float) Math.sqrt(0.5)), SimpleMath.getUp(q, new Vector3f()));

        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, 1f, 0f), SimpleMath.getDir(q, new Vector3f()));
        equals(new Vector3f(0f, 0f, -1f), SimpleMath.getUp(q, new Vector3f()));

        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, (float) Math.sqrt(0.5), -(float) Math.sqrt(0.5)), SimpleMath.getDir(q, new Vector3f()));
        equals(new Vector3f(0f, -(float) Math.sqrt(0.5), -(float) Math.sqrt(0.5)), SimpleMath.getUp(q, new Vector3f()));

        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, 0f, -1f), SimpleMath.getDir(q, new Vector3f()));
        equals(new Vector3f(0f, -1f, 0f), SimpleMath.getUp(q, new Vector3f()));

        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
        equals(new Vector3f(0f, -(float) Math.sqrt(0.5), -(float) Math.sqrt(0.5)), SimpleMath.getDir(q, new Vector3f()));
        equals(new Vector3f(0f, -(float) Math.sqrt(0.5), (float) Math.sqrt(0.5)), SimpleMath.getUp(q, new Vector3f()));

    }

    /**
     * <p>
     * simpleDotProductCheck.
     * </p>
     */
    @Test
    public void simpleDotProductCheck() {
        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, SimpleMath.getRght(q, new Vector3f()));
        assertEquals(0, Vector3f.dot(SimpleMath.getDir(q, new Vector3f()), SimpleMath.getUp(q, new Vector3f())), 0.0001);
        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, SimpleMath.getUp(q, new Vector3f()));
        assertEquals(0, Vector3f.dot(SimpleMath.getDir(q, new Vector3f()), SimpleMath.getUp(q, new Vector3f())), 0.0001);
        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, SimpleMath.getRght(q, new Vector3f()));
        assertEquals(0, Vector3f.dot(SimpleMath.getDir(q, new Vector3f()), SimpleMath.getUp(q, new Vector3f())), 0.0001);
        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, SimpleMath.getUp(q, new Vector3f()));
        assertEquals(0, Vector3f.dot(SimpleMath.getDir(q, new Vector3f()), SimpleMath.getUp(q, new Vector3f())), 0.0001);
        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, SimpleMath.getRght(q, new Vector3f()));
        assertEquals(0, Vector3f.dot(SimpleMath.getDir(q, new Vector3f()), SimpleMath.getUp(q, new Vector3f())), 0.0001);
        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, SimpleMath.getUp(q, new Vector3f()));
        assertEquals(0, Vector3f.dot(SimpleMath.getDir(q, new Vector3f()), SimpleMath.getUp(q, new Vector3f())), 0.0001);
        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, SimpleMath.getRght(q, new Vector3f()));
        assertEquals(0, Vector3f.dot(SimpleMath.getDir(q, new Vector3f()), SimpleMath.getUp(q, new Vector3f())), 0.0001);
        SimpleMath.rotate(q, SimpleMath.QUARTER_PI, SimpleMath.getUp(q, new Vector3f()));
        assertEquals(0, Vector3f.dot(SimpleMath.getDir(q, new Vector3f()), SimpleMath.getUp(q, new Vector3f())), 0.0001);
    }

    /**
     * <p>
     * randomizedRotationTest.
     * </p>
     */
    @Test
    public void randomizedRotationTest() {
        final Random rand = new Random(1);
        // check if the axes stay orthogonal
        for (int i = 0; i < 1000000; i++) {
            final Vector3f up = SimpleMath.getUp(q, new Vector3f());
            final Vector3f dir = SimpleMath.getDir(q, new Vector3f());
            final Vector3f rght = SimpleMath.getRght(q, new Vector3f());
            assertEquals(0, Vector3f.dot(up, dir), 0.000001);
            assertEquals(0, Vector3f.dot(rght, dir), 0.000001);
            assertEquals(0, Vector3f.dot(up, rght), 0.000001);
            SimpleMath.rotate(q, rand.nextFloat(), new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
        }
    }

}
