package net.wohlfart.gl.elements;

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


// see: http://lwjgl.blogspot.de/2012/04/chapter-one-triangle.html
public class Triangle implements IDrawable, IPoolable {
    private static final int VECTOR_SIZE = 4;

    private IShader shader;
    // A VAO can have up to 16 attributes (VBO's) assigned to it by default
    private int vaoHandle;   // Vertex Array Object
    private int vboHandle;   // Vertex Buffer Object

    private int vertexCount;


    public Triangle(final IShader shader) {
        this( shader,
              new Vector4f(+0.0f,+0.5f,+0.0f,+1.0f),
              new Vector4f(-0.5f,-0.5f,+0.0f,+1.0f),
              new Vector4f(+0.5f,-0.5f,+0.0f,+1.0f)  );
    }

    public Triangle(final IShader shader,
                    final Vector4f tm,
                    final Vector4f bl,
                    final Vector4f br) {
        this.shader = shader;

        float[] vertices = new float[] {
                tm.x, tm.y, tm.z, tm.w,
                bl.x, bl.y, bl.z, tm.w,
                br.x, br.y, br.z, tm.w};
        vertexCount = 3;

        // create a VAO in memory
        vaoHandle = GL30.glGenVertexArrays();
        // select/bind the VAO
        GL30.glBindVertexArray(vaoHandle);

        // create a VBO in the VAO
        vboHandle = GL15.glGenBuffers();
        // select/bind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboHandle);
        // create & flip a buffer for the vertices
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(this.vertexCount * VECTOR_SIZE);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();
        // set the size and data of the VBO and set it to STATIC_DRAW
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        // enable the position shader attribute
        GL20.glEnableVertexAttribArray(shader.getInPosition());
        // setup the OpenGL pipeline, refers to whatever is bound using the GL_ARRAY_BUFFER
        // VECTOR_SIZE size of a single vertex, float values, 0 space between the vertices, 0 offset from the buffer start
        GL20.glVertexAttribPointer(shader.getInPosition(), VECTOR_SIZE, GL11.GL_FLOAT, false, 0, 0);

        GL30.glBindVertexArray(0);
    }


    @Override
    public void draw() {
        GL30.glBindVertexArray(vaoHandle);                         // bind to the VAO
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);      // draw triangles, start at 0, do use vertexCount vertices
        GL30.glBindVertexArray(0);                                 // unbind the VAO
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

        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }

}
