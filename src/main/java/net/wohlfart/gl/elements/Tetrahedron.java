package net.wohlfart.gl.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.ByteLines;
import net.wohlfart.gl.shader.mesh.IMeshData;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
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
public class Tetrahedron extends LazyRenderable {

    private final Vector3f translation = new Vector3f(0, 0, 0);
    private final Quaternion rotation = new Quaternion();
    private float length = 1;

	private final Byte[] indices = new Byte[] {
			0, 1, 2,
			0, 2, 3,
			0, 3, 1,
	};

    public Tetrahedron() {

    }

    public Tetrahedron(final float length) {
    	this.length = length;
    }

	private final ReadableColor[] colors = new ReadableColor[] {
			Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE,};

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
	protected IMeshData setupMesh(final Renderer renderer) {
		WireframeMeshBuilder builder = new WireframeMeshBuilder();
		builder.setVertices(createVertices());
		builder.setIndices(new ByteLines(indices));
		builder.setColor(Arrays.<ReadableColor> asList(colors));
		builder.setRotation(rotation);
		builder.setTranslation(translation);
		return builder.build(renderer);
	}


}
