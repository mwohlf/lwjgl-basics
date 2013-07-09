package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.AbstractRenderable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

// @formatter:off
/**
 *
 *         5---------4
 *        /|        /|
 *       / |       / |
 *      1 ------- 0  |
 *      |  |      |  |
 *      |  6 - - -|- 7
 *      | /       | /
 *      |/        |/
 *      2-------- 3
 *
 *        A simple Cube class.
 */
public class Cube extends AbstractRenderable { // @formatter:on REVIEWED

    private float length = 1;


    public Cube() {
        // nothing to do
    }

    public Cube(float length) {
        this.length = length;
    }

    @Override
    protected IsRenderable setupMesh() {
        final WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(createVertices());
        builder.setIndices(createIndices());
        builder.setLinePrimitive(GL11.GL_LINES);
        builder.setColor(color);
        builder.setRotation(initialRotation);
        builder.setTranslation(initialTranslation);
        return builder.build();
    }

    private List<Vector3f> createVertices() {
        final float l = length / 2f;
        final List<Vector3f> result = new ArrayList<Vector3f>(8);
        result.add(new Vector3f(+l, +l, +l));
        result.add(new Vector3f(-l, +l, +l));
        result.add(new Vector3f(-l, -l, +l));
        result.add(new Vector3f(+l, -l, +l));
        result.add(new Vector3f(+l, +l, -l));
        result.add(new Vector3f(-l, +l, -l));
        result.add(new Vector3f(-l, -l, -l));
        result.add(new Vector3f(+l, -l, -l));
        return result;
    }

    private List<Integer> createIndices() {
        final List<Integer> result = new ArrayList<Integer>(6 * 2 * 3);
        result.addAll(createIndices(0, 1, 2, 3));
        result.addAll(createIndices(4, 0, 3, 7));
        result.addAll(createIndices(5, 4, 7, 6));
        result.addAll(createIndices(1, 5, 6, 2));
        result.addAll(createIndices(0, 4, 5, 1));
        result.addAll(createIndices(3, 2, 6, 7));
        return result;
    }

    private Collection<Integer> createIndices(int i1, int i2, int i3, int i4) {
        final List<Integer> result = new ArrayList<Integer>();
        result.add(i1);
        result.add(i2);

        result.add(i2);
        result.add(i3);

        result.add(i3);
        result.add(i4);

        result.add(i4);
        result.add(i1);
        return result;
    }

}
