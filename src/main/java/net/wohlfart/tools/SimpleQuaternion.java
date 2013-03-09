package net.wohlfart.tools;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

// see: http://code.google.com/p/jmonkeyengine/source/browse/branches/jme3/src/core/com/jme3/math/Quaternion.java?r=5231
/**
 * <p>SimpleQuaternion class.</p>
 *
 *
 *
 */
@SuppressWarnings("serial")
public class SimpleQuaternion extends Quaternion {

    /**
     * <p>normalizeLocal.</p>
     */
    public void normalizeLocal() {
        final float l = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        x = x / l;
        y = y / l;
        z = z / l;
        w = w / l;
    }

    /**
     * <p>multLocal.</p>
     *
     * @param vec a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Vector3f multLocal(Vector3f vec) {
        float xx, yy, zz;

        xx = w * w * vec.x + 2 * y * w * vec.z - 2 * z * w * vec.y + x * x * vec.x + 2 * y * x * vec.y + 2 * z * x * vec.z - z * z * vec.x - y * y * vec.x;

        yy = 2 * x * y * vec.x + y * y * vec.y + 2 * z * y * vec.z + 2 * w * z * vec.x - z * z * vec.y + w * w * vec.y - 2 * x * w * vec.z - x * x * vec.y;

        zz = 2 * x * z * vec.x + 2 * y * z * vec.y + z * z * vec.z - 2 * w * y * vec.x - y * y * vec.z + 2 * w * x * vec.y - x * x * vec.z + w * w * vec.z;

        vec.x = xx;
        vec.y = yy;
        vec.z = zz;
        return vec;
    }

}
