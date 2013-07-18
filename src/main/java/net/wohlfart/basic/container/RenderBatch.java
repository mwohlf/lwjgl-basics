package net.wohlfart.basic.container;

import net.wohlfart.basic.elements.IsRenderable;

import org.lwjgl.util.vector.Matrix4f;

public interface RenderBatch<T extends IsRenderable> extends IsRenderable { // REVIEWED

    /**
     * to initialize stuff
     */
    void setup();


    /**
     * Get the current projection matrix in use it defines the screen ratio
     * and field of view angle.
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    Matrix4f getProjectionMatrix();

    /**
     * Get the current modelView matrix, this matrix changes when the
     * point of view moves or the view direction rotates.
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    Matrix4f getModelViewMatrix();


}
