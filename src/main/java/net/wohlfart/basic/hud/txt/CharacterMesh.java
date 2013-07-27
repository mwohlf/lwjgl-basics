package net.wohlfart.basic.hud.txt;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;
import net.wohlfart.basic.Settings;
import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.Vertex;
import net.wohlfart.gl.shader.mesh.AbstractMeshBuilder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;

/**
 * a single character somewhere on the screen, also a builder class to create
 */
public class CharacterMesh implements IsRenderable {

    private final int vaoHandle;
    private final int texHandle;

    private CharacterMesh(int vaoHandle, int texHandle) {
        this.vaoHandle = vaoHandle;
        this.texHandle = texHandle;
    }

    @Override
    public void render() {
        // Bind the texture
        GL30.glBindVertexArray(vaoHandle);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);   // FIXME: check if texture binding is stored in the VAO
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texHandle);
        GL11.glDrawElements(GL11.GL_TRIANGLES, // indices type
                6, // index elem count
                GL11.GL_UNSIGNED_BYTE,  // index elem size
                0);  // index offset
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }



    public static class Builder extends AbstractMeshBuilder {

        private CharAtlas atlas;
        private CharInfo info;
        private float screenX;
        private float screenY;
        private ReadableColor color;  // FIXME: color must be implemented in the shader


        private final Vertex[] vertices = new Vertex[] {new Vertex(), new Vertex(), new Vertex(), new Vertex()};
        private final float[] verticesBuffer = new float[vertices.length * (3 + 2 + 4)];
        // OpenGL expects to draw vertices in counter clockwise order by default
        private final byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };


        @Override
        public IsRenderable build() {
            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);
            createVboHandle(createVertexStream());
            createIdxBufferHandle(indices);

            final int[] offset = {0};
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                             + ShaderAttributeHandle.COLOR.getByteCount()
                             + ShaderAttributeHandle.TEXTURE_COORD.getByteCount()
                             ;
            ShaderAttributeHandle.POSITION.enable(stride, offset);
            ShaderAttributeHandle.COLOR.enable(stride, offset);
            ShaderAttributeHandle.TEXTURE_COORD.enable(stride, offset);
            ShaderAttributeHandle.NORMAL.disable();

            // done with the VAO
            GL30.glBindVertexArray(0);

            final int texId = atlas.getTexHandle();
            return new CharacterMesh(vaoHandle, texId);
        }

        protected float[] createVertexStream() {
            Settings settings = CONTEXT_HOLDER.getSettings();
            final float screenWidth = settings.getWidth();
            final float screenHeight = settings.getHeight();  // screen size in pixel 1200/700

            float z = 0f;       // [-1...1]

            // the x/y coordinates must fit into a [-1 .. +1] interval for the OpenGL screen space
            float x1 = ((screenX - (screenWidth/2))/ screenWidth ) * 2f;
            float y1 = (((screenHeight/2) - screenY)/ screenHeight) * 2f;
            float x2 = (((screenX - (screenWidth/2)) + info.getWidth()) / screenWidth) * 2f;
            float y2 = ((((screenHeight/2) - screenY) - info.getHeight()) / screenHeight) * 2f;

            final float atlasWidth = atlas.getImage().getWidth();
            final float atlasHeight = atlas.getImage().getHeight();  // texture atlas size in pixel (512)

            // texture coordinates are in the [0...1] interval
            final float s1 = info.getX() / atlasWidth;
            final float t1 = info.getY() / atlasHeight;
            final float s2 = (info.getX() + info.getWidth()) / atlasWidth;
            final float t2 = (info.getY() + info.getHeight()) / atlasHeight;

            // We'll define our quad using 4 vertices of the custom 'Vertex' class
            vertices[0].setXYZ(x1, y1, z).setST(s1, t1).setColor(color);
            vertices[1].setXYZ(x1, y2, z).setST(s1, t2).setColor(color);
            vertices[2].setXYZ(x2, y2, z).setST(s2, t2).setColor(color);
            vertices[3].setXYZ(x2, y1, z).setST(s2, t1).setColor(color);

            int i = 0;
            for (Vertex vertex : vertices) {
                verticesBuffer[i++] = vertex.getXYZ()[0];
                verticesBuffer[i++] = vertex.getXYZ()[1];
                verticesBuffer[i++] = vertex.getXYZ()[2];
                verticesBuffer[i++] = vertex.getRGBA()[0];
                verticesBuffer[i++] = vertex.getRGBA()[1];
                verticesBuffer[i++] = vertex.getRGBA()[2];
                verticesBuffer[i++] = vertex.getRGBA()[3];
                verticesBuffer[i++] = vertex.getST()[0];
                verticesBuffer[i++] = vertex.getST()[1];
            }

            return verticesBuffer;
        }


        public Builder setCharInfo(CharInfo info) {
            this.info = info;
            return this;
        }

        public Builder setCharAtlas(CharAtlas atlas) {
            this.atlas = atlas;
            return this;
        }

        public Builder setScreenX(float x) {
            this.screenX = x;
            return this;
        }

        public Builder setScreenY(float y) {
            this.screenY = y;
            return this;
        }

        public Builder setColor(ReadableColor color) {
            this.color = color;
            return this;
        }

    }
}
