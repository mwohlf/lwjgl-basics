package net.wohlfart.gl.view;

import org.lwjgl.util.vector.Vector3f;

/**
 * <p>CanMove interface might be implemented
 * by anything that has a position and might be moved around.
 * This is mainly for a player position or a camera.</p>
 */
public interface CanMove {


    public void move(Vector3f vector);


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
    public void setPosition(Vector3f vector);

}
