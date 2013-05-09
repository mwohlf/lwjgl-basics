package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.mesh.TexturedMeshBuilder;

/**
 * <p>TexturedQuad class.</p>
 */
public class TexturedQuad extends AbstractRenderable {

    @Override
    protected IsRenderable setupMesh() {
        final TexturedMeshBuilder builder = new TexturedMeshBuilder();
        builder.setTranslation(translation);
        builder.setRotation(rotation);
        builder.setTextureFilename("/gfx/images/ash_uvgrid01.png");
        return builder.build();
    }

}
