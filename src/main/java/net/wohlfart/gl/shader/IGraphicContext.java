package net.wohlfart.gl.shader;

public interface IGraphicContext {

	int getLocation(ShaderAttributeHandle shaderAttributeHandle);

	int getLocation(ShaderUniformHandle shaderUniformHandle);

	void bind();

	void unbind();

	void dispose();

}
