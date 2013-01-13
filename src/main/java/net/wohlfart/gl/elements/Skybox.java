package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.IMesh;

public class Skybox implements Renderable {


	IMesh[] sides;

	@Override
	public void render(final Renderer renderer) {
		if (sides == null) {
			sides = new IMesh[] {
					SkyboxSide.MINUS_X.build(renderer, 100, 100),
					SkyboxSide.PLUS_X.build(renderer, 100, 100),
					SkyboxSide.MINUS_Y.build(renderer, 100, 100),
					SkyboxSide.PLUS_Y.build(renderer, 100, 100),
					SkyboxSide.MINUS_Z.build(renderer, 100, 100),
					SkyboxSide.PLUS_Z.build(renderer, 100, 100),
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
