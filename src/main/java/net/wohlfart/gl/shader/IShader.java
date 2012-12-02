package net.wohlfart.gl.shader;

import org.lwjgl.util.vector.Matrix4f;

public interface IShader {

	public abstract void init();

	public abstract void bind();

	public abstract void unbind();

}
