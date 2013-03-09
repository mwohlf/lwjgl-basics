package net.wohlfart.gl.view;

import org.lwjgl.util.vector.Matrix4f;

/**
 * <p>HasCamProjectionModelViewMatrices interface implemented by objects that
 * know where the current camera is at and in which direction the cam is looking</p>
 */
public interface HasCamProjectionModelViewMatrices {

    /**
     * <p>Get the current projection matrix in use (shouldn't change at all).</p>
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    public Matrix4f getProjectionMatrix();

    /**
     * <p>Get the current modelView matrix, this matrix changes when the point of view moves or the view direction rotates.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    public Matrix4f getModelViewMatrix();


}
