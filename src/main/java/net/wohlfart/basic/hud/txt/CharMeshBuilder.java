package net.wohlfart.basic.hud.txt;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;
import net.wohlfart.basic.Settings;
import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.Vertex;
import net.wohlfart.gl.shader.mesh.AbstractMeshBuilder;
import net.wohlfart.gl.shader.mesh.CharacterMesh;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * this creates a mesh for a single character
 */
public class CharMeshBuilder extends AbstractMeshBuilder {

    private CharAtlas atlas;
    private CharInfo info;

    private float screenX;
    private float screenY;

    private final Vertex[] vertices = new Vertex[] {new Vertex(), new Vertex(), new Vertex(), new Vertex()};
    private final float[] verticesBuffer = new float[vertices.length * (3 + 2)];


    @Override
    public IsRenderable build() {
        final int vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        createVboHandle(createVertexStream());

        // OpenGL expects to draw vertices in counter clockwise order by default
        byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
        createIdxBufferHandle(indices);

        final int[] offset = {0};
        final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                         + ShaderAttributeHandle.TEXTURE_COORD.getByteCount()
                         ;
        ShaderAttributeHandle.POSITION.enable(stride, offset);
        ShaderAttributeHandle.TEXTURE_COORD.enable(stride, offset);
        ShaderAttributeHandle.NORMAL.disable();
        ShaderAttributeHandle.COLOR.disable();

        // done with the VAO
        GL30.glBindVertexArray(0);

        final int texId = atlas.getTexHandle();
        return new CharacterMesh(vaoHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indices.length, 0, texId);
    }

    protected float[] createVertexStream() {
        Settings settings = CONTEXT_HOLDER.getSettings();
        final float screenWidth = settings.getWidth();
        final float screenHeight = settings.getHeight();  // screen size in pixel 1200/700
        final float aspectRatio = screenWidth / screenHeight;    // > 1   e.g.1.7

        float alpha = SimpleMath.deg2rad(settings.getFieldOfView()) / 2f;   // 0.39
        final float yScale = 1f / SimpleMath.tan(alpha);          // 2.41
        final float xScale = yScale / aspectRatio;                // 1.40

        final float atlasWidth = atlas.getImage().getWidth();
        final float atlasHeight = atlas.getImage().getHeight();  // texture atlas size in pixel (512)

        float z = -settings.getNearPlane();

        // the x/y coordinates must fit into a [-0.5 .. +0.5] interval
        float x1 = ((screenX / screenWidth) / xScale ) * 2f;
        float y1 = ((screenY / screenHeight) / yScale ) * 2f;
        float x2 = (((screenX + info.getWidth()) / screenWidth) / xScale ) * 2f;
        float y2 = (((screenY - info.getHeight()) / screenHeight) / yScale ) * 2f;

        // the mesh coordinates are:
        //   x axis is left to right
        //   y axis is bottom to top

        // TODO: move the origin to the top left

        // texture coordinates are in the [0...1] interval
        final float s1 = info.getX() / atlasWidth;
        final float t1 = info.getY() / atlasHeight;
        final float s2 = (info.getX() + info.getWidth()) / atlasWidth;
        final float t2 = (info.getY() + info.getHeight()) / atlasHeight;

        // We'll define our quad using 4 vertices of the custom 'Vertex' class
        vertices[0].setXYZ(x1, y1, z).setST(s1, t1);
        vertices[1].setXYZ(x1, y2, z).setST(s1, t2);
        vertices[2].setXYZ(x2, y2, z).setST(s2, t2);
        vertices[3].setXYZ(x2, y1, z).setST(s2, t1);

        int i = 0;
        for (Vertex vertex : vertices) {
            verticesBuffer[i++] = vertex.getXYZ()[0];
            verticesBuffer[i++] = vertex.getXYZ()[1];
            verticesBuffer[i++] = vertex.getXYZ()[2];
            verticesBuffer[i++] = vertex.getST()[0];
            verticesBuffer[i++] = vertex.getST()[1];
        }

        return verticesBuffer;
    }


    public CharMeshBuilder setCharInfo(CharInfo info) {
        this.info = info;
        return this;
    }

    public CharMeshBuilder setCharAtlas(CharAtlas atlas) {
        this.atlas = atlas;
        return this;
    }

    public CharMeshBuilder setScreenX(float x) {
        this.screenX = x;
        return this;
    }

    public CharMeshBuilder setScreenY(float y) {
        this.screenY = y;
        return this;
    }

}
