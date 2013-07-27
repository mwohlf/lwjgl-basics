package net.wohlfart.gl.shader;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Vector3f;

/**
 * tool class to create VAOs and VBOs
 *
 * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_interleaved
 */
public class Vertex { // REVIEWED

    private float[] xyzw = new float[] { 0f, 0f, 0f, 1f };
    private float[] rgba = new float[] { 1f, 1f, 1f, 1f };
    private float[] normal = new float[] { 1f, 0f, 0f };
    private float[] st = new float[] { 0f, 0f };


    public Vertex setXYZ(Vector3f vec) {
        this.setXYZW(vec.x, vec.y, vec.z, 1f);
        return this;
    }

    public Vertex setXYZ(float x, float y, float z) {
        this.setXYZW(x, y, z, 1f);
        return this;
    }

    public Vertex setNormal(Vector3f vec) {
        this.setNormal(vec.x, vec.y, vec.z);
        return this;
    }

    public Vertex setNormal(float x, float y, float z) {
        this.normal = new float[] { x, y, z };
        return this;
    }

    public Vertex setRGB(float r, float g, float b) {
        this.setRGBA(r, g, b, 1f);
        return this;
    }

    public Vertex setXYZW(float x, float y, float z, float w) {
        this.xyzw = new float[] { x, y, z, w };
        return this;
    }

    public Vertex setST(float s, float t) {
        this.st = new float[] { s, t };
        return this;
    }

    public Vertex setRGBA(float r, float g, float b, float a) {
        this.rgba = new float[] { r, g, b, 1f };
        return this;
    }


    public Vertex setColor(ReadableColor color) {
        this.rgba = new float[] {
                color.getRedByte()/255f,
                color.getGreenByte()/255f,
                color.getBlueByte()/255f,
                color.getAlphaByte()/255f };
        return this;
    }

    public float[] getXYZ() {
        return new float[] { this.xyzw[0], this.xyzw[1], this.xyzw[2] };
    }

    public float[] getXYZW() {
        return new float[] { this.xyzw[0], this.xyzw[1], this.xyzw[2], this.xyzw[3] };
    }

    public float[] getRGBA() {
        return new float[] { this.rgba[0], this.rgba[1], this.rgba[2], this.rgba[3] };
    }

    public float[] getST() {
        return new float[] { this.st[0], this.st[1] };
    }

    public float[] getNormal() {
        return new float[] { this.normal[0], this.normal[1], this.normal[2] };
    }

}
