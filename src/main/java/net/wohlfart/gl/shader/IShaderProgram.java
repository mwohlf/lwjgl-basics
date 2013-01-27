package net.wohlfart.gl.shader;


public interface IShaderProgram  {

	// to id the shader
	abstract int getProgramId();

	// called when the shader in created
	abstract void setup();

	// called when the shader program is about to be used
	abstract void bind();

	// called when the shader is no longer used
	public abstract void unbind();

	// called when the shader is no longer needed
	public abstract void dispose();

}
