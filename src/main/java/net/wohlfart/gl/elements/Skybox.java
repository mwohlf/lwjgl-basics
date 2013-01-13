package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.IMesh;

public class Skybox implements Renderable {


	IMesh side;

	@Override
	public void render(final Renderer renderer) {
		if (side == null) {
			side = SkyboxSide.MINUS_X.build(renderer, 100, 100);
		}
		side.draw();
	}

	@Override
	public void dispose() {

	}

}
