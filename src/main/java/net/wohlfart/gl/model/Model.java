package net.wohlfart.gl.model;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.action.Action.Actor;
import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Model extends AbstractRenderable implements Actor, IsRenderable {
    // unique
    private final String name;

    private float radius;

    private final List<Vector3f> positions = new ArrayList<Vector3f>();
    private final List<Vector3f> normals = new ArrayList<Vector3f>();
    private final List<Vector2f> textureCoords = new ArrayList<Vector2f>();

    private final List<VertexAttr> attrIdices = new ArrayList<VertexAttr>();

    private static class VertexAttr {
        protected int positionIdx;
        protected int normalIdx;
        protected int textureCoordIdx;
    }

    public Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getRadius() {
        return radius;
    }


    protected float calculateRadius() {
        float result = 0;
        Vector3f tmp = new Vector3f();
        for (final Vector3f vec : positions) {
            tmp.set(translation);
            tmp = Vector3f.sub(vec, tmp, tmp);
            result = Math.max(result, tmp.length());
        }
        return result;
    }

    @Override
    protected IsRenderable setupMesh() {
        radius = calculateRadius();
        final ModelMeshBuilder builder = new ModelMeshBuilder();
        builder.setIndices(getIndices());
        builder.setVertexStream(createVertexStream());
        builder.setTrianglePrimitive(GL11.GL_TRIANGLES);
        return builder.build();
    }

    int[] getIndices() {
        // TODO: reuse vertices
        final int[] result = new int[attrIdices.size()];
        for (int index = 0; index < result.length; index++) {
            result[index] = index;
        }
        return result;
    }

    /**
     * the vertex stream has the following format:
     *
     * 4 position coords 4 normal coords 2 texture coords
     *
     *
     * @return
     */
    float[] createVertexStream() {
        // TODO: check if the size of the attributes fits in the array
        int index = 0;
        final float[] result = new float[attrIdices.size()
                * (ShaderAttributeHandle.POSITION.getFloatCount() + ShaderAttributeHandle.NORMAL.getFloatCount() + ShaderAttributeHandle.TEXTURE_COORD
                        .getFloatCount())];
        for (final VertexAttr vertexAttr : attrIdices) {
            result[index++] = positions.get(vertexAttr.positionIdx).x;
            result[index++] = positions.get(vertexAttr.positionIdx).y;
            result[index++] = positions.get(vertexAttr.positionIdx).z;
            result[index++] = 1;
            result[index++] = normals.get(vertexAttr.normalIdx).x;
            result[index++] = normals.get(vertexAttr.normalIdx).y;
            result[index++] = normals.get(vertexAttr.normalIdx).z;
            result[index++] = 0;
            result[index++] = textureCoords.get(vertexAttr.textureCoordIdx).x;
            result[index++] = textureCoords.get(vertexAttr.textureCoordIdx).y;
        }
        return result;
    }

    float[] createVertexPositionStream() {
        int index = 0;
        final float[] result = new float[attrIdices.size() * ShaderAttributeHandle.POSITION.getFloatCount()];
        for (final VertexAttr vertexAttr : attrIdices) {
            result[index++] = positions.get(vertexAttr.positionIdx).x;
            result[index++] = positions.get(vertexAttr.positionIdx).y;
            result[index++] = positions.get(vertexAttr.positionIdx).z;
            result[index++] = 1;
        }
        return result;
    }

    public void addPosition(float x, float y, float z) {
        positions.add(new Vector3f(x, y, z));
    }

    public void addNormal(float x, float y, float z) {
        normals.add(new Vector3f(x, y, z));
    }

    public void addTextureCoord(float u, float v) {
        textureCoords.add(new Vector2f(u, v));
    }

    public void addVertexForStream(final int position, final int textureCoords, final int normal) {
        attrIdices.add(new VertexAttr() {
            {
                this.positionIdx = position;
                this.textureCoordIdx = textureCoords;
                this.normalIdx = normal;
            }
        });
    }

    List<Vector3f> getPositions() {
        return positions;
    }

    List<Vector3f> getNormals() {
        return normals;
    }

    List<Vector2f> getTextureCoords() {
        return textureCoords;
    }

    List<VertexAttr> getAttrIndices() {
        return attrIdices;
    }


}
