package net.wohlfart.gl.shader;

import org.lwjgl.util.vector.Vector3f;

/**
 * tool class to create VAOs and VBOs
 *
 * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_interleaved
 */
public class Vertex {

    private static final int BYTES_PER_FLOAT = 4;

    private static final int POSITION_ELEM_COUNT = 4;
    private static final int COLOR_ELEM_COUNT = 4;
    private static final int NORMAL_ELEM_COUNT = 3;
    private static final int TEXTURE_ELEM_COUNT = 2;

    private static final int positionBytesCount = POSITION_ELEM_COUNT * BYTES_PER_FLOAT;
    private static final int colorByteCount = COLOR_ELEM_COUNT * BYTES_PER_FLOAT;
    private static final int normalByteCount = NORMAL_ELEM_COUNT * BYTES_PER_FLOAT;
    private static final int textureByteCount = TEXTURE_ELEM_COUNT * BYTES_PER_FLOAT;

    // Byte offsets per parameter
    private static final int positionByteOffset = 0;
    private static final int colorByteOffset = positionByteOffset + positionBytesCount;
    private static final int normalByteOffset = colorByteOffset + colorByteCount;
    private static final int textureByteOffset = normalByteOffset + normalByteCount;

    // The amount of elements that a vertex has
    private static final int elementCount = POSITION_ELEM_COUNT + COLOR_ELEM_COUNT + NORMAL_ELEM_COUNT + TEXTURE_ELEM_COUNT;
    // The size of a vertex in bytes, like in C/C++: sizeof(Vertex)
    private static final int stride = positionBytesCount + colorByteCount + normalByteCount + textureByteCount;



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
