package net.wohlfart.gl.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.mesh.ByteLines;
import net.wohlfart.gl.shader.mesh.IMeshData;
import net.wohlfart.gl.shader.mesh.WireframeMeshBuilder;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
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
public class Cube extends LazyRenderable {

    private final Vector3f translation = new Vector3f(0, 0, 0);
    private final Quaternion rotation = new Quaternion();
    private float length = 1;

    public Cube() {

    }

    public Cube(final float length) {
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

    protected ByteLines createIndices() {
        List<Byte> result = new ArrayList<Byte>(6 * 2 * 3);
        result.addAll(createIndices((byte)0, (byte)1, (byte)2, (byte)3));
        result.addAll(createIndices((byte)4, (byte)0, (byte)3, (byte)7));
        result.addAll(createIndices((byte)5, (byte)4, (byte)7, (byte)6));
        result.addAll(createIndices((byte)1, (byte)5, (byte)6, (byte)2));
        result.addAll(createIndices((byte)0, (byte)4, (byte)5, (byte)1));
        result.addAll(createIndices((byte)3, (byte)2, (byte)6, (byte)7));
        return new ByteLines(result);
    }

    protected Collection<Byte> createIndices(byte i1, byte i2, byte i3, byte i4) {
    	List<Byte> result = new ArrayList<Byte>();
    	result.add(i1);
    	result.add(i2);
    	result.add(i3);
    	result.add(i3);
    	result.add(i4);
    	result.add(i1);
    	return result;
	}

	protected List<ReadableColor> createColors() {
        List<ReadableColor> result = new ArrayList<ReadableColor>(8);
        for (int i = 0; i < 8; i++) {
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
