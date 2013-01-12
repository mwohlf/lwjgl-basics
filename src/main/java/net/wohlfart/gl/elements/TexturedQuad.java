package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.TexturedMeshBuilder;

public class TexturedQuad extends RenderableImpl {

	@Override
	protected IMesh setupMesh(Renderer renderer) {
		TexturedMeshBuilder builder = new TexturedMeshBuilder();
		builder.setRenderer(renderer);
		builder.setTranslation(translation);
		builder.setRotation(rotation);
		builder.setTextureFilename("/images/ash_uvgrid01.png");
		return builder.build();
	}

}
