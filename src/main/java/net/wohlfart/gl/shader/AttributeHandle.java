package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL20;


// handler for vertex attributes used in the shader
public enum AttributeHandle {
	COLOR(IShaderProgram.COLOR),
	POSITION(IShaderProgram.POSITION),
	TEXTURE_COORD(IShaderProgram.TEXTURE_COORD);

	private final String lookupString;

	AttributeHandle(final String lookupString) {
		this.lookupString = lookupString;
	}

	public int getLocation(final ShaderProgram shaderProgram) {
		return GL20.glGetAttribLocation(shaderProgram.getProgramId(), lookupString);
	}

}
