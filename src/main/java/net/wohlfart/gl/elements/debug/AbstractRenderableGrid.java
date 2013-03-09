package net.wohlfart.gl.elements.debug;

import net.wohlfart.gl.elements.AbstractRenderable;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

/**
 * <p>Abstract AbstractRenderableGrid class.</p>
 *
 *
 *
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
    public AbstractRenderableGrid color(ReadableColor color) {
        this.color = color;
        resetMeshData();
        return this;
    }

    /**
     * <p>lineWidth.</p>
     *
     * @param lineWidth a float.
     * @return a {@link net.wohlfart.gl.elements.debug.AbstractRenderableGrid} object.
     */
    public AbstractRenderableGrid lineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        meshData = null;
        return this;
    }

}
