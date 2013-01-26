package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.mesh.IMesh;

public class Skybox implements Renderable {

	IMesh[] sides;
	int size = 1024;


	public void init() {
		sides = new IMesh[] {
				SkyboxSide.MINUS_X.build(size, size),
				SkyboxSide.PLUS_X.build(size, size),
				SkyboxSide.MINUS_Y.build(size, size),
				SkyboxSide.PLUS_Y.build(size, size),
				SkyboxSide.MINUS_Z.build(size, size),
				SkyboxSide.PLUS_Z.build(size, size),
		};
	}

	@Override
	public void render() {
		if (sides == null) {
			init();
		}

		for (IMesh side : sides) {
			side.draw();
		}
	}

	@Override
	public void dispose() {

	}

}
