package net.wohlfart.gl.shader;

public interface IGraphicContext {

	int getLocation(ShaderAttributeHandle shaderAttributeHandle);

	int getLocation(ShaderUniformHandle shaderUniformHandle);

}
