package net.wohlfart.gl.elements.debug;

import java.util.Arrays;

import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>Arrow class.</p>
 *
 *
 *
 */
public class Arrow extends AbstractRenderableGrid {

    // vertices[0] is the direction of the arrow
    private final Vector3f[] vertices = new Vector3f[] { new Vector3f(+0.00f, +0.00f, +1.00f), // tip is in z direction <-- end
            new Vector3f(+0.00f, +0.00f, +0.00f), // base <-- start
            new Vector3f(+0.02f, +0.02f, +0.90f), // tip right
            new Vector3f(-0.02f, +0.02f, +0.90f), // tip left
            new Vector3f(-0.02f, -0.02f, +0.90f), // tip top
            new Vector3f(+0.02f, -0.02f, +0.90f), // tip bottom
    };

    private final Integer[] indices = new Integer[] {
            1, 0, // shaft
            2, 0, // tip1
            3, 0, // tip2
            4, 0, // tip3
            5, 0, // tip4
    };


    /**
     * <p>createLink.</p>
     *
     * @param start a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param end a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link net.wohlfart.gl.elements.debug.Arrow} object.
     */
    public static Arrow createLink(Vector3f start, Vector3f end) {
        Arrow result = new Arrow(new Vector3f(end.x - start.x,
                                              end.y - start.y,
                                              end.z - start.z));
        result.translate(start);
        return result;
    }

    /**
     * <p>Constructor for Arrow.</p>
     */
    public Arrow() {

    }

    /**
     * <p>Constructor for Arrow.</p>
     *
     * @param tip a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Arrow(final Vector3f tip) {
        float length = tip.length();
        for (final Vector3f vec : vertices) {
            vec.z *= length;
        }
        SimpleMath.createQuaternion(vertices[0], tip, rotation);
    }

    /** {@inheritDoc} */
    @Override
    protected IMesh setupMesh() {
        final WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(Arrays.<Vector3f> asList(vertices));
        builder.setIndices(indices);
        builder.setIndicesStructure(GL11.GL_LINES);
        builder.setIndexElemSize(GL11.GL_UNSIGNED_INT);
        builder.setColor(color);
        builder.setLineWidth(lineWidth);
        builder.setRotation(rotation);
        builder.setTranslation(translation);
        return builder.build();
    }

}
