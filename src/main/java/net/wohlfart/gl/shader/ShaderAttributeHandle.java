package net.wohlfart.gl.shader;

import static net.wohlfart.gl.shader.GraphicContextManager.INSTANCE;

//
/**
 * <p>
 * All handlers for vertex attributes used in any shader.
 * </p>
 */
public enum ShaderAttributeHandle {// @formatter:off
    COLOR("in_Color", 4),
    POSITION("in_Position", 4),
    TEXTURE_COORD("in_TexCoord", 2),
    NORMAL("in_Normal", 4);
    // @formatter:on

    private final String lookupString;
    private final int floatCount;

    ShaderAttributeHandle(String lookupString, int floatCount) {
        this.lookupString = lookupString;
        this.floatCount = floatCount;
    }

    String getLookupString() {
        return lookupString;
    }

    /**
     * <p>
     * Returns the number of floats this attribute uses.
     * </p>
     * 
     * @return a int.
     */
    public int getFloatCount() {
        return floatCount;
    }

    /**
     * <p>
     * Returns the number of bytes this attribute uses.
     * </p>
     * 
     * @return a int.
     */
    public int getByteCount() {
        return floatCount * 4;
    }

    /**
     * <p>
     * getLocation.
     * </p>
     * 
     * @return a int.
     */
    public int getLocation() {
        return INSTANCE.getCurrentGraphicContext().getLocation(this);
    }

}
