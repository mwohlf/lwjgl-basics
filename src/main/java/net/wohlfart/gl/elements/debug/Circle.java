package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * A simple Circle class.
 * </p>
 */
public class Circle extends AbstractRenderable {

    private int pieces = 15; // LOD
    private float radius = 1;

    /**
     * <p>
     * Constructor for Circle.
     * </p>
     */
    public Circle() {
    }

    /**
     * <p>
     * Constructor for Circle.
     * </p>
     * 
     * @param radius
     *            a float.
     */
    public Circle(float radius) {
        this.radius = radius;
    }

    /**
     * <p>
     * Constructor for Circle.
     * </p>
     * 
     * @param radius
     *            a float.
     * @param pieces
     *            the level of detail.
     */
    public Circle(float radius, int pieces) {
        this.radius = radius;
        this.pieces = pieces;
    }

    /** {@inheritDoc} */
    @Override
    protected IsRenderable setupMesh() {
        final WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(createVertices());
        builder.setIndices(createIndices());
        builder.setLinePrimitive(GL11.GL_LINES);
        builder.setColor(color);
        builder.setRotation(rotation);
        builder.setTranslation(translation);
        return builder.build();
    }

    /**
     * <p>
     * createVertices.
     * </p>
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
     * <p>
     * createIndices.
     * </p>
     * 
     * @return an array of {@link java.lang.Integer} objects.
     */
    protected List<Integer> createIndices() {
        final List<Integer> result = new ArrayList<Integer>(pieces * 2);
        for (int i = 0; i < pieces; i++) {
            result.add(i);
            result.add((i + 1) % pieces);
        }
        return result;
    }

}
