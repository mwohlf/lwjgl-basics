package net.wohlfart.gl.elements;

import java.util.Arrays;

import net.wohlfart.gl.shader.IShader;
import net.wohlfart.tools.Pool.IPoolable;
import net.wohlfart.tools.VertexArrayObject;
import net.wohlfart.tools.VertexArrayObject.Builder;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Vector3f;

public class Arrow implements IDrawable, IPoolable {

    private VertexArrayObject vao;


    public Arrow(final IShader shader) {
    	Builder builder = new VertexArrayObject.Builder();
    	builder.setVertices(Arrays.<Vector3f>asList(new Vector3f[] {
            new Vector3f(+0.00f, +0.00f, +1.00f), // tip is in z direction  <-- end
    	    new Vector3f(+0.00f, +0.00f, +0.00f), // base  <-- start
    	    new Vector3f(+0.02f, +0.02f, +0.90f), // tip right
    	    new Vector3f(-0.02f, +0.02f, +0.90f), // tip left
    	    new Vector3f(-0.02f, -0.02f, +0.90f), // tip top
    	    new Vector3f(+0.02f, -0.02f, +0.90f), // tip bottom
        }));
    	builder.setIndices(Arrays.<Byte>asList(new Byte[] {
            1, 0,  // shaft
            2, 0,  // tip1
            3, 0,  // tip2
            4, 0,  // tip3
            5, 0,  // tip4
        }));
    	builder.setColor(Arrays.<ReadableColor>asList(new ReadableColor[] {
            Color.BLUE,
            Color.RED,
            Color.BLUE,
            Color.BLUE,
            Color.BLUE,
            Color.BLUE,
        }));

    	builder.setShader(shader);
    	builder.setTranslation(new Vector3f(0, 0, -1));
    	vao = builder.build();
    }

	@Override
	public void draw() {
		vao.draw();
	}

	@Override
	public void reset() {
		vao.dispose();
	}

}
