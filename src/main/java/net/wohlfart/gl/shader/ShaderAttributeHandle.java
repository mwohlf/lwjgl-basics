package net.wohlfart.gl.shader;




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

	String getLookupString() {
		return lookupString;
	}



	public int getSize() {
		return size;
	}

	public int getLocation() {
		return GraphicContextManager.INSTANCE.getLocation(this);
	}

}
