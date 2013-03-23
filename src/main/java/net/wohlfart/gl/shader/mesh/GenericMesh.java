package net.wohlfart.gl.shader.mesh;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>GenericMesh class.</p>
 */
public class GenericMesh implements IsRenderable {

    private final int vaoHandle;
    private final int vboVerticesHandle;
    private final int vboIndicesHandle;
    private final int lineWidth;
    private final int indicesType;
    private final int indexElemSize;
    private final int indicesCount;
    private final int indexOffset;
    private final ReadableColor color;

    // this is usually mapped with translation(vector) and rotation(quaternion)
    private final Matrix4f model2world = SimpleMath.convert(new Vector3f(0, 0,0), new Matrix4f());


    // only called by the builder
    GenericMesh(GenericMeshData builder) {
        vaoHandle = builder.getVaoHandle();
        vboVerticesHandle = builder.getVboVerticesHandle();
        vboIndicesHandle = builder.getVboIndicesHandle();
        lineWidth = builder.getLineWidth();
        indicesType = builder.getIndicesType();
        indexElemSize = builder.getIndexElemSize();
        indicesCount = builder.getIndicesCount();
        indexOffset = builder.getIndexOffset();
        color = builder.getColor();
    }



    /** {@inheritDoc} */
    @Override
    public void render() {
        final int colorAttrib = ShaderAttributeHandle.COLOR.getLocation();
        final int positionAttrib = ShaderAttributeHandle.POSITION.getLocation();

        ShaderUniformHandle.MODEL_TO_WORLD.set(model2world);

        GL30.glBindVertexArray(vaoHandle);
        // wire width
        GL11.glLineWidth(lineWidth);
        // color is not indexed
        GL20.glDisableVertexAttribArray(colorAttrib);
        GL20.glEnableVertexAttribArray(positionAttrib);

        GL20.glVertexAttrib4f(colorAttrib, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        // vertices are indexed
        GL11.glDrawElements(indicesType, indicesCount, indexElemSize, indexOffset);
        GL30.glBindVertexArray(0);
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);
        // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboVerticesHandle);
        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboIndicesHandle);
        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }

}
