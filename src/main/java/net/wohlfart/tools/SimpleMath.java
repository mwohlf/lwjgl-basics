package net.wohlfart.tools;

import java.util.Random;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * <p>toolkit class to work with vectors, matrices and quaternions.</p>
 */
public final class SimpleMath {
    private static final float EPSILON = 0.0000001f;

    private static final Random RANDOM = new Random();

    /** Constant <code>QUARTER_PI=(float) (0.25d * Math.PI)</code> */
    public static final float QUARTER_PI = (float) (0.25d * Math.PI);
    /** Constant <code>HALF_PI=(float) (0.5d * Math.PI)</code> */
    public static final float HALF_PI = (float) (0.5d * Math.PI);
    /** Constant <code>PI=(float) Math.PI</code> */
    public static final float PI = (float) Math.PI;
    /** Constant <code>TWO_PI=(float) (2d * Math.PI)</code> */
    public static final float TWO_PI = (float) (2d * Math.PI);

    /** Constant <code>X_AXIS</code> */
    public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    /** Constant <code>Y_AXIS</code> */
    public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    /** Constant <code>Z_AXIS</code> */
    public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);

    /** Constant <code>UNION_MATRIX</code> */
    public static final Matrix4f UNION_MATRIX = Matrix4f.setIdentity(new Matrix4f());
    /** Constant <code>NULL_MATRIX</code> this is used to indicate a marix that is not yet initialized */
    public static final Matrix4f NULL_MATRIX = Matrix4f.setZero(new Matrix4f());


    private SimpleMath() {
         // tools class with only static methods
    }

    // --------- trigeometric stuff

    /**
     * <p>sin.</p>
     *
     * @param f a float.
     * @return a float.
     */
    public static float sin(float f) {
        return (float) Math.sin(f);
    }

    /**
     * <p>cos.</p>
     *
     * @param f a float.
     * @return a float.
     */
    public static float cos(float f) {
        return (float) Math.cos(f);
    }

    /**
     * <p>tan.</p>
     *
     * @param angle a float.
     * @return a float.
     */
    public static float tan(float angle) {
        return (float) Math.tan(angle);
    }

    /**
     * <p>rad2deg.</p>
     *
     * @param rad a float.
     * @return a float.
     */
    public static float rad2deg(final float rad) {
        return rad * 360f / SimpleMath.TWO_PI;
    }

    /**
     * <p>deg2rad.</p>
     *
     * @param deg a float.
     * @return a float.
     */
    public static float deg2rad(final float deg) {
        return deg * SimpleMath.TWO_PI / 360f;
    }

    /**
     * <p>coTan.</p>
     *
     * @param angle a float.
     * @return a float.
     */
    public static float coTan(float angle) {
        return (float) (1f / Math.tan(angle));
    }

    /**
     * <p>sqrt.</p>
     *
     * @param f a float.
     * @return a float.
     */
    public static float sqrt(float f) {
        return (float) Math.sqrt(f);
    }

    public static float sqare(float nearPlane) {
        return nearPlane * nearPlane;
    }


    // --------- random function

    /**
     * <p>random.</p>
     *
     * @param i a int.
     * @return a int.
     */
    public static int random(final int i) {
        return RANDOM.nextInt(i); // i exclusive
    }

    /**
     * <p>random.</p>
     *
     * @param min a int.
     * @param max a int.
     * @return a int.
     */
    public static int random(final int min, final int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    /**
     * <p>random.</p>
     *
     * @param min a float.
     * @param max a float.
     * @return a float.
     */
    public static float random(final float min, final float max) {
        return RANDOM.nextFloat() * (max - min) + min;
    }

    // --------- quaternion and vector stuff
    // mostly from http://content.gpwiki.org/index.php/OpenGL:Tutorials:Using_Quaternions_to_represent_rotation

    /**
     * rotate the vector by a rotation defined by the quaternion
     *
     * @param q a {@link org.lwjgl.util.vector.Quaternion} object.
     * @param vec a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param result a {@link org.lwjgl.util.vector.Vector3f} object.
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



    public static Vector3f scale(Vector3f in, float scale, Vector3f out) {
        out.x = in.x * scale;
        out.y = in.y * scale;
        out.z = in.z * scale;
        return out;
    }

    /**
     * create a rotation quaternion defined by a start and an end vector,
     * the rotation will be the rotation needed to transform the first vector into the second
     *
     * taken from: https://bitbucket.org/sinbad/ogre/src/9db75e3ba05c/OgreMain/include/OgreVector3.h#cl-651
     *
     * @param start a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param end a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param result a {@link org.lwjgl.util.vector.Quaternion} object.
     * @return a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    public static Quaternion createQuaternion(Vector3f start, Vector3f end, Quaternion result) {

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

    /**
     * <p>getPowerOfTwoBiggerThan.</p>
     *
     * @param n a int.
     * @return a int.
     */
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

    /**
     * <p>createMatrix.</p>
     *
     * @param currentTranslation a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param matrix a {@link org.lwjgl.util.vector.Matrix4f} object.
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
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


    /**
     * <p>convert.</p>
     *
     * @param move a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param ROTATION a {@link org.lwjgl.util.vector.Quaternion} object.
     * @param mat a {@link org.lwjgl.util.vector.Matrix4f} object.
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
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
     * <p>convert.</p>
     *
     * @param move
     *            vector describing a move
     * @return a matrix
     * @param mat a {@link org.lwjgl.util.vector.Matrix4f} object.
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
     * <p>convert.</p>
     *
     * @param ROTATION
     *            quaternion describing a rotation
     * @return a matrix
     * @param mat a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    public static Matrix4f convert(Quaternion rot, Matrix4f mat) {
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

    /**
     * <p>mul.</p>
     *
     * @param dir a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param a a float.
     */
    public static void mul(Vector3f dir, float a) {
        dir.x *= a;
        dir.y *= a;
        dir.z *= a;
    }

    /**
     * <p>sum.</p>
     *
     * @param result a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param a a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param b a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param c a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public static void add(Vector3f a, Vector3f b, Vector3f c, Vector3f result) {
        result.x = a.x + b.x + c.x;
        result.y = a.y + b.y + c.y;
        result.z = a.z + b.z + c.z;
    }

    /**
     * add one vector to another vector
     *
     * @param a a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param b a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param result a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public static void add(Vector3f a, Vector3f b, Vector3f result) {
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        result.z = a.z + b.z;
    }

    /**
     * substract one vector from another vector
     *
     * @param a a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param b a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param result a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public static void sub(Vector3f a, Vector3f b, Vector3f result) {
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        result.z = a.z - b.z;
    }

    /**
     * <p>multiply a vector with a matrix</p>
     *
     * @param n a {@link org.lwjgl.util.vector.Matrix4f} object.
     * @param in a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param out a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public static void mul(Matrix4f n, Vector3f in, Vector3f out) {
        out.x = n.m00 * in.x + n.m01 * in.y + n.m02 * in.z;
        out.y = n.m10 * in.x + n.m11 * in.y + n.m12 * in.z;
        out.z = n.m20 * in.x + n.m21 * in.y + n.m22 * in.z;
    }

    // the (1,0,0) vector / X axis
    public static Vector3f getRght(Quaternion q, Vector3f result) {
        result.x = 1f - 2f * (q.y * q.y + q.z * q.z);
        result.y = 2f * (q.x * q.y - q.w * q.z);
        result.z = 2f * (q.x * q.z + q.w * q.y);
        return result.normalise(new Vector3f());
    }

    // the (0,1,0) vector / Y axis
    public static Vector3f getUp(Quaternion q, Vector3f result) {
        result.x = 2f * (q.x * q.y + q.w * q.z);
        result.y = 1f - 2f * (q.z * q.z + q.x * q.x);
        result.z = 2f * (q.y * q.z - q.w * q.x);
        return result.normalise(new Vector3f());
    }

    // the (0,0,1) vector / Z axis
    public static Vector3f getDir(Quaternion q, Vector3f result) {
        result.x = 2f * (q.x * q.z - q.w * q.y);
        result.y = 2f * (q.y * q.z + q.w * q.x);
        result.z = 1f - 2f * (q.x * q.x + q.y * q.y);
        return result.normalise(new Vector3f());
    }

    // TODO: fix this: without allocating new memory...
    // it would also be nice to be thread-safe...
    final static Quaternion ROTATION = new Quaternion();
    public static void rotate(Quaternion q, float angle, Vector3f axis) {
        axis.normalise();
        final double n = Math.sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z);
        final float sin = (float) (Math.sin(0.5 * angle) / n);
        ROTATION.x = axis.x * sin;
        ROTATION.y = axis.y * sin;
        ROTATION.z = axis.z * sin;
        ROTATION.w = (float) Math.cos(0.5 * angle);

        Quaternion.mul(ROTATION, q, ROTATION);
        normalizeLocal(ROTATION);
        q.set(ROTATION);
    }

    /**
     * <p>normalizeLocal.</p>
     */
    public static void normalizeLocal(Quaternion q) {
        final float l = (float) Math.sqrt(q.x * q.x + q.y * q.y + q.z * q.z + q.w * q.w);
        q.x = q.x / l;
        q.y = q.y / l;
        q.z = q.z / l;
        q.w = q.w / l;
    }

    /**
     * <p>multLocal.</p>
     *
     * @param vec a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public static Vector3f multLocal(Vector3f vec, Quaternion q) {
        float xx, yy, zz;
        // @formatter:off
        xx = q.w * q.w * vec.x + 2 * q.y * q.w * vec.z
             - 2 * q.z * q.w * vec.y + q.x * q.x * vec.x
             + 2 * q.y * q.x * vec.y + 2 * q.z * q.x * vec.z
             - q.z * q.z * vec.x - q.y * q.y * vec.x;

        yy = 2 * q.x * q.y * vec.x + q.y * q.y * vec.y
             + 2 * q.z * q.y * vec.z + 2 * q.w * q.z * vec.x
             - q.z * q.z * vec.y + q.w * q.w * vec.y
             - 2 * q.x * q.w * vec.z - q.x * q.x * vec.y;

        zz = 2 * q.x * q.z * vec.x + 2 * q.y * q.z * vec.y
             + q.z * q.z * vec.z - 2 * q.w * q.y * vec.x
             - q.y * q.y * vec.z + 2 * q.w * q.x * vec.y
             - q.x * q.x * vec.z + q.w * q.w * vec.z;
        // @formatter:on
        vec.x = xx;
        vec.y = yy;
        vec.z = zz;
        return vec;
    }


    /**
     * calculate a 3x3 matrix,
     * its either inverse and transpose of model matrix or just upper corner of that matrix if no uniform scaling is used
     *
     * TODO: check for uniform scaling
     *
     * @param m4 the model to world matrix
     * @return
     */
    public static Matrix3f calculateNormalMatrix(Matrix4f m4, Matrix3f m3) {
        m3.m00 = m4.m00;
        m3.m01 = m4.m01;
        m3.m02 = m4.m02;
        m3.m10 = m4.m10;
        m3.m11 = m4.m11;
        m3.m12 = m4.m12;
        m3.m20 = m4.m20;
        m3.m21 = m4.m21;
        m3.m22 = m4.m22;
        // Matrix3f.invert(m3, m3);
        // Matrix3f.transpose(m3, m3);
        return m3;
    }

    public static float distance(Vector3f a, Vector3f b) {
        return (float)Math.sqrt(
                  (a.x - b.x) * (a.x - b.x)
                + (a.y - b.y) * (a.y - b.y)
                + (a.z - b.z) * (a.z - b.z)
                );
    }

    Vector3f getForwardVector(Quaternion q) {
        return new Vector3f( 2 * (q.x * q.z + q.w * q.y),
                             2 * (q.y * q.x - q.w * q.x),
                             1 - 2 * (q.x * q.x + q.y * q.y));
    }

    Vector3f getUpVector(Quaternion q) {
        return new Vector3f( 2 * (q.x * q.y - q.w * q.z),
                             1 - 2 * (q.x * q.x + q.z * q.z),
                             2 * (q.y * q.z + q.w * q.x));
    }

    Vector3f getRightVector(Quaternion q) {
        return new Vector3f( 1 - 2 * (q.y * q.y + q.z * q.z),
                             2 * (q.x * q.y + q.w * q.z),
                             2 * (q.x * q.z - q.w * q.y));
    }

}
