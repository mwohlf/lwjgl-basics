package net.wohlfart.gl.spatial;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.AbstractRenderable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Model extends AbstractRenderable {
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
            tmp.set(initialTranslation);
            tmp = Vector3f.sub(vec, tmp, tmp);
            result = Math.max(result, tmp.length());
        }
        return result;
    }

    @Override
    protected IsRenderable setupMesh() {
        radius = calculateRadius();

        final ModelMesh.Builder builder = new ModelMesh.Builder();
        builder.setIndices(getIndices());
        builder.setVertexStream(createVertexStream());
        builder.setTrianglePrimitive(GL11.GL_TRIANGLES);
        return builder.build();
    }

    protected int[] getIndices() {
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
     * 3 position coords 3 normal coords 2 texture coords
     *
     * @return
     */
    protected float[] createVertexStream() {
        int index = 0;
        final float[] result = new float[attrIdices.size() * (3 + 3 + 2)];  // @formatter:on
        for (final VertexAttr vertexAttr : attrIdices) {
            result[index++] = positions.get(vertexAttr.positionIdx).x;
            result[index++] = positions.get(vertexAttr.positionIdx).y;
            result[index++] = positions.get(vertexAttr.positionIdx).z;
            result[index++] = normals.get(vertexAttr.normalIdx).x;
            result[index++] = normals.get(vertexAttr.normalIdx).y;
            result[index++] = normals.get(vertexAttr.normalIdx).z;
            result[index++] = textureCoords.get(vertexAttr.textureCoordIdx).x;
            result[index++] = textureCoords.get(vertexAttr.textureCoordIdx).y;
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

    public void addVertexForStream(final int position, final int textureCoords, final int normal) { // @formatter:off
        attrIdices.add(new VertexAttr() { {
                this.positionIdx = position;
                this.textureCoordIdx = textureCoords;
                this.normalIdx = normal;
            }});  // @formatter:on
    }

    protected List<Vector3f> getPositions() {
        return positions;
    }

    protected List<Vector3f> getNormals() {
        return normals;
    }

    protected List<Vector2f> getTextureCoords() {
        return textureCoords;
    }

    protected List<VertexAttr> getAttrIndices() {
        return attrIdices;
    }


    @Override
    public String toString() {
        return "Model: '" + name + "'";
    }

}
