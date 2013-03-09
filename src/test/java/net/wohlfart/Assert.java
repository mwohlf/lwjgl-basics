package net.wohlfart;

import static org.junit.Assert.assertEquals;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>Assert class.</p>
 *
 * @author michael
 * @version $Id: $Id
 * @since 0.0.1
 */
public final class Assert {
    private static final float MAX_DIFF = 0.00001f;

    /**
     * <p>assertEqualVec.</p>
     *
     * @param vec1 a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param vec2 a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public static void assertEqualVec(final Vector3f vec1, final Vector3f vec2) {
        assertEquals("x element doesn't match", vec1.x, vec2.x, MAX_DIFF);
        assertEquals("y element doesn't match", vec1.y, vec2.y, MAX_DIFF);
        assertEquals("z element doesn't match", vec1.z, vec2.z, MAX_DIFF);
    }

    /**
     * <p>assertEqualQuaternion.</p>
     *
     * @param quat1 a {@link org.lwjgl.util.vector.Quaternion} object.
     * @param quat2 a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    public static void assertEqualQuaternion(final Quaternion quat1, final Quaternion quat2) {
        assertEquals("x element doesn't match", quat1.x, quat2.x, MAX_DIFF);
        assertEquals("y element doesn't match", quat1.y, quat2.y, MAX_DIFF);
        assertEquals("z element doesn't match", quat1.z, quat2.z, MAX_DIFF);
        assertEquals("w element doesn't match", quat1.w, quat2.w, MAX_DIFF);
    }

}
