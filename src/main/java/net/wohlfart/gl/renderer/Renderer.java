package net.wohlfart.gl.renderer;

import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.ShaderUniformHandle;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix4f;



/**
 * the renderer's job is to hide the details of the shader from the drawing
 */
public interface Renderer {

	public abstract void setup();

	//public abstract void dispose();

	//public abstract IShaderProgram getShaderProgram();

	public abstract int getVertexAttrib(ShaderAttributeHandle handle);

	public abstract void set(ShaderUniformHandle handle, Matrix4f matrix);

	public abstract void set(ShaderUniformHandle handle, ReadableColor readableColor);

}
