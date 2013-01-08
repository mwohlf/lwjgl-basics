package net.wohlfart.gl.elements.debug;

import java.util.Arrays;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Arrow extends RenderableGrid {

	// vertices[0] is the direction of the arrow
	private final Vector3f[] vertices = new Vector3f[] {
			new Vector3f(+0.00f, +0.00f, +1.00f), // tip is in z direction <-- end
			new Vector3f(+0.00f, +0.00f, +0.00f), // base <-- start
			new Vector3f(+0.02f, +0.02f, +0.90f), // tip right
			new Vector3f(-0.02f, +0.02f, +0.90f), // tip left
			new Vector3f(-0.02f, -0.02f, +0.90f), // tip top
			new Vector3f(+0.02f, -0.02f, +0.90f), // tip bottom
	};

	private final Integer[] indices = new Integer[] {
			1, 0, // shaft
			2, 0, // tip1
			3, 0, // tip2
			4, 0, // tip3
			5, 0, // tip4
	};


	public Arrow() {

	}

	public Arrow(final Vector3f tip) {
		SimpleMath.createQuaternion(vertices[0], tip, rotation);
	}

	@Override
	protected IMesh setupMesh(final Renderer renderer) {
		WireframeMeshBuilder builder = new WireframeMeshBuilder();
		builder.setVertices(Arrays.<Vector3f> asList(vertices));
		builder.setIndices(indices);
		builder.setIndicesStructure(GL11.GL_LINES);
		builder.setIndexElemSize(GL11.GL_UNSIGNED_INT);
		builder.setColor(color);
		builder.setLineWidth(lineWidth);
		builder.setRotation(rotation);
		builder.setTranslation(translation);
		builder.setRenderer(renderer);
		return builder.build();
	}

}
