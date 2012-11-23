package net.wohlfart.gl.tools;

import static org.junit.Assert.assertEquals;

import org.lwjgl.util.vector.Vector3f;


public class Assert {

    // compare two vectors
    public static void assertEqualVector(final Vector3f expected, final Vector3f actual) {
        assertEquals(expected.x, actual.x, 0.01); // 2% tollerance is too much ?
        assertEquals(expected.y, actual.y, 0.01);
        assertEquals(expected.z, actual.z, 0.01);
    }

}
