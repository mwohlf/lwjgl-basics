package net.wohlfart.gl.elements;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.mesh.ColoredMesh;

/**
 * ColoredQuad class.
 */
public class ColoredQuad extends AbstractRenderable {

    @Override
    protected IsRenderable setupMesh() {
        final ColoredMesh.Builder builder = new ColoredMesh.Builder();
        return builder.build();
    }

}
