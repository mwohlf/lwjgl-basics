package net.wohlfart.gl.antlr4;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.renderer.IsRenderable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Model extends AbstractRenderable {
    private final String name;

    private final List<Vector3f> positions = new ArrayList<Vector3f>();
    private final List<Vector3f> normals = new ArrayList<Vector3f>();
    private final List<Vector2f> textureCoords = new ArrayList<Vector2f>();

    private final List<VertexAttr> attrIdices = new ArrayList<VertexAttr>();


    public static class VertexAttr {
        int positionIdx;
        int normalIdx;
        int textureCoordIdx;
    }


    public Model(String name) {
        this.name = name;
    }


    @Override
    public IsRenderable setupMesh() {
        final ModelMeshBuilder builder = new ModelMeshBuilder();
        builder.setIndices(getIndices());
        builder.setVertexStream(createVertexStream());
        builder.setTrianglePrimitive(GL11.GL_TRIANGLES);
        return builder.build();
    }


    int[] getIndices() {
        return null;
    }

    /**
     * the vertex stream has the following format:
     *
     * 4 position coords
     * 4 normal coords
     * 2 texture coords
     *
     *
     * @return
     */

    float[] createVertexStream() {
        return null;
    }


    void addPosition(float x, float y, float z) {
        positions.add(new Vector3f(x,y,z));
    }

    void addNormal(float x, float y, float z) {
        normals.add(new Vector3f(x,y,z));
    }

    void addTextureCoord(float u, float v) {
        textureCoords.add(new Vector2f(u,v));
    }

    void addVertexForStream(final int position, final int textureCoords, final int normal) {
        attrIdices.add(new VertexAttr() {{
            positionIdx = position;
            textureCoordIdx = textureCoords;
            normalIdx = normal;
        }});
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
