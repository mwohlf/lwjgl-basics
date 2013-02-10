package net.wohlfart.gl.elements.hud;

public class CharInfo {
    char c;
    float x;
    float y;
    float w;
    float h;

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
