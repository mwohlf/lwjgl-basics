package net.wohlfart.gl;


import static org.junit.Assert.assertEquals;
import net.wohlfart.tools.SimpleMath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Vector3f;


public class CameraTest extends VectorTestBase {

	Camera camera;

    @Before
    public void runBeforeEveryTest() {
        camera = new Camera();
    }

    @After
    public void runAfterEveryTest() {
    	camera = null;
    }


    @Test
    public void simpleCheck() {
    	// right
    	equals(new Vector3f( 0f, 1f, 0f), camera.getUp(new Vector3f()));
    	// view
    	equals(new Vector3f( 0f, 0f,-1f), camera.getDir(new Vector3f()));
    }


    @Test
    public void simpleRotateXCheck() {
    	// note the cam has an internal state
    	equals(new Vector3f(-1f, 0f, 0f), camera.getRght(new Vector3f()));
    	equals(new Vector3f( 0f, 1f, 0f), camera.getUp(new Vector3f()));
    	equals(new Vector3f( 0f, 0f,-1f), camera.getDir(new Vector3f()));
    	assertEquals(0.0, camera.getRoll(), 0.0001d);
    	assertEquals(0.0, camera.getPitch(), 0.0001d);
    	assertEquals(0.0, camera.getYaw(), 0.0001d);

    	camera.rotate(SimpleMath.PI, new Vector3f(1, 0, 0));  // 180 degree
    	equals(new Vector3f(-1f, 0f, 0f), camera.getRght(new Vector3f()));
    	equals(new Vector3f( 0f,-1f, 0f), camera.getUp(new Vector3f()));
    	equals(new Vector3f( 0f, 0f, 1f), camera.getDir(new Vector3f()));
    	assertEquals(0.0, camera.getRoll(), 0.0001d);
    	assertEquals(-SimpleMath.PI, camera.getPitch(), 0.0001d);
    	assertEquals(0.0, camera.getYaw(), 0.0001d);

    	camera.rotate(0, new Vector3f(1, 0, 0));
    	equals(new Vector3f( 0f, 0f, 1f), camera.getDir(new Vector3f()));
    	equals(new Vector3f( 0f,-1f, 0f), camera.getUp(new Vector3f()));
    	assertEquals(0.0, camera.getRoll(), 0.0001d);
    	assertEquals(-SimpleMath.PI, camera.getPitch(), 0.0001d);
    	assertEquals(0.0, camera.getYaw(), 0.0001d);

    	camera.rotate(SimpleMath.PI, new Vector3f(1, 0, 0));
    	equals(new Vector3f( 0f, 0f,-1f), camera.getDir(new Vector3f()));
    	equals(new Vector3f( 0f, 1f, 0f), camera.getUp(new Vector3f()));
    	assertEquals(0.0, camera.getRoll(), 0.0001d);
    	assertEquals(0.0, camera.getPitch(), 0.0001d);
    	assertEquals(0.0, camera.getYaw(), 0.0001d);

    	camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
    	equals(new Vector3f( 0f, (float)Math.sqrt(0.5),-(float)Math.sqrt(0.5)), camera.getDir(new Vector3f()));
    	equals(new Vector3f( 0f, (float)Math.sqrt(0.5), (float)Math.sqrt(0.5)), camera.getUp(new Vector3f()));
    	assertEquals(0.0, camera.getRoll(), 0.0001d);
    	assertEquals(SimpleMath.QUARTER_PI, camera.getPitch(), 0.0001d);
    	assertEquals(0.0, camera.getYaw(), 0.0001d);

    	camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
    	equals(new Vector3f( 0f,  1f, 0f), camera.getDir(new Vector3f()));
    	equals(new Vector3f( 0f,  0f, 1f), camera.getUp(new Vector3f()));
    	assertEquals(0.0, camera.getRoll(), 0.0001d);
    	assertEquals(SimpleMath.HALF_PI, camera.getPitch(), 0.0001d);
    	assertEquals(0.0, camera.getYaw(), 0.0001d);

    	camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
    	equals(new Vector3f( 0f,  (float)Math.sqrt(0.5), (float)Math.sqrt(0.5)), camera.getDir(new Vector3f()));
    	equals(new Vector3f( 0f, -(float)Math.sqrt(0.5), (float)Math.sqrt(0.5)), camera.getUp(new Vector3f()));
    	assertEquals(0.0, camera.getRoll(), 0.0001d);
    	assertEquals(SimpleMath.HALF_PI + SimpleMath.QUARTER_PI, camera.getPitch(), 0.0001d);
    	assertEquals(0.0, camera.getYaw(), 0.0001d);

    	camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
    	equals(new Vector3f( 0f, 0f, 1f), camera.getDir(new Vector3f()));
    	equals(new Vector3f( 0f,-1f, 0f), camera.getUp(new Vector3f()));
    	assertEquals(0.0, camera.getRoll(), 0.0001d);
    	assertEquals(-SimpleMath.PI, camera.getPitch(), 0.0001d);
    	assertEquals(0.0, camera.getYaw(), 0.0001d);

    	camera.rotate(SimpleMath.QUARTER_PI, new Vector3f(1, 0, 0));
    	equals(new Vector3f( 0f,-(float)Math.sqrt(0.5), (float)Math.sqrt(0.5)), camera.getDir(new Vector3f()));
    	equals(new Vector3f( 0f,-(float)Math.sqrt(0.5),-(float)Math.sqrt(0.5)), camera.getUp(new Vector3f()));
    	assertEquals(0.0, camera.getRoll(), 0.0001d);
    	assertEquals(-SimpleMath.PI + SimpleMath.QUARTER_PI, camera.getPitch(), 0.0001d);
    	assertEquals(0.0, camera.getYaw(), 0.0001d);

    }


    @Test
    public void simpleDotProductCheck() {
    	camera.rotate(SimpleMath.QUARTER_PI, camera.getRght(new Vector3f()));
    	assertEquals(0,Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
    	camera.rotate(SimpleMath.QUARTER_PI, camera.getUp(new Vector3f()));
    	assertEquals(0,Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
    	camera.rotate(SimpleMath.QUARTER_PI, camera.getRght(new Vector3f()));
    	assertEquals(0,Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
    	camera.rotate(SimpleMath.QUARTER_PI, camera.getUp(new Vector3f()));
    	assertEquals(0,Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
    	camera.rotate(SimpleMath.QUARTER_PI, camera.getRght(new Vector3f()));
    	assertEquals(0,Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
    	camera.rotate(SimpleMath.QUARTER_PI, camera.getUp(new Vector3f()));
    	assertEquals(0,Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
    	camera.rotate(SimpleMath.QUARTER_PI, camera.getRght(new Vector3f()));
    	assertEquals(0,Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);
    	camera.rotate(SimpleMath.QUARTER_PI, camera.getUp(new Vector3f()));
    	assertEquals(0,Vector3f.dot(camera.getDir(new Vector3f()), camera.getUp(new Vector3f())), 0.0001);

    }


}
