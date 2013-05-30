package net.wohlfart.gl.view;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * CanRotate interface.
 * </p>
 */
public interface CanRotate {

    /**
     * <p>
     * rotate.
     * </p>
     * 
     * @param deltaAngle
     *            a float.
     * @param axis
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public abstract void rotate(float deltaAngle, Vector3f axis);

    /**
     * <p>
     * getRotation.
     * </p>
     * 
     * @return a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    public abstract Quaternion getRotation();

    public abstract void setRotation(Quaternion quaternion);

    /**
     * <p>
     * getRght.
     * </p>
     * 
     * @param vector3f
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public abstract Vector3f getRght(Vector3f vector3f);

    /**
     * <p>
     * getUp.
     * </p>
     * 
     * @param result
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public abstract Vector3f getUp(Vector3f result);

    /**
     * <p>
     * getDir.
     * </p>
     * 
     * @param result
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public abstract Vector3f getForward(Vector3f result);

}
