package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * 0 /|\ / | \ / | \ / 3 \ / - - \ 1 --------- 2
 *
 *
 *
 */
public class TetrahedronMesh extends AbstractRenderableGrid {

    private float length = 1;

    private final Integer[] indices = new Integer[] { 0, 1, 1, 2, 2, 0, 0, 2, 2, 3, 3, 0, 0, 3, 3, 1, 1, 0, };

    /**
     * <p>Constructor for TetrahedronMesh.</p>
     */
    public TetrahedronMesh() {
    }

    /**
     * <p>Constructor for TetrahedronMesh.</p>
     *
     * @param length a float.
     */
    public TetrahedronMesh(float length) {
        this.length = length;
    }

    /**
     * <p>createVertices.</p>
     *
     * @return a {@link java.util.List} object.
     */
    protected List<Vector3f> createVertices() {
        final float h = +SimpleMath.sqrt(2f / 3f) * length;
        final List<Vector3f> result = new ArrayList<Vector3f>(4);
        result.add(new Vector3f(0, +h / 2f, 0));
        result.add(new Vector3f(-length / 2f, -h / 2f, +h / 2f));
        result.add(new Vector3f(+length / 2f, -h / 2f, +h / 2f));
        result.add(new Vector3f(0, -h / 2f, -h / 2f));
        return result;
    }

    /** {@inheritDoc} */
    @Override
    protected IMesh setupMesh() {
        final WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(createVertices());
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
