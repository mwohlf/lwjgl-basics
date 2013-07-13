package net.wohlfart.gl.elements.hud.txt;

/*
 * dimension info for a single character
 */
public class CharInfo {
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }

}
