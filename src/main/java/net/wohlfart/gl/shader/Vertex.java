package net.wohlfart.gl.shader;

import org.lwjgl.util.vector.Vector3f;

/**
 * tool class to create VAOs and VBOs
 *
 * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_interleaved
 */
public class Vertex {

    public static final int BYTES_PER_FLOAT = 4;

    public static final int POSITION_ELEM_COUNT = 4;
    public static final int COLOR_ELEM_COUNT = 4;
    public static final int NORMAL_ELEM_COUNT = 3;
    public static final int TEXTURE_ELEM_COUNT = 2;

    public static final int positionBytesCount = POSITION_ELEM_COUNT * BYTES_PER_FLOAT;
    public static final int colorByteCount = COLOR_ELEM_COUNT * BYTES_PER_FLOAT;
    public static final int normalByteCount = NORMAL_ELEM_COUNT * BYTES_PER_FLOAT;
    public static final int textureByteCount = TEXTURE_ELEM_COUNT * BYTES_PER_FLOAT;

    // Byte offsets per parameter
    public static final int positionByteOffset = 0;
    public static final int colorByteOffset = positionByteOffset + positionBytesCount;
    public static final int normalByteOffset = colorByteOffset + colorByteCount;
    public static final int textureByteOffset = normalByteOffset + normalByteCount;

    // The amount of elements that a vertex has
    public static final int elementCount = POSITION_ELEM_COUNT + COLOR_ELEM_COUNT + NORMAL_ELEM_COUNT + TEXTURE_ELEM_COUNT;
    // The size of a vertex in bytes, like in C/C++: sizeof(Vertex)
    public static final int stride = positionBytesCount + colorByteCount + normalByteCount + textureByteCount;



    private float[] xyzw = new float[] { 0f, 0f, 0f, 1f };
    private float[] rgba = new float[] { 1f, 1f, 1f, 1f };
    private float[] normal = new float[] { 1f, 0f, 0f };
    private float[] st = new float[] { 0f, 0f };


    public void setXYZ(Vector3f vec) {
        this.setXYZW(vec.x, vec.y, vec.z, 1f);
    }

    public void setXYZ(float x, float y, float z) {
        this.setXYZW(x, y, z, 1f);
    }

    public void setNormal(Vector3f vec) {
        this.setNormal(vec.x, vec.y, vec.z);
    }

    public void setNormal(float x, float y, float z) {
        this.normal = new float[] { x, y, z };
    }

    public void setRGB(float r, float g, float b) {
        this.setRGBA(r, g, b, 1f);
    }

    public void setXYZW(float x, float y, float z, float w) {
        this.xyzw = new float[] { x, y, z, w };
    }

    public void setST(float s, float t) {
        this.st = new float[] { s, t };
    }

    public void setRGBA(float r, float g, float b, float a) {
        this.rgba = new float[] { r, g, b, 1f };
    }


    public float[] getAllElements() {
        final float[] out = new float[Vertex.elementCount];
        int i = 0;

        out[i++] = this.xyzw[0];
        out[i++] = this.xyzw[1];
        out[i++] = this.xyzw[2];
        out[i++] = this.xyzw[3];

        out[i++] = this.rgba[0];
        out[i++] = this.rgba[1];
        out[i++] = this.rgba[2];
        out[i++] = this.rgba[3];

        out[i++] = this.st[0];
        out[i++] = this.st[1];

        out[i++] = this.normal[0];
        out[i++] = this.normal[1];
        out[i++] = this.normal[2];

        return out;
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
