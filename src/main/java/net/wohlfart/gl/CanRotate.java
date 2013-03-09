package net.wohlfart.gl;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>CanRotate interface.</p>
 *
 *
 *
 */
public interface CanRotate {

    /**
     * <p>rotate.</p>
     *
     * @param angle a float.
     * @param axis a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public abstract void rotate(final float angle, final Vector3f axis);

    /**
     * <p>getRotation.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    public abstract Quaternion getRotation();

    /**
     * <p>getRght.</p>
     *
     * @param vector3f a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public abstract Vector3f getRght(final Vector3f vector3f);

    /**
     * <p>getUp.</p>
     *
     * @param result a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public abstract Vector3f getUp(final Vector3f result);

    /**
     * <p>getDir.</p>
     *
     * @param result a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public abstract Vector3f getDir(final Vector3f result);

}
