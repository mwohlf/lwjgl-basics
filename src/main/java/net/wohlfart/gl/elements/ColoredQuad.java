package net.wohlfart.gl.elements;

import net.wohlfart.gl.shader.mesh.ColoredMeshBuilder;
import net.wohlfart.gl.shader.mesh.IMesh;

public class ColoredQuad extends RenderableImpl {

	@Override
	protected IMesh setupMesh() {
		ColoredMeshBuilder builder = new ColoredMeshBuilder();
		return builder.build();
	}

}
