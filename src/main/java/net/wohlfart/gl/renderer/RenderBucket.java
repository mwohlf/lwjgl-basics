package net.wohlfart.gl.renderer;

import java.util.HashSet;
import java.util.Set;


/**
 * a set of renderables that have common features like using the same shader/renderer
 */
public class RenderBucket implements Renderable {

	protected Set<Renderable> container = new HashSet<>();


	public void add(Renderable renderable) {
		container.add(renderable);
	}

	@Override
	public void render(Renderer renderer) {
		for (Renderable renderable : container) {
			renderable.render(renderer);
		}
	}

	@Override
	public void dispose() {
		for (Renderable renderable : container) {
			renderable.dispose();
		}
		container.clear();
	}

}
