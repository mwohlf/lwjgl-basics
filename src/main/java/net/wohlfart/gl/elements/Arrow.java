package net.wohlfart.gl.elements;

import java.util.Arrays;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.ByteLines;
import net.wohlfart.gl.shader.mesh.IMeshData;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public class Arrow extends LazyRenderable {

	// vertices[0] is the direction of the arrow
	private final Vector3f[] vertices = new Vector3f[] {
			new Vector3f(+0.00f, +0.00f, +1.00f), // tip is in z direction <-- end
			new Vector3f(+0.00f, +0.00f, +0.00f), // base <-- start
			new Vector3f(+0.02f, +0.02f, +0.90f), // tip right
			new Vector3f(-0.02f, +0.02f, +0.90f), // tip left
			new Vector3f(-0.02f, -0.02f, +0.90f), // tip top
			new Vector3f(+0.02f, -0.02f, +0.90f), // tip bottom
	};

	private final Byte[] indices = new Byte[] {
			1, 0, // shaft
			2, 0, // tip1
			3, 0, // tip2
			4, 0, // tip3
			5, 0, // tip4
	};


	private final Vector3f translation = new Vector3f(0, 0, 0);

	private final Quaternion rotation = new Quaternion();

	private ReadableColor color;


	public Arrow() {

	}

	public Arrow(final Vector3f tip) {
		SimpleMath.createQuaternion(vertices[0], tip, rotation);
		this.color = ReadableColor.BLUE;
	}

	public Arrow(final Vector3f tip, final ReadableColor color) {
		SimpleMath.createQuaternion(vertices[0], tip, rotation);
		this.color = color;
	}

	@Override
	protected IMeshData setupMesh(final Renderer renderer) {
		WireframeMeshBuilder builder = new WireframeMeshBuilder();
		builder.setVertices(Arrays.<Vector3f> asList(vertices));
		builder.setIndices(new ByteLines(indices));
		builder.setColor(color);
		builder.setRotation(rotation);
		builder.setTranslation(translation);
		return builder.build(renderer);
	}

}
