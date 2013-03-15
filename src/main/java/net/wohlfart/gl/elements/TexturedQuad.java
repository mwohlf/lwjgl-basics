package net.wohlfart.gl.elements;

import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.TexturedMeshBuilder;

/**
 * <p>TexturedQuad class.</p>
 */
public class TexturedQuad extends AbstractRenderable {

    /** {@inheritDoc} */
    @Override
    protected IMesh setupMesh() {
        final TexturedMeshBuilder builder = new TexturedMeshBuilder();
        builder.setTranslation(translation);
        builder.setRotation(rotation);
        builder.setTextureFilename("/images/ash_uvgrid01.png");
        return builder.build();
    }

}
