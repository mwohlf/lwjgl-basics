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



// see: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
public class IcosphereWire extends LazyRenderable {

    private final Vector3f translation = new Vector3f(0, 0, 0);
    private final Quaternion rotation = new Quaternion();
    private int lod = 0;

    private static final float h = SimpleMath.sqrt(2f/3f);
    /*
	private final List<Vector3f> vertices
		= new ArrayList<Vector3f>(Arrays.<Vector3f> asList(new Vector3f[] {
			new Vector3f(+0.0f, +h/2f, +0.0f),     // top
			new Vector3f(-0.5f, -h/2f, +h/2f),     // front left
			new Vector3f(+0.5f, -h/2f, +h/2f),     // front right
			new Vector3f(+0.0f, -h/2f, -h/2f),     // back
		}));
	*/
	private final List<Vector3f> vertices
	= new ArrayList<Vector3f>(Arrays.<Vector3f> asList(new Vector3f[] {
		new Vector3f(+1.0f,  0f, -1/SimpleMath.sqrt(2f)),     // top
		new Vector3f(-1.0f,  0f, -1/SimpleMath.sqrt(2f)),     // front left
		new Vector3f(   0f, +1f, +1/SimpleMath.sqrt(2f)),     // front right
		new Vector3f(   0f, -1f, +1/SimpleMath.sqrt(2f)),     // back
	}));

	private Byte[] indices = new Byte[] {
			0, 1, 1, 2, 2, 0,
			0, 2, 2, 3, 3, 0,
			0, 3, 3, 1, 1, 0,
			3, 2, 2, 1, 1, 3,
	};

	private final ReadableColor color = Color.BLUE;


	public IcosphereWire() {
		this.lod = 0;
		normalize();
	}


	public IcosphereWire(final int lod) {
		this.lod = lod;
		normalize();
	}

	private void normalize() {
		for (Vector3f vec : vertices) {
			vec.normalise();
		}
	}

	private void splitPlanes(final int iterations) {
		for (int i = 0; i < iterations; i++) {
			splitPlanes();
		}
	}

	private void splitPlanes() {
		// for each side we have 4 new smaller sides now
		Byte[] indices2 = new Byte[indices.length * 4];
		int indicesPerSide = 6;
		int newIndices = indicesPerSide * 4;

		for (int i = 0; i < indices.length; i += indicesPerSide) {  // 6 is the number of indices per side

			// get the start of the lines of a side
			Vector3f v1 = vertices.get(indices[i + 0]);  // top
			Vector3f v2 = vertices.get(indices[i + 2]);  // left
			Vector3f v3 = vertices.get(indices[i + 4]);  // right

			// find the midpoints
			Vector3f n1 = splitLine(v1, v2);  // mid-left
			Vector3f n2 = splitLine(v2, v3);  // mid-bottom
			Vector3f n3 = splitLine(v3, v1);  // mid-right

			n1.normalise();
			n2.normalise();
			n3.normalise();

			int offset = vertices.size();
			vertices.add(n1);  // offset + 0
			vertices.add(n2);  // offset + 1
			vertices.add(n3);  // offset + 2

			// top triangle
			int j = (i/indicesPerSide) * newIndices;
			indices2[j + 0] = indices[i + 0];        // top
			indices2[j + 1] = (byte) (offset + 0);
			indices2[j + 2] = (byte) (offset + 0);   // mid-left
			indices2[j + 3] = (byte) (offset + 2);
			indices2[j + 4] = (byte) (offset + 2);   // mid-right
			indices2[j + 5] = indices[i + 0];

			indices2[j + 6] = (byte) (offset + 0);   // mid-left
			indices2[j + 7] = indices[i + 2];
			indices2[j + 8] = indices[i + 2];        // left
			indices2[j + 9] = (byte) (offset + 1);
			indices2[j +10] = (byte) (offset + 1);   // mid-bottom
			indices2[j +11] = (byte) (offset + 0);

			indices2[j +12] = (byte) (offset + 2);   // mid-right
			indices2[j +13] = (byte) (offset + 1);
			indices2[j +14] = (byte) (offset + 1);   // mid-bottom
			indices2[j +15] = indices[i + 4];
			indices2[j +16] = indices[i + 4];        // right
			indices2[j +17] = (byte) (offset + 2);

			// center
			indices2[j +18] = (byte) (offset + 0);   // mid-left
			indices2[j +19] = (byte) (offset + 1);
			indices2[j +20] = (byte) (offset + 1);   // mid-bottom
			indices2[j +21] = (byte) (offset + 2);
			indices2[j +22] = (byte) (offset + 2);   // mid-right
			indices2[j +23] = (byte) (offset + 0);
		}
		indices = indices2;
	}



	private Vector3f splitLine(final Vector3f v1, final Vector3f v2) {
		return new Vector3f((v1.x + v2.x)/2f,
				            (v1.y + v2.y)/2f,
				            (v1.z + v2.z)/2f);
	}


	@Override
	protected IMeshData setupMesh(final Renderer renderer) {
		splitPlanes(lod);
		WireframeMeshBuilder builder = new WireframeMeshBuilder();
		builder.setVertices(vertices);
		builder.setIndices(new ByteLines(indices));
		builder.setColor(color);
		builder.setRotation(rotation);
		builder.setTranslation(translation);
		builder.setRenderer(renderer);
		builder.setLineWidth(1);
		return builder.build();
	}

}
