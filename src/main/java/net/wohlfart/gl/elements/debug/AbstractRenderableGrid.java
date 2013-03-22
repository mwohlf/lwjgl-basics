package net.wohlfart.gl.elements.debug;

import net.wohlfart.gl.elements.AbstractRenderable;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

/**
 * <p>The Abstract AbstractRenderableGrid class is the base of a set of wireframe mesh classes
 * that can be used to debug a scene.</p>
 */
public abstract class AbstractRenderableGrid extends AbstractRenderable {

    protected ReadableColor color = Color.BLUE;
    protected float lineWidth = 1;

    /**
     * <p>color.</p>
     *
     * @param color a {@link org.lwjgl.util.ReadableColor} object.
     * @return a {@link net.wohlfart.gl.elements.debug.AbstractRenderableGrid} object.
     */
    public AbstractRenderableGrid withColor(ReadableColor color) {
        this.color = color;
        destroyMeshData();
        return this;
    }

    /**
     * <p>lineWidth.</p>
     *
     * @param lineWidth a float.
     * @return a {@link net.wohlfart.gl.elements.debug.AbstractRenderableGrid} object.
     */
    public AbstractRenderableGrid withLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        destroyMeshData();
        return this;
    }

}
