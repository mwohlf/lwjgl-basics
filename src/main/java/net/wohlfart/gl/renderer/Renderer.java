package net.wohlfart.gl.renderer;

import net.wohlfart.gl.shader.AttributeHandle;
import net.wohlfart.gl.shader.IShaderProgram;
import net.wohlfart.gl.shader.UniformHandle;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix4f;



/**
 * the renderer's job is to hide the details of the shader from the drawing
 */
public interface Renderer {

	public abstract void setup();

	public abstract void dispose();

	public abstract IShaderProgram getShaderProgram();

	public abstract int getVertexAttrib(AttributeHandle handle);

	public abstract void set(UniformHandle handle, Matrix4f matrix);

	public abstract void set(UniformHandle handle, ReadableColor readableColor);

}
