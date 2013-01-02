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
public class Icosphere extends LazyRenderable {

    private final Vector3f translation = new Vector3f(0, 0, 0);
    private final Quaternion rotation = new Quaternion();
    private final float length = 1;
    private int lod = 0;

    private static final float h = SimpleMath.sqrt(2f/3f);
	private final List<Vector3f> vertices
		= new ArrayList<Vector3f>(Arrays.<Vector3f> asList(new Vector3f[] {
			new Vector3f(+0.0f, +h/2f, +0f),
			new Vector3f(-0.5f, -h/2f, +h/2f),
			new Vector3f(+0.5f, -h/2f, +h/2f),
			new Vector3f(+0.0f, -h/2f, -h/2f),
		}));

	private Byte[] indices = new Byte[] {
			0, 1, 2,
			0, 2, 3,
			0, 3, 1,
			3, 2, 1,
	};

	private final ReadableColor color = Color.BLUE;


	public Icosphere() {
		this.lod = 0;
	}


	public Icosphere(final int lod) {
		this.lod = lod;
	}

	private void splitPlanes(final int iterations) {
		for (int i = 0; i < iterations; i +=3) {
			splitPlanes();
		}
	}

	private void splitPlanes() {
		Byte[] indices2 = new Byte[indices.length * 4];
		for (int i = 0; i < indices.length; i +=3) {
			Vector3f v1 = vertices.get(indices[i + 0]);
			Vector3f v2 = vertices.get(indices[i + 1]);
			Vector3f v3 = vertices.get(indices[i + 2]);

			Vector3f n1 = splitLine(v1,v2);
			Vector3f n2 = splitLine(v2,v3);
			Vector3f n3 = splitLine(v3,v1);

			int s1 = vertices.size();
			vertices.add(n1);  // s1
			vertices.add(n2);  // s1 + 1
			vertices.add(n3);  // s1 + 2

			int j = i/3 * 12;
			indices2[j + 0] = indices[i + 0];
			indices2[j + 1] = (byte) (s1 + 0);
			indices2[j + 2] = (byte) (s1 + 2);

			indices2[j + 3] = (byte) (s1 + 0);
			indices2[j + 4] = indices[i + 1];
			indices2[j + 5] = (byte) (s1 + 1);

			indices2[j + 6] = (byte) (s1 + 2);
			indices2[j + 7] = (byte) (s1 + 1);
			indices2[j + 8] = indices[i + 2];

			indices2[j + 9] = (byte) (s1 + 0);
			indices2[j +10] = (byte) (s1 + 1);
			indices2[j +11] = (byte) (s1 + 2);
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
		return builder.build(renderer);
	}

}
