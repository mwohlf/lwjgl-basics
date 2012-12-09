package net.wohlfart.gl.elements;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.wohlfart.gl.shader.IShader;
import net.wohlfart.tools.Pool;
import net.wohlfart.tools.Pool.IPoolable;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;


// read: http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
// and: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
// and: http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2005.html
public class Quad implements IDrawable, IPoolable {
    private static final int VECTOR_SIZE = 4;

    private static final Pool<Quad> pool = new Pool<Quad>() {
        @Override
        protected Quad newObject () {
            return new Quad();
        }
    };

    private IShader shader;
    private int vaoHandle;
    private int vboHandle;

    private int vertexCount;
    private int indicesCount;

    /**
     * only called/created by the pool
     */
    private Quad() {}

    /**
     * @return a new object possibly from the pool
     */
    public static Quad create(final IShader shader) {
        return create( shader,
                       new Vector4f(+0.5f,+0.5f,+0.0f,+1.0f),
                       new Vector4f(-0.5f,+0.5f,+0.0f,+1.0f),
                       new Vector4f(-0.5f,-0.5f,+0.0f,+1.0f),
                       new Vector4f(+0.5f,-0.5f,+0.0f,+1.0f));
    }

    public static Quad create(final IShader shader,
                              final Vector4f tr,
                              final Vector4f tl,
                              final Vector4f bl,
                              final Vector4f br) {
        Quad result = pool.obtain();

        result.shader = shader;

        float[] vertices = new float[] {
                tr.x, tr.y, tr.z, tr.w,
                tl.x, tl.y, tl.z, tr.w,
                bl.x, bl.y, bl.z, tr.w,
                br.x, br.y, br.z, tr.w};
        result.vertexCount = 4;

        byte[] indices = {
                // top left
                0, 1, 2,
                // bottom right
                2, 3, 0};
        result.indicesCount = indices.length;

        // create a VAO in memory
        result.vaoHandle = GL30.glGenVertexArrays();
        // select/bind the VAO
        GL30.glBindVertexArray(result.vaoHandle);

        // create a new VBO for the vertices
        result.vboHandle = GL15.glGenBuffers();
        // and select it
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, result.vboHandle);
        // sending data to OpenGL requires the usage of (flipped) byte buffers
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(result.vertexCount * VECTOR_SIZE);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();
        // push the data to the VBO
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);		// Deselect (bind to 0) the VBO

        // create a new VBO for the indices
        result.vboHandle = GL15.glGenBuffers();
        // and select it
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, result.vboHandle);
        // setup the indices buffer
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(result.indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        // push the data to the VBO
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(shader.getInPosition());
        // assign vertex VBO to slot 0 of the VAO
        GL20.glVertexAttribPointer(shader.getInPosition(), VECTOR_SIZE, GL11.GL_FLOAT, false, 0, 0);

        GL30.glBindVertexArray(0);

        return result;
    }





    @Override
    public void draw() {
        // bind to the VAO that has all the information about the vertices
        GL30.glBindVertexArray(vaoHandle);
        // draw the vertices refered to by the indices (index drawing)
        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
        // unbind the VAO
        GL30.glBindVertexArray(0);
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
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);

        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboHandle);

        // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboHandle);

        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }




}
