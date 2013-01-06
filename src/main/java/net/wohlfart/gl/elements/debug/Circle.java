package net.wohlfart.gl.elements.debug;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.IMeshData;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;


public class Circle extends RenderableWireMesh {

    private final int pieces = 15; // LOD
    private float radius = 1;


    public Circle() {}

    public Circle(float radius) {
    	this.radius = radius;
    }

    protected List<Vector3f> createVertices() {
        List<Vector3f> result = new ArrayList<Vector3f>(pieces);
        for (int i = 0; i < pieces; i++) {
        	float rad = (SimpleMath.TWO_PI * i) / pieces;
        	float x = SimpleMath.sin(rad) * radius;
        	float y = SimpleMath.cos(rad) * radius;
        	result.add(i, new Vector3f(x, y, 0));
        }
        return result;
    }

    protected Integer[] createIndices() {
        List<Integer> result = new ArrayList<Integer>(pieces * 2);
        for (int i = 0; i < pieces; i++) {
        	result.add(i * 2, i);
        	result.add(i * 2 + 1, ((i + 1)%pieces));
        }
        return result.toArray(new Integer[result.size()]);
    }

    @Override
    protected IMeshData setupMesh(Renderer renderer) {
        WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(createVertices());
        builder.setIndices(createIndices());
		builder.setIndicesStructure(GL11.GL_LINE_LOOP);  // loop!
		builder.setIndexElemSize(GL11.GL_UNSIGNED_INT);
		builder.setColor(color);
		builder.setLineWidth(lineWidth);
		builder.setRotation(rotation);
        builder.setTranslation(translation);
        builder.setRenderer(renderer);
        return builder.build();
    }

}
