package net.wohlfart.gl.shader;

import org.lwjgl.util.vector.Matrix4f;


public interface IShader {

	public abstract void init();

	public abstract void bind();

	public abstract void unbind();

	public abstract int getInPosition();

	public abstract int getInColor();

	public abstract int getInTexCoord();
	
	public abstract void setCurrentModelMatrix(final Matrix4f modelMatrix);

}
