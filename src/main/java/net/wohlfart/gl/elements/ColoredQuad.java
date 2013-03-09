package net.wohlfart.gl.elements;

import net.wohlfart.gl.shader.mesh.ColoredMeshBuilder;
import net.wohlfart.gl.shader.mesh.IMesh;

/**
 * <p>ColoredQuad class.</p>
 *
 *
 *
 */
public class ColoredQuad extends AbstractRenderable {

    /** {@inheritDoc} */
    @Override
    protected IMesh setupMesh() {
        final ColoredMeshBuilder builder = new ColoredMeshBuilder();
        return builder.build();
    }

}
