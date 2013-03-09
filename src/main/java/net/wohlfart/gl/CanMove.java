package net.wohlfart.gl;

import org.lwjgl.util.vector.Vector3f;

/**
 * <p>CanMove interface.</p>
 *
 *
 *
 */
public interface CanMove {

    /**
     * <p>getPosition.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Vector3f getPosition();

    /**
     * <p>setPosition.</p>
     *
     * @param vector a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void setPosition(final Vector3f vector);

}
