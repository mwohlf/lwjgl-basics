package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

// see: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
public class TerahedronRefinedMesh extends RenderableGrid {

    private int lod = 0;
    private float radius = 1;

    private final List<Vector3f> vertices = new ArrayList<Vector3f>(Arrays.<Vector3f> asList(new Vector3f[] {
            new Vector3f(+1.0f, 0f, -1 / SimpleMath.sqrt(2f)), // top
            new Vector3f(-1.0f, 0f, -1 / SimpleMath.sqrt(2f)), // front left
            new Vector3f(0f, +1f, +1 / SimpleMath.sqrt(2f)), // front right
            new Vector3f(0f, -1f, +1 / SimpleMath.sqrt(2f)), // back
    }));

    private Integer[] indices = new Integer[] { 0, 1, 1, 2, 2, 0, 0, 2, 2, 3, 3, 0, 0, 3, 3, 1, 1, 0, 3, 2, 2, 1, 1, 3, };

    public TerahedronRefinedMesh() {
        this(0);
    }

    public TerahedronRefinedMesh(int lod) {
        this(lod, 1f);
    }

    public TerahedronRefinedMesh(int lod, float radius) {
        this.lod = lod;
        this.radius = radius;
        normalize();
    }

    private void normalize() {
        for (final Vector3f vec : vertices) {
            final float l = vec.length();
            vec.scale(radius / l);
        }
    }

    private void splitPlanes(final int iterations) {
        for (int i = 0; i < iterations; i++) {
            splitPlanes();
        }
    }

    private void splitPlanes() {
        // for each side we have 4 new smaller sides now
        final Integer[] indices2 = new Integer[indices.length * 4];
        final int indicesPerSide = 6;
        final int newIndices = indicesPerSide * 4;

        for (int i = 0; i < indices.length; i += indicesPerSide) { // 6 is the number of indices per side

            // get the start of the lines of a side
            final Vector3f v1 = vertices.get(indices[i + 0]); // top
            final Vector3f v2 = vertices.get(indices[i + 2]); // left
            final Vector3f v3 = vertices.get(indices[i + 4]); // right

            // find the midpoints
            final Vector3f n1 = splitLine(v1, v2); // mid-left
            final Vector3f n2 = splitLine(v2, v3); // mid-bottom
            final Vector3f n3 = splitLine(v3, v1); // mid-right

            // to keep them on the sphere
            n1.scale(radius / n1.length());
            n2.scale(radius / n2.length());
            n3.scale(radius / n3.length());

            final int offset = vertices.size();
            vertices.add(n1); // offset + 0
            vertices.add(n2); // offset + 1
            vertices.add(n3); // offset + 2

            // top triangle
            final int j = i / indicesPerSide * newIndices;
            indices2[j + 0] = indices[i + 0]; // top
            indices2[j + 1] = offset + 0;
            indices2[j + 2] = offset + 0; // mid-left
            indices2[j + 3] = offset + 2;
            indices2[j + 4] = offset + 2; // mid-right
            indices2[j + 5] = indices[i + 0];

            // left triangle
            indices2[j + 6] = offset + 0; // mid-left
            indices2[j + 7] = indices[i + 2];
            indices2[j + 8] = indices[i + 2]; // left
            indices2[j + 9] = offset + 1;
            indices2[j + 10] = offset + 1; // mid-bottom
            indices2[j + 11] = offset + 0;

            // right triangle
            indices2[j + 12] = offset + 2; // mid-right
            indices2[j + 13] = offset + 1;
            indices2[j + 14] = offset + 1; // mid-bottom
            indices2[j + 15] = indices[i + 4];
            indices2[j + 16] = indices[i + 4]; // right
            indices2[j + 17] = offset + 2;

            // center triangle
            indices2[j + 18] = offset + 0; // mid-left
            indices2[j + 19] = offset + 1;
            indices2[j + 20] = offset + 1; // mid-bottom
            indices2[j + 21] = offset + 2;
            indices2[j + 22] = offset + 2; // mid-right
            indices2[j + 23] = offset + 0;
        }
        indices = indices2;
    }

    private Vector3f splitLine(final Vector3f v1, final Vector3f v2) {
        return new Vector3f((v1.x + v2.x) / 2f, (v1.y + v2.y) / 2f, (v1.z + v2.z) / 2f);
    }

    @Override
    protected IMesh setupMesh() {
        splitPlanes(lod);
        final WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(vertices);
        builder.setIndices(indices);
        builder.setIndicesStructure(GL11.GL_LINES);
        builder.setIndexElemSize(GL11.GL_UNSIGNED_INT);
        builder.setColor(color);
        builder.setRotation(rotation);
        builder.setTranslation(translation);
        builder.setLineWidth(lineWidth);
        return builder.build();
    }

}
