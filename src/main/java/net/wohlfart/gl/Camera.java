package net.wohlfart.gl;

import net.wohlfart.tools.SimpleMath;
import net.wohlfart.tools.SimpleQuaternion;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

// To make a camera you typically use three vectors: Position, View, and Up,
// we derive view and up from the quaternion and keep the camera at the origin
//
// see: http://www.gamedev.net/page/resources/_/technical/math-and-physics/a-simple-quaternion-based-camera-r1997
// see: http://code.google.com/p/jmonkeyengine/source/browse/branches/jme3/src/core/com/jme3/math/Quaternion.java?r=5231
// see: http://introcs.cs.princeton.edu/java/32class/Quaternion.java.html
// see: http://www.mathworks.de/de/help/aeroblks/quaternionrotation.html
// see: http://stackoverflow.com/questions/4870393/rotating-coordinate-system-via-a-quaternion
public class Camera implements CanRotate {
    private final SimpleQuaternion q = new SimpleQuaternion();

    @Override
    public Quaternion getRotation() {
        return q;
    }

    // the (1,0,0) vector / X axis
    @Override
    public Vector3f getRght(final Vector3f result) {
        result.x = (1f - 2f * (q.y * q.y + q.z * q.z));
        result.y = 2f * (q.x * q.y - q.w * q.z);
        result.z = 2f * (q.x * q.z + q.w * q.y);
        return result.normalise(new Vector3f());
    }

    // the (0,1,0) vector / Y axis
    @Override
    public Vector3f getUp(final Vector3f result) {
        result.x = 2f * (q.x * q.y + q.w * q.z);
        result.y = (1f - 2f * (q.z * q.z + q.x * q.x));
        result.z = 2f * (q.y * q.z - q.w * q.x);
        return result.normalise(new Vector3f());
    }

    // the (0,0,1) vector / Z axis
    @Override
    public Vector3f getDir(final Vector3f result) {
        result.x = 2f * (q.x * q.z - q.w * q.y);
        result.y = 2f * (q.y * q.z + q.w * q.x);
        result.z = (1f - 2f * (q.x * q.x + q.y * q.y));
        return result.normalise(new Vector3f());
    }

    /**
     * @param angle
     *            rotation in rad
     * @param axis
     *            rotation axis, must be in the cam's coord system e.g. use (0,1,0)
     */
    @Override
    public void rotate(final float angle, final Vector3f axis) {
        axis.normalise();
        final SimpleQuaternion rot = new SimpleQuaternion();
        final double n = Math.sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z);
        final float sin = (float) (Math.sin(0.5 * angle) / n);
        rot.x = axis.x * sin;
        rot.y = axis.y * sin;
        rot.z = axis.z * sin;
        rot.w = (float) Math.cos(0.5 * angle);

        Quaternion.mul(rot, q, rot);
        rot.normalizeLocal();
        q.set(rot);
    }

    public void lookThrough() {
        // GL11.glLoadIdentity();
        float x, y, z;
        final float angle = 2f * (float) Math.acos(q.w);
        final float s = (float) Math.sqrt(1 - q.w * q.w); // assuming quaternion normalised then w is less than 1, so term always positive.

        // test to avoid divide by zero, s is always positive due to sqrt
        if (s < 0.001) {
            // if s close to zero then direction of axis not important
            x = q.x; // if it is important that axis is normalised then replace with x=1; y=z=0;
            y = q.y;
            z = q.z;
        } else {
            x = q.x / s; // normalize axis
            y = q.y / s;
            z = q.z / s;
        }
        GL11.glRotatef(SimpleMath.rad2deg(angle), x, y, z);
    }

    @Override
    public String toString() {
        final Vector3f up = getUp(new Vector3f());
        final Vector3f dir = getDir(new Vector3f());
        final Vector3f rght = getRght(new Vector3f());

        return "" + "Cam: [" + q.toString() + "] \n" + " up: " + up + " size:" + Math.sqrt(up.x * up.x + up.y * up.y + up.z * up.z) + "\n" + " dir: " + dir
                + " size:" + Math.sqrt(dir.x * dir.x + dir.y * dir.y + dir.z * dir.z) + "\n" + " rght: " + rght + " size:"
                + Math.sqrt(rght.x * rght.x + rght.y * rght.y + rght.z * rght.z) + "\n"

                + " dot: up.dir" + Vector3f.dot(up, dir) + " dot: up.rght" + Vector3f.dot(up, rght) + " dot: dir.rght" + Vector3f.dot(dir, rght);
    }

}
