package net.wohlfart.gl.elements.debug;

import net.wohlfart.gl.elements.AbstractRenderable;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

public abstract class AbstractRenderableGrid extends AbstractRenderable {

    protected ReadableColor color = Color.BLUE;
    protected float lineWidth = 1;

    public AbstractRenderableGrid color(ReadableColor color) {
        this.color = color;
        resetMeshData();
        return this;
    }

    public AbstractRenderableGrid lineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        meshData = null;
        return this;
    }

}
