package net.wohlfart.gl.elements.debug;

import net.wohlfart.gl.elements.RenderableImpl;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;


public abstract class RenderableGrid extends RenderableImpl {

	protected ReadableColor color = Color.BLUE;
	protected float lineWidth = 1;


	public RenderableGrid color(ReadableColor color) {
		this.color = color;
		resetMeshData();
		return this;
	}

	public RenderableGrid lineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		meshData = null;
		return this;
	}

}
