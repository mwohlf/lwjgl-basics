package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.mesh.IMesh;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;


public abstract class RenderableImpl implements Renderable {

	protected IMesh meshData;

	protected final Vector3f translation = new Vector3f();
	protected final Quaternion rotation = new Quaternion();

	protected abstract IMesh setupMesh();

	protected void resetMeshData() {
		meshData = null;
	}

	public RenderableImpl translate(Vector3f translation) {
		this.translation.set(translation);
		resetMeshData();
		return this;
	}

	public RenderableImpl rotate(Quaternion rotation) {
		this.rotation.set(rotation);
		resetMeshData();
		return this;
	}


	@Override
	public void render() {
		if (meshData == null) {
			meshData = setupMesh();
		}
		meshData.draw();
	}

	@Override
	public void dispose() {
		meshData.dispose();
		meshData = null;
	}

}
