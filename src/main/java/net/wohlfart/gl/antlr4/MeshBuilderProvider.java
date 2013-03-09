package net.wohlfart.gl.antlr4;

import net.wohlfart.gl.shader.mesh.GenericMeshBuilder;

/**
 * <p>Creates MeshBuilders, usually from a file definition.</p>
 */
interface MeshBuilderProvider {

    /**
     * <p>Returns the MeshBilder that has been created by this provider.</p>
     *
     * @return a {@link net.wohlfart.gl.shader.mesh.GenericMeshBuilder} object.
     */
    GenericMeshBuilder getMeshBuilder();

}
