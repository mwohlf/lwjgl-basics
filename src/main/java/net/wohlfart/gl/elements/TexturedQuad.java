package net.wohlfart.gl.elements;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.mesh.TexturedMesh;

/**
 * TexturedQuad class.
 */
public class TexturedQuad extends AbstractRenderable {
    final TexturedMesh.Builder builder = new TexturedMesh.Builder();

    @Override
    protected IsRenderable setupMesh() {

        builder.setInitTranslation(initialTranslation);
        builder.setInitRotation(initialRotation);
        return builder.build();
    }

    public void setTexHandle(int texHandle) {
        builder.setTexHandle(texHandle);
    }

    public void setTexFilename(String texFilename) {
        builder.setTexFilename(texFilename);
    }

    public void setSize(float size) {
        builder.setSize(size);
    }

}
