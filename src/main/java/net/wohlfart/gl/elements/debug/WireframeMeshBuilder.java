package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.mesh.AbstractMeshBuilder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Vector3f;

/**
 * A Builder class for creating debug elements for a scene.
 */
public class WireframeMeshBuilder extends AbstractMeshBuilder { // REVIEWED

    private final List<Vector3f> vertices = new ArrayList<Vector3f>();
    private final List<Integer> indices = new ArrayList<Integer>();
    private int linePrimitive;

    private ReadableColor color = ReadableColor.GREY;
    @Override
    public IsRenderable build() {
        final int vaoHandle = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoHandle);

        applyRotationAndTranslation(vertices);

        createVboHandle(getVertices());

        int[] idc = getIndices();
        createIdxBufferHandle(idc);

        final int[] offset = {0};
        final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                ;
        ShaderAttributeHandle.POSITION.enable(stride, offset);
        ShaderAttributeHandle.COLOR.disable();
        ShaderAttributeHandle.NORMAL.disable();
        ShaderAttributeHandle.TEXTURE_COORD.disable();

        GL30.glBindVertexArray(0);

        return new WireframeMesh(vaoHandle, linePrimitive, GL11.GL_UNSIGNED_INT, idc.length, color);
    }

    private float[] getVertices() {
        int floatCount = ShaderAttributeHandle.POSITION.getFloatCount();
        final float[] result = new float[vertices.size() * floatCount];
        int i = 0;
        for (final Vector3f v : vertices) {
            result[i++] = v.x;
            result[i++] = v.y;
            result[i++] = v.z;
            if (floatCount >= 4) {
                result[i++] = 1f;
            }
        }
        return result;
    }

    private int[] getIndices() {
        final int[] result = new int[indices.size()];
        int i = 0;
        for (final Integer b : indices) {
            result[i++] = b;
        }
        return result;
    }

    public void setVertices(final List<Vector3f> vertices) {
        this.vertices.addAll(vertices);
    }

    public void setIndices(final List<Integer> indices) {
        this.indices.addAll(indices);
    }

    public void setColor(final ReadableColor color) {
        this.color = color;
    }

    public void setLinePrimitive(int linePrimitive) {
        this.linePrimitive = linePrimitive;
    }

}
