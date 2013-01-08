package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.ColoredMeshBuilder;
import net.wohlfart.gl.shader.mesh.IMesh;

public class ColoredQuad extends RenderableImpl {

	@Override
	protected IMesh setupMesh(Renderer renderer) {
		ColoredMeshBuilder builder = new ColoredMeshBuilder();
		builder.setRenderer(renderer);
		return builder.build();
	}

}
