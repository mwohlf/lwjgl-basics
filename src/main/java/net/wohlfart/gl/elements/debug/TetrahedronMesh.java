package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;



/**
 *           0
 *          /|\
 *         / | \
 *        /  |  \
 *       /   3   \
 *      / -     - \
 *     1 --------- 2
 *
 */
public class TetrahedronMesh extends RenderableGrid {

    private float length = 1;

	private final Integer[] indices = new Integer[] {
			0, 1, 1, 2, 2, 0,
			0, 2, 2, 3, 3, 0,
			0, 3, 3, 1, 1, 0,
	};

    public TetrahedronMesh() {}

    public TetrahedronMesh(float length) {
    	this.length = length;
    }

    protected List<Vector3f> createVertices() {
    	float h = +SimpleMath.sqrt(2f/3f) * length;
        List<Vector3f> result = new ArrayList<Vector3f>(4);
        result.add(new Vector3f( 0, +h/2f, 0));
        result.add(new Vector3f(-length/2f, -h/2f, +h/2f));
        result.add(new Vector3f(+length/2f, -h/2f, +h/2f));
        result.add(new Vector3f( 0, -h/2f, -h/2f));
        return result;
    }

	@Override
	protected IMesh setupMesh() {
		WireframeMeshBuilder builder = new WireframeMeshBuilder();
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
