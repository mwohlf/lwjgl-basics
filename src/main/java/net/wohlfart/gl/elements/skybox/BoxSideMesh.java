package net.wohlfart.gl.elements.skybox;

import net.wohlfart.gl.elements.HasNormal;
import net.wohlfart.gl.shader.mesh.TexturedMesh;

import org.lwjgl.util.vector.Vector3f;

class BoxSideMesh extends TexturedMesh implements HasNormal {

    private Vector3f normal = new Vector3f();

    // @formatter:off
    BoxSideMesh(int vaoHandle,
                int vboVerticesHandle,
                int vboIndicesHandle,
                int indicesType,
                int indexElemSize,
                int indicesCount,
                int indexOffset,
                int colorAttrib,
                int positionAttrib,
                int textureAttrib,
                int textureId,
                Vector3f normal) {

        super(vaoHandle,
              vboVerticesHandle,
              vboIndicesHandle,
              indicesType,
              indexElemSize,
              indicesCount,
              indexOffset,
              colorAttrib,
              positionAttrib,
              textureAttrib,
              textureId);
        this.normal = normal.normalise(this.normal);
    } // @formatter:on

    /** {@inheritDoc} */
    @Override
    public Vector3f getNormal() {
        return normal;
    }

}
