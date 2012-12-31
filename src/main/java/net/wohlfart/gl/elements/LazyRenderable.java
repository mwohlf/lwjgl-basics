package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.IMeshData;


public abstract class LazyRenderable implements Renderable {

	private IMeshData mesh;


	protected abstract IMeshData setupMesh(Renderer renderer);


	@Override
	public void render(final Renderer renderer) {
		if (mesh == null) {
			mesh = setupMesh(renderer);
		}
		mesh.draw();
	}

	@Override
	public void dispose() {
		mesh.dispose();
		mesh = null;
	}

}
