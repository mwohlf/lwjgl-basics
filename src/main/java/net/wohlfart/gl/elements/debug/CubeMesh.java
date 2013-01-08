package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;





/**
 *      5---------4
 *     /|        /|
 *    / |       / |
 *   1 ------- 0  |
 *   |  |      |  |
 *   |  6 - - -|- 7
 *   |/        |/
 *   2-------- 3
 */
public class CubeMesh extends RenderableGrid {


    private float length = 1;

    public CubeMesh() {}

    public CubeMesh(float length) {
    	this.length = length;
    }

    protected List<Vector3f> createVertices() {
    	float l = length/2f;
        List<Vector3f> result = new ArrayList<Vector3f>(8);
        result.add(new Vector3f(+l,+l,+l));
        result.add(new Vector3f(-l,+l,+l));
        result.add(new Vector3f(-l,-l,+l));
        result.add(new Vector3f(+l,-l,+l));
        result.add(new Vector3f(+l,+l,-l));
        result.add(new Vector3f(-l,+l,-l));
        result.add(new Vector3f(-l,-l,-l));
        result.add(new Vector3f(+l,-l,-l));
        return result;
    }

    protected Integer[] createIndices() {
        List<Integer> result = new ArrayList<Integer>(6 * 2 * 3);
        result.addAll(createIndices(0, 1, 2, 3));
        result.addAll(createIndices(4, 0, 3, 7));
        result.addAll(createIndices(5, 4, 7, 6));
        result.addAll(createIndices(1, 5, 6, 2));
        result.addAll(createIndices(0, 4, 5, 1));
        result.addAll(createIndices(3, 2, 6, 7));
        return result.toArray(new Integer[result.size()]);
    }

    protected Collection<Integer> createIndices(int i1, int i2, int i3, int i4) {
    	List<Integer> result = new ArrayList<Integer>();
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


    @Override
    protected IMesh setupMesh(Renderer renderer) {
        WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(createVertices());
        builder.setIndices(createIndices());
		builder.setIndicesStructure(GL11.GL_LINES);
		builder.setIndexElemSize(GL11.GL_UNSIGNED_INT);
        builder.setColor(color);
        builder.setLineWidth(lineWidth);
        builder.setRotation(rotation);
        builder.setTranslation(translation);
        builder.setRenderer(renderer);
        return builder.build();
    }

}
