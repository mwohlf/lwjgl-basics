package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

// @formatter:off
/**
 *        0
 *       /|\
 *      / | \
 *     /  |  \
 *    /   3   \
 *   /  -   -  \
 *  1 --------- 2
 */
public class Tetrahedron extends AbstractRenderable { // @formatter:on  REVIEWED

    private float length = 1;

    private final List<Integer> indices = Arrays.asList(new Integer[] {// @formatter:off
            0, 1,  1, 2,  2, 0,
            0, 2,  2, 3,  3, 0,
            0, 3,  3, 1,  1, 0, });  // @formatter:on


    public Tetrahedron() {
        // nothing to do
    }

    public Tetrahedron(float length) {
        this.length = length;
    }

    @Override
    protected IsRenderable setupMesh() {
        final WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(createVertices());
        builder.setIndices(indices);
        builder.setLinePrimitive(GL11.GL_LINES);
        builder.setColor(color);
        builder.setRotation(initialRotation);
        builder.setTranslation(initialTranslation);
        return builder.build();
    }

    private List<Vector3f> createVertices() {
        final float h = +SimpleMath.sqrt(2f / 3f) * length;
        final List<Vector3f> result = new ArrayList<Vector3f>(4);
        result.add(new Vector3f(0, +h / 2f, 0));
        result.add(new Vector3f(-length / 2f, -h / 2f, +h / 2f));
        result.add(new Vector3f(+length / 2f, -h / 2f, +h / 2f));
        result.add(new Vector3f(0, -h / 2f, -h / 2f));
        return result;
    }

}
