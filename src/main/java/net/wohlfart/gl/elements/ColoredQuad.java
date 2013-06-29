package net.wohlfart.gl.elements;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.mesh.ColoredMesh;

/**
 * <p>
 * ColoredQuad class.
 * </p>
 *
 *
 *
 */
public class ColoredQuad extends AbstractRenderable {

    /** {@inheritDoc} */
    @Override
    protected IsRenderable setupMesh() {
        final ColoredMesh.Builder builder = new ColoredMesh.Builder();
        return builder.build();
    }

}
