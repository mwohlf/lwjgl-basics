package net.wohlfart.gl.elements;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.mesh.TexturedMesh;

/**
 * <p>
 * TexturedQuad class.
 * </p>
 */
public class TexturedQuad extends AbstractRenderable {

    @Override
    protected IsRenderable setupMesh() {
        final TexturedMesh.Builder builder = new TexturedMesh.Builder();
        builder.setTranslation(translation);
        builder.setRotation(rotation);
        builder.setTextureFilename("/gfx/images/ash_uvgrid01.png");
        return builder.build();
    }

}
