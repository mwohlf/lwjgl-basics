package net.wohlfart.tools;

import net.wohlfart.CustomAssert;

import org.junit.Assert;
import org.junit.Test;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * SimpleMathTest class.
 * </p>
 */
public class SimpleMathTest {

    /**
     * <p>
     * testCreateQuaternionA.
     * </p>
     */
    @Test
    public void testCreateQuaternionA() {
        final Quaternion quat = new Quaternion();
        SimpleMath.createQuaternion(new Vector3f(0, 0, 1), new Vector3f(0, 1, 0), quat);

        final Vector3f vec = new Vector3f(0, 0, 1);
        SimpleMath.mul(quat, vec, vec);
        CustomAssert.assertEqualVec(new Vector3f(0, 1, 0), vec);
    }

    /**
     * <p>
     * testCreateQuaternionB.
     * </p>
     */
    @Test
    public void testCreateQuaternionB() {
        final Quaternion quat = new Quaternion();
        SimpleMath.createQuaternion(new Vector3f(1, 1, 0), new Vector3f(1, 0, 0), quat);

        final Vector3f vec = new Vector3f(1, 1, 0);
        SimpleMath.mul(quat, vec, vec);
        CustomAssert.assertEqualVec(new Vector3f(1.41421f, 0, 0), vec);
    }

    /**
     * <p>
     * testCreateQuaternionIdentity.
     * </p>
     */
    @Test
    public void testCreateQuaternionIdentity() {
        final Quaternion quat = new Quaternion();
        SimpleMath.createQuaternion(new Vector3f(10, 0, 0), new Vector3f(10, 0, 0), quat);

        final Vector3f vec = new Vector3f(1, 1, 0);
        SimpleMath.mul(quat, vec, vec);
        CustomAssert.assertEqualVec(new Vector3f(1, 1, 0), vec);
    }

    /**
     * <p>
     * testCreateQuaternion1180degreeA.
     * </p>
     */
    @Test
    public void testCreateQuaternion1180degreeA() {
        final Quaternion quat = new Quaternion();
        SimpleMath.createQuaternion(new Vector3f(10, 0, 0), new Vector3f(-10, 0, 0), quat);

        final Vector3f vec = new Vector3f(3, 0, 0);
        SimpleMath.mul(quat, vec, vec);
        CustomAssert.assertEqualVec(new Vector3f(-3, 0, 0), vec);
    }

    /**
     * <p>
     * testCreateQuaternion1180degreeB.
     * </p>
     */
    @Test
    public void testCreateQuaternion1180degreeB() {
        final Quaternion quat = new Quaternion();
        SimpleMath.createQuaternion(new Vector3f(0, 1, 1), new Vector3f(0, -1, -1), quat);

        final Vector3f vec = new Vector3f(3, 0, 0);
        SimpleMath.mul(quat, vec, vec);
        CustomAssert.assertEqualVec(new Vector3f(-3, 0, 0), vec);
    }

    /**
     * <p>
     * testAddVector.
     * </p>
     */
    @Test
    public void testAddVector() {
        // for the sake of coverage
        final Vector3f result = new Vector3f();
        SimpleMath.add(new Vector3f(0, 2, 1), new Vector3f(3, 0, 0), result);
        CustomAssert.assertEqualVec(new Vector3f(3, 2, 1), result);
    }



    /**
     * <p>
     * testAddVector.
     * </p>
     */
    @Test
    public void testDistance() {
        Vector3f start, end, point;

        start = new Vector3f(-4,1,0);
        end = new Vector3f(+4,1,0);
        point = new Vector3f(0,0,0);
        Assert.assertEquals( SimpleMath.distance(start, end, point), 1, 0.00001d);

        start = new Vector3f(-14,1,0);
        end = new Vector3f(+4,1,0);
        point = new Vector3f(0,0,0);
        Assert.assertEquals( SimpleMath.distance(start, end, point), 1, 0.00001d);

        start = new Vector3f(-4,1,0);
        end = new Vector3f(+14,1,0);
        point = new Vector3f(0,0,0);
        Assert.assertEquals( SimpleMath.distance(start, end, point), 1, 0.00001d);

        start = new Vector3f(-14,2,0);
        end = new Vector3f(+14,2,0);
        point = new Vector3f(0,0,0);
        Assert.assertEquals( SimpleMath.distance(start, end, point), 2, 0.00001d);

        start = new Vector3f(-14,2,0);
        end = new Vector3f(+14,2,0);
        point = new Vector3f(0,-2,0);
        Assert.assertEquals( SimpleMath.distance(start, end, point), 4, 0.00001d);

        start = new Vector3f(-14,2,3);
        end = new Vector3f(+14,2,3);
        point = new Vector3f(0,-2,3);
        Assert.assertEquals( SimpleMath.distance(start, end, point), 4, 0.00001d);

        start = new Vector3f(-14,-4,3);
        end = new Vector3f(+14,-4,3);
        point = new Vector3f(0,-2,3);
        Assert.assertEquals( SimpleMath.distance(start, end, point), 2, 0.00001d);

    }


}
