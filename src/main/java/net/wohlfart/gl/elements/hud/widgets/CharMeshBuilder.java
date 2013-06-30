package net.wohlfart.gl.elements.hud.widgets;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.Vertex;
import net.wohlfart.gl.shader.mesh.CharacterMesh;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this creates a mesh for a single character
 */
class CharMeshBuilder {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CharMeshBuilder.class);

    private CharAtlas atlas;
    private CharInfo info;

    private float screenX;
    private float screenY;

    public IsRenderable build() {
        final GraphicContextManager cxtManager = GraphicContextManager.INSTANCE;
        final float screenWidth = cxtManager.getScreenWidth();
        final float screenHeight = cxtManager.getScreenHeight();  // screen size in pixel 1200/700
        final float aspectRatio = screenWidth / screenHeight;    // > 1   e.g.1.7

        float alpha = SimpleMath.deg2rad(cxtManager.getFieldOfView()) / 2f;   // 0.39
        final float yScale = 1f / SimpleMath.tan(alpha);          // 2.41
        final float xScale = yScale / aspectRatio;                // 1.40

        final float atlasWidth = atlas.getImage().getWidth();
        final float atlasHeight = atlas.getImage().getHeight();  // texture atlas size in pixel (512)

        float z = cxtManager.getNearPlane();

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
        final Vertex v0 = new Vertex();
        v0.setXYZ(x1, y1, -z);
        v0.setST(s1, t1);

        final Vertex v1 = new Vertex();
        v1.setXYZ(x1, y2, -z);
        v1.setST(s1, t2);

        final Vertex v2 = new Vertex();
        v2.setXYZ(x2, y2, -z);
        v2.setST(s2, t2);

        final Vertex v3 = new Vertex();
        v3.setXYZ(x2, y1, -z);
        v3.setST(s2, t1);

        final Vertex[] vertices = new Vertex[] { v0, v1, v2, v3 };
        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * (3 + 2));
        for (int i = 0; i < vertices.length; i++) {
            verticesBuffer.put(vertices[i].getXYZ());
            verticesBuffer.put(vertices[i].getST());
        }
        verticesBuffer.flip();

        // OpenGL expects to draw vertices in counter clockwise order by default
        final byte[] indices = { 0, 1, 2, 2, 3, 0 };
        final int indicesCount = indices.length;
        final ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        // Create a new Vertex Array Object in memory and select it (bind)
        final int vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        final int vboVerticesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVerticesHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        final int vboIndicesHandle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndicesHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

        int offset;
        final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                         + ShaderAttributeHandle.TEXTURE_COORD.getByteCount();

        offset = 0;

        ShaderAttributeHandle.POSITION.enable();
        GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(),
                Vertex.POSITION_ELEM_COUNT, GL11.GL_FLOAT, false, stride, offset);
        offset += ShaderAttributeHandle.POSITION.getByteCount();

        ShaderAttributeHandle.TEXTURE_COORD.enable();
        GL20.glVertexAttribPointer(ShaderAttributeHandle.TEXTURE_COORD.getLocation(),
                Vertex.TEXTURE_ELEM_COUNT, GL11.GL_FLOAT, false, stride, offset);
        offset += ShaderAttributeHandle.TEXTURE_COORD.getByteCount();

        ShaderAttributeHandle.NORMAL.disable();

        ShaderAttributeHandle.COLOR.disable();

        // done with the VAO
        GL30.glBindVertexArray(0);

        final int texId = atlas.getTextureId();
        return new CharacterMesh(vaoHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indicesCount, 0, texId);
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
