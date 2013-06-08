package net.wohlfart.gl.elements.skybox;

import net.wohlfart.gl.elements.HasNormal;
import net.wohlfart.gl.shader.mesh.TexturedMesh;

import org.lwjgl.util.vector.Vector3f;


// todo: unify this with the texture mesh
class BoxSideMesh extends TexturedMesh implements HasNormal {

    private final Vector3f normal = new Vector3f();

    // @formatter:off
    BoxSideMesh(int vaoHandle,
                int indicesType,
                int indexElemSize,
                int indicesCount,
                int indexOffset,
                int textureId,
                Vector3f normal) {

        super(vaoHandle,
              indicesType,
              indexElemSize,
              indicesCount,
              indexOffset,
              textureId);
        this.normal.set(normal).normalise();
    } // @formatter:on

    @Override
    public Vector3f getNormal() {
        return normal;
    }

}
