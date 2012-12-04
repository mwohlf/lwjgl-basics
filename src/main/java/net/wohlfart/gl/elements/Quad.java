package net.wohlfart.gl.elements;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.wohlfart.tools.Pool;
import net.wohlfart.tools.Pool.IPoolable;


// read: http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
// and: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
public class Quad implements IDrawable, IPoolable {

	private static final Pool<Quad> pool = new Pool<Quad>() {
        @Override
        protected Quad newObject () {
            return new Quad();
        }
    };

    /**
     * only created by the pool
     */
    private Quad() {

    }

    /**
     * @return a new object possibly from the pool
     */
    public static Quad create() {
    	return pool.obtain();
    }

    /**
     * free the object for reuse
     */
    public void free() {
    	pool.free(this);
    }

    /**
     * called by the pool when the object is freed
     */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}



	@Override
	public void draw() {
		/*
		// Bind to the VAO that has all the information about the quad vertices
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);

		// Draw the vertices
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

		// Put everything back to default (deselect)
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		*/
	}

}
