package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.mesh.ColoredMeshBuilder;

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
        final ColoredMeshBuilder builder = new ColoredMeshBuilder();
        return builder.build();
    }

}
