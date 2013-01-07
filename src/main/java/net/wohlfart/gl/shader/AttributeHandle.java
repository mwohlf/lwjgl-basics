package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL20;


// handler for vertex attributes used in the shader
public enum AttributeHandle {
	COLOR("in_Color", 4),
	POSITION("in_Position", 4),
	TEXTURE_COORD("in_TexCoord", 2);

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
