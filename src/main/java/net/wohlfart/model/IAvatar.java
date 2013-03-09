package net.wohlfart.model;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>IAvatar interface.</p>
 */
public interface IAvatar {

    // moving and rotating
    /**
     * <p>translationForce.</p>
     *
     * @param x a float.
     * @param y a float.
     * @param z a float.
     */
    public void translationForce(float x, float y, float z);

    /**
     * <p>rotationForce.</p>
     *
     * @param xRot a float.
     * @param yRot a float.
     * @param zRot a float.
     */
    public void rotationForce(float xRot, float yRot, float zRot);

    // getting the direction and position
    /**
     * <p>getDirection.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Vector3f getDirection();

    /**
     * <p>getLeft.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Vector3f getLeft();

    /**
     * <p>getUp.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Vector3f getUp();

    /**
     * <p>getWorldTranslation.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Vector3f getWorldTranslation();

    // picking 2d/3d
    /**
     * <p>getWorldCoordinates.</p>
     *
     * @param vector2f a {@link org.lwjgl.util.vector.Vector2f} object.
     * @param f a float.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Vector3f getWorldCoordinates(Vector2f vector2f, float f);

    // acting
    /**
     * <p>clickAction.</p>
     *
     * @param vector2f a {@link org.lwjgl.util.vector.Vector2f} object.
     */
    public void clickAction(Vector2f vector2f);

}
