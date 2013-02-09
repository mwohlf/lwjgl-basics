package net.wohlfart.gl.elements;

import net.wohlfart.gl.shader.mesh.TexturedFragmentMesh;

import org.lwjgl.util.vector.Vector3f;

public class MeshWithNormal extends TexturedFragmentMesh implements HasNormal {

	private final Vector3f normal;

	public MeshWithNormal(int vaoHandle, int vboVerticesHandle,
			int vboIndicesHandle, int indicesType, int indexElemSize,
			int indicesCount, int indexOffset, int colorAttrib,
			int positionAttrib, int textureAttrib, int textureId,
			Vector3f normal) {
		super(vaoHandle, vboVerticesHandle, vboIndicesHandle, indicesType,
				indexElemSize, indicesCount, indexOffset, colorAttrib, positionAttrib,
				textureAttrib, textureId);
		this.normal = normal;
	}

	@Override
	public Vector3f getNormal() {
		return normal;
	}

}
