package net.wohlfart.gl.elements;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.mesh.TexturedMesh;

/**
 * TexturedQuad class.
 */
public class TexturedQuad extends AbstractRenderable {

    @Override
    protected IsRenderable setupMesh() {
        final TexturedMesh.Builder builder = new TexturedMesh.Builder();
        builder.setTranslation(initialTranslation);
        builder.setRotation(initialRotation);
        builder.setTextureFilename("/gfx/images/ash_uvgrid01.png");
        return builder.build();
    }

}
