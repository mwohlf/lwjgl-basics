package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.IMesh;

public class Skybox implements Renderable {


	IMesh[] sides;
	int size = 400;


	@Override
	public void render(final Renderer renderer) {
		if (sides == null) {
			sides = new IMesh[] {
					SkyboxSide.MINUS_X.build(renderer, size, size),
					SkyboxSide.PLUS_X.build(renderer, size, size),
					SkyboxSide.MINUS_Y.build(renderer, size, size),
					SkyboxSide.PLUS_Y.build(renderer, size, size),
					SkyboxSide.MINUS_Z.build(renderer, size, size),
					SkyboxSide.PLUS_Z.build(renderer, size, size),
			};
		}

		for (IMesh side : sides) {
			side.draw();
		}
	}

	@Override
	public void dispose() {

	}

}
