package net.wohlfart.basic.container;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.IsUpdateable;

import org.lwjgl.util.vector.Matrix4f;

public interface RenderSet<T extends IsRenderable> extends IsRenderable, IsUpdateable {

    /**
     * to initialize stuff
     */
    void setup();

    /**
     * Get the current projection matrix in use (shouldn't change at all) it defines the screen ratio
     * and view angle.
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    Matrix4f getProjectionMatrix();

    /**
     * Get the current modelView matrix, this matrix changes when the point of view moves or the view direction rotates.
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    Matrix4f getModelViewMatrix();

}
