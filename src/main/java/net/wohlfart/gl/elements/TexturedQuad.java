package net.wohlfart.gl.elements;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.mesh.TexturedMesh;

/**
 * TexturedQuad class.
 */
public class TexturedQuad extends AbstractRenderable {

    private int texHandle = -1;
    private String texFilename = "/gfx/images/ash_uvgrid01.png";


    @Override
    protected IsRenderable setupMesh() {
        final TexturedMesh.Builder builder = new TexturedMesh.Builder();
        builder.setInitTranslation(initialTranslation);
        builder.setInitRotation(initialRotation);
        builder.setTexHandle(texHandle);
        builder.setTextureFilename(texFilename);
        return builder.build();
    }

    public void setTexHandle(int texHandle) {
        this.texHandle = texHandle;
    }

    public void setTexFilename(String texFilename) {
        this.texFilename = texFilename;
    }


}
