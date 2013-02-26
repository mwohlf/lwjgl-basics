package net.wohlfart.tools;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public final class SimpleMath {
    private static final float EPSILON = 0.0000001f;

    private static final Random RANDOM = new Random();

    public static final float QUARTER_PI = (float) (0.25d * Math.PI);
    public static final float HALF_PI = (float) (0.5d * Math.PI);
    public static final float PI = (float) Math.PI;
    public static final float TWO_PI = (float) (2d * Math.PI);

    public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);

    public static final Matrix4f UNION_MATRIX = new Matrix4f();

    // --------- trigeometric stuff

    public static float sin(final float f) {
        return (float) Math.sin(f);
    }

    public static float cos(final float f) {
        return (float) Math.cos(f);
    }

    public static float tan(float angle) {
        return (float) Math.tan(angle);
    }

    public static float rad2deg(final float rad) {
        return rad * 360f / SimpleMath.TWO_PI;
    }

    public static float deg2rad(final float deg) {
        return deg * SimpleMath.TWO_PI / 360f;
    }

    public static float coTan(final float angle) {
        return (float) (1f / Math.tan(angle));
    }

    public static float sqrt(final float f) {
        return (float) Math.sqrt(f);
    }

    // --------- random function

    public static int random(final int i) {
        return RANDOM.nextInt(i); // i exclusive
    }

    public static int random(final int min, final int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    public static float random(final float min, final float max) {
        return RANDOM.nextFloat() * (max - min) + min;
    }

    // --------- quaternion and vector stuff
    // mostly from http://content.gpwiki.org/index.php/OpenGL:Tutorials:Using_Quaternions_to_represent_rotation

    /**
     * rotate the vector by a rotation defined by the quaternion
     *
     * @param q
     * @param vec
     */

    public static void mul(final Quaternion q, final Vector3f vec, final Vector3f result) {
        float xx, yy, zz;
        // @formatter:off
        xx = q.w * q.w * vec.x + 2 * q.y * q.w * vec.z - 2 * q.z * q.w * vec.y
           + q.x * q.x * vec.x + 2 * q.y * q.x * vec.y + 2 * q.z * q.x * vec.z
           - q.z * q.z * vec.x - q.y * q.y * vec.x;

        yy = 2 * q.x * q.y * vec.x + q.y * q.y * vec.y + 2 * q.z * q.y * vec.z
           + 2 * q.w * q.z * vec.x - q.z * q.z * vec.y + q.w * q.w * vec.y
           - 2 * q.x * q.w * vec.z - q.x * q.x * vec.y;

        zz = 2 * q.x * q.z * vec.x + 2 * q.y * q.z * vec.y + q.z * q.z * vec.z
           - 2 * q.w * q.y * vec.x - q.y * q.y * vec.z + 2 * q.w * q.x * vec.y
           - q.x * q.x * vec.z + q.w * q.w * vec.z;
        // @formatter:on

        result.x = xx;
        result.y = yy;
        result.z = zz;
    }

    /**
     * add one vector to another vecto
     *
     * @param translation
     * @param vec
     * @param result
     */
    public static void add(final Vector3f translation, final Vector3f vec, final Vector3f result) {
        result.x = vec.x + translation.x;
        result.y = vec.y + translation.y;
        result.z = vec.z + translation.z;
    }

    /**
     * create a rotation quaternion defined by a start and an end vector, the rotation will be the rotation needed to transform the first vector into the second
     *
     * taken from: https://bitbucket.org/sinbad/ogre/src/9db75e3ba05c/OgreMain/include/OgreVector3.h#cl-651
     *
     * @param start
     * @param end
     * @param result
     */
    public static Quaternion createQuaternion(final Vector3f start, final Vector3f end, final Quaternion result) {

        final Vector3f startNorm = start.normalise(new Vector3f());
        final Vector3f endNorm = end.normalise(new Vector3f());

        final float dot = Vector3f.dot(startNorm, endNorm);

        if (dot >= +(1f - EPSILON)) {
            // vectors are equals
            Quaternion.setIdentity(result);
            return result;
        }
        if (dot <= -(1f - EPSILON)) {
            // opposite direction, create a random axis
            Vector3f axis = Vector3f.cross(startNorm, X_AXIS, new Vector3f());
            if (axis.length() <= EPSILON) {// pick another if colinear
                axis = Vector3f.cross(startNorm, Y_AXIS, new Vector3f());
            }
            axis.normalise();
            result.setFromAxisAngle(new Vector4f(axis.x, axis.y, axis.z, PI));
            return result;
        }

        final float s = sqrt((1f + dot) * 2f);
        final float invs = 1 / s;
        final Vector3f c = Vector3f.cross(startNorm, endNorm, new Vector3f());

        result.x = c.x * invs;
        result.y = c.y * invs;
        result.z = c.z * invs;
        result.w = s * 0.5f;

        result.normalise(result);
        return result;
    }

    public static int getPowerOfTwoBiggerThan(int n) {
        if (n < 0) {
            return 0;
        }
        --n;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n + 1;
    }

    public static Matrix4f createMatrix(Vector3f currentTranslation, Matrix4f matrix) {

        matrix.m00 = 1;
        matrix.m01 = 0;
        matrix.m02 = 0;
        matrix.m03 = 0;

        matrix.m10 = 0;
        matrix.m11 = 1;
        matrix.m12 = 0;
        matrix.m13 = 0;

        matrix.m20 = 0;
        matrix.m21 = 0;
        matrix.m22 = 1;
        matrix.m23 = 0;

        matrix.m30 = currentTranslation.x;
        matrix.m31 = currentTranslation.y;
        matrix.m32 = currentTranslation.z;
        matrix.m33 = 1;

        return matrix;
    }


    public static Matrix4f convert(final Vector3f move, final Quaternion rot, final Matrix4f mat) {
        //final SimpleMatrix4f mat = new SimpleMatrix4f();

        final float xx = rot.x * rot.x;
        final float xy = rot.x * rot.y;
        final float xz = rot.x * rot.z;
        final float xw = rot.x * rot.w;

        final float yy = rot.y * rot.y;
        final float yz = rot.y * rot.z;
        final float yw = rot.y * rot.w;

        final float zz = rot.z * rot.z;
        final float zw = rot.z * rot.w;

        // column-row syntax
        mat.m00 = 1 - 2 * (yy + zz);
        mat.m10 = 2 * (xy - zw);
        mat.m20 = 2 * (xz + yw);
        mat.m30 = move.x;

        mat.m01 = 2 * (xy + zw);
        mat.m11 = 1 - 2 * (xx + zz);
        mat.m21 = 2 * (yz - xw);
        mat.m31 = move.y;

        mat.m02 = 2 * (xz - yw);
        mat.m12 = 2 * (yz + xw);
        mat.m22 = 1 - 2 * (xx + yy);
        mat.m32 = move.z;

        mat.m03 = 0;
        mat.m13 = 0;
        mat.m23 = 0;
        mat.m33 = 1;

        return mat;
    }

    /**
     * @param move
     *            vector describing a move
     * @return a matrix
     */
    public static Matrix4f convert(final Vector3f move, final Matrix4f mat) {

        // column-row syntax
        mat.m00 = 1;
        mat.m10 = 0;
        mat.m20 = 0;
        mat.m30 = move.x;

        mat.m01 = 0;
        mat.m11 = 1;
        mat.m21 = 0;
        mat.m31 = move.y;

        mat.m02 = 0;
        mat.m12 = 0;
        mat.m22 = 1;
        mat.m32 = move.z;

        mat.m03 = 0;
        mat.m13 = 0;
        mat.m23 = 0;
        mat.m33 = 1;

        return mat;
    }

    /**
     * @param rot
     *            quaternion describing a rotation
     * @return a matrix
     */
    public static Matrix4f convert(final Quaternion rot, final Matrix4f mat) {
        //final SimpleMatrix4f mat = new SimpleMatrix4f();

        final float xx = rot.x * rot.x;
        final float xy = rot.x * rot.y;
        final float xz = rot.x * rot.z;
        final float xw = rot.x * rot.w;

        final float yy = rot.y * rot.y;
        final float yz = rot.y * rot.z;
        final float yw = rot.y * rot.w;

        final float zz = rot.z * rot.z;
        final float zw = rot.z * rot.w;

        // column-row syntax
        mat.m00 = 1 - 2 * (yy + zz);
        mat.m10 = 2 * (xy - zw);
        mat.m20 = 2 * (xz + yw);
        mat.m30 = 0;

        mat.m01 = 2 * (xy + zw);
        mat.m11 = 1 - 2 * (xx + zz);
        mat.m21 = 2 * (yz - xw);
        mat.m31 = 0;

        mat.m02 = 2 * (xz - yw);
        mat.m12 = 2 * (yz + xw);
        mat.m22 = 1 - 2 * (xx + yy);
        mat.m32 = 0;

        mat.m03 = 0;
        mat.m13 = 0;
        mat.m23 = 0;
        mat.m33 = 1;

        return mat;
    }

    public static void mul(Vector3f dir, float a) {
        dir.x *= a;
        dir.y *= a;
        dir.z *= a;
    }

    public static void sum(Vector3f target, Vector3f a, Vector3f b, Vector3f c) {
        target.x = a.x + b.x + c.x;
        target.y = a.y + b.y + c.y;
        target.z = a.z + b.z + c.z;
    }

}
