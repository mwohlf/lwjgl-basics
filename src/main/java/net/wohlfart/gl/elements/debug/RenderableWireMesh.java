package net.wohlfart.gl.elements.debug;

import net.wohlfart.gl.elements.Renderable;
import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.IMeshData;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;


public abstract class RenderableWireMesh implements Renderable {

	private IMeshData mesh;

	protected final Vector3f translation = new Vector3f();
	protected final Quaternion rotation = new Quaternion();
	protected ReadableColor color = Color.BLUE;
	protected float lineWidth = 1;


	protected abstract IMeshData setupMesh(Renderer renderer);

	public RenderableWireMesh translate(Vector3f translation) {
		this.translation.set(translation);
		mesh = null;
		return this;
	}

	public RenderableWireMesh rotate(Quaternion rotation) {
		this.rotation.set(rotation);
		mesh = null;
		return this;
	}

	public RenderableWireMesh color(ReadableColor color) {
		this.color = color;
		mesh = null;
		return this;
	}

	public RenderableWireMesh lineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		mesh = null;
		return this;
	}

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
