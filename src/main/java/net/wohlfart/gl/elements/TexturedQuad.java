package net.wohlfart.gl.elements;

import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.TexturedMeshBuilder;

public class TexturedQuad extends RenderableImpl {

    @Override
    protected IMesh setupMesh() {
        final TexturedMeshBuilder builder = new TexturedMeshBuilder();
        builder.setTranslation(translation);
        builder.setRotation(rotation);
        builder.setTextureFilename("/images/ash_uvgrid01.png");
        return builder.build();
    }

}
