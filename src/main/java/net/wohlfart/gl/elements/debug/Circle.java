package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>Circle class.</p>
 */
public class Circle extends AbstractRenderableGrid {

    private static final int pieces = 15; // LOD
    private float radius = 1;

    /**
     * <p>Constructor for Circle.</p>
     */
    public Circle() {
    }

    /**
     * <p>Constructor for Circle.</p>
     *
     * @param radius a float.
     */
    public Circle(float radius) {
        this.radius = radius;
    }

    /**
     * <p>createVertices.</p>
     *
     * @return a {@link java.util.List} object.
     */
    protected List<Vector3f> createVertices() {
        final List<Vector3f> result = new ArrayList<Vector3f>(pieces);
        for (int i = 0; i < pieces; i++) {
            final float rad = SimpleMath.TWO_PI * i / pieces;
            final float x = SimpleMath.sin(rad) * radius;
            final float y = SimpleMath.cos(rad) * radius;
            result.add(i, new Vector3f(x, y, 0));
        }
        return result;
    }

    /**
     * <p>createIndices.</p>
     *
     * @return an array of {@link java.lang.Integer} objects.
     */
    protected Integer[] createIndices() {
        final List<Integer> result = new ArrayList<Integer>(pieces * 2);
        for (int i = 0; i < pieces; i++) {
            result.add(i * 2, i);
            result.add(i * 2 + 1, (i + 1) % pieces);
        }
        return result.toArray(new Integer[result.size()]);
    }

    /** {@inheritDoc} */
    @Override
    protected IMesh setupMesh() {
        final WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(createVertices());
        builder.setIndices(createIndices());
        builder.setIndicesStructure(GL11.GL_LINE_LOOP); // loop!
        builder.setIndexElemSize(GL11.GL_UNSIGNED_INT);
        builder.setColor(color);
        builder.setLineWidth(lineWidth);
        builder.setRotation(rotation);
        builder.setTranslation(translation);
        return builder.build();
    }

}
