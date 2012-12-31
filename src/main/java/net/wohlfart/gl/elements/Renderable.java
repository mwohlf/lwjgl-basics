package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderer;


public interface Renderable {

	public abstract void render(final Renderer renderer);

	public abstract void dispose();


}
