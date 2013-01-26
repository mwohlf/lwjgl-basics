package net.wohlfart.gl.shader;


import org.lwjgl.opengl.GL20;


// handler for vertex attributes used in the shader
public enum ShaderAttributeHandle {
	COLOR("in_Color", 4),
	POSITION("in_Position", 4),
	TEXTURE_COORD("in_TexCoord", 2);

	private final String lookupString;
	private final int size;

	ShaderAttributeHandle(String lookupString, int size) {
		this.lookupString = lookupString;
		this.size = size;
	}

	// FIXME: this method has to be removed
	public int getLocation(final IShaderProgram shaderProgram) {
		return GL20.glGetAttribLocation(shaderProgram.getProgramId(), lookupString);
	}

	public int getSize() {
		return size;
	}

	public int getLocation() {
		return GraphicContextManager.INSTANCE.getLocation(this);
	}

}
