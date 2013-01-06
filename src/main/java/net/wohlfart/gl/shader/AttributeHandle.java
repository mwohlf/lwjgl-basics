package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL20;


// handler for vertex attributes used in the shader
public enum AttributeHandle {
	COLOR(IShaderProgram.COLOR, 4),
	POSITION(IShaderProgram.POSITION, 4),
	TEXTURE_COORD(IShaderProgram.TEXTURE_COORD, 2);

	private final String lookupString;
	private final int size;

	AttributeHandle(String lookupString, int size) {
		this.lookupString = lookupString;
		this.size = size;
	}

	public int getLocation(final ShaderProgram shaderProgram) {
		return GL20.glGetAttribLocation(shaderProgram.getProgramId(), lookupString);
	}

	public int getSize() {
		return size;
	}

}
