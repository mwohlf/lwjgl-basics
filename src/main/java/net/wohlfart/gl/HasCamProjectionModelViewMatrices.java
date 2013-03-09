package net.wohlfart.gl;

import org.lwjgl.util.vector.Matrix4f;

/**
 * <p>HasCamProjectionModelViewMatrices interface.</p>
 *
 *
 *
 */
public interface HasCamProjectionModelViewMatrices {

    // current projection matrix in use (shouldn't change at all)
    /**
     * <p>getProjectionMatrix.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    public Matrix4f getProjectionMatrix();
    // current modelView matrix, changed when cam moves or rotates
    /**
     * <p>getModelViewMatrix.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    public Matrix4f getModelViewMatrix();


}
