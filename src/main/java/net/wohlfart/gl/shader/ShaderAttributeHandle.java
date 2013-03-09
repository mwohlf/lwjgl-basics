package net.wohlfart.gl.shader;

import static net.wohlfart.gl.shader.GraphicContextManager.INSTANCE;

// handler for vertex attributes used in the shader
public enum ShaderAttributeHandle { // @formatter:off
    COLOR("in_Color", 4),
    POSITION("in_Position", 4),
    TEXTURE_COORD("in_TexCoord", 2);
    // @formatter:on

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
        return INSTANCE.getCurrentGraphicContext().getLocation(this);
    }

}
