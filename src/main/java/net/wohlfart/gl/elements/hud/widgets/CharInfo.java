package net.wohlfart.gl.elements.hud.widgets;

/*
 * dimension info for a single character
 */
class CharInfo {
    protected final char c;
    protected final float x;
    protected final float y;
    protected final float w;
    protected final float h;

    CharInfo(char c, float x, float y, float w, float h) {
        this.c = c;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * <p>
     * Getter for the field <code>x</code>.
     * </p>
     * 
     * @return a float.
     */
    public float getX() {
        return x;
    }

    /**
     * <p>
     * Getter for the field <code>y</code>.
     * </p>
     * 
     * @return a float.
     */
    public float getY() {
        return y;
    }

    /**
     * <p>
     * getWidth.
     * </p>
     * 
     * @return a float.
     */
    public float getWidth() {
        return w;
    }

    /**
     * <p>
     * getHeight.
     * </p>
     * 
     * @return a float.
     */
    public float getHeight() {
        return h;
    }

}
