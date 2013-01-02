package net.wohlfart.gl.elements;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.ByteLines;
import net.wohlfart.gl.shader.mesh.IMeshData;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;


public class Circle extends LazyRenderable {

    private final Vector3f translation = new Vector3f(0, 0, 0);
    private final Quaternion rotation = new Quaternion();
    private final int pieces = 15; // LOD
    private float radius = 1;
    private Vector3f normal = new Vector3f(0, 0, 1);



    public Circle() {

    }

    public Circle(float radius, Vector3f normal) {
    	this.radius = radius;
    	SimpleMath.createQuaternion(this.normal, normal, rotation);
    	this.normal = normal;
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

    protected ByteLines createIndices() {
        List<Byte> result = new ArrayList<Byte>(pieces * 2);
        for (byte i = 0; i < pieces; i++) {
        	result.add(i * 2, i);
        	result.add(i * 2 + 1, (byte)((i + 1)%pieces));
        }
        return new ByteLines(result);
    }

    protected List<ReadableColor> createColors() {
        List<ReadableColor> result = new ArrayList<ReadableColor>(pieces);
        for (int i = 0; i < pieces; i++) {
        	result.add(i, ReadableColor.BLUE);
        }
        return result;
    }

    @Override
    protected IMeshData setupMesh(Renderer renderer) {
        WireframeMeshBuilder builder = new WireframeMeshBuilder();
        builder.setVertices(createVertices());
        builder.setIndices(createIndices());
        builder.setColor(createColors());
        builder.setRotation(rotation);
        builder.setTranslation(translation);
        return builder.build(renderer);
    }

}
