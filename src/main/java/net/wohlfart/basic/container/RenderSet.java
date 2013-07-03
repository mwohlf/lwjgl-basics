package net.wohlfart.basic.container;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.IsUpdateable;

import org.lwjgl.util.vector.Matrix4f;

public interface RenderSet<T extends IsRenderable> extends IsRenderable, IsUpdateable { // REVIEWED

    /**
     * to initialize stuff
     */
    void setup();

    /**
     * Get the current modelView matrix, this matrix changes when the point of view moves or the view direction rotates.
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    Matrix4f getModelViewMatrix();

}
