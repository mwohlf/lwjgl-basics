package net.wohlfart.gl.shader;

import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * Vertex class.<br/>
 * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_interleaved
 * </p>
 */
public class Vertex {
    private float[] xyzw = new float[] { 0f, 0f, 0f, 1f };
    private float[] rgba = new float[] { 1f, 1f, 1f, 1f };
    private float[] normal = new float[] { 1f, 0f, 0f };
    private float[] st = new float[] { 0f, 0f };

    public static final int bytesPerFloat = 4;

    public static final int positionElementCount = 4;
    public static final int colorElementCount = 4;
    public static final int normalElementCount = 3;
    public static final int textureElementCount = 2;

    public static final int positionBytesCount = positionElementCount * bytesPerFloat;
    public static final int colorByteCount = colorElementCount * bytesPerFloat;
    public static final int normalByteCount = normalElementCount * bytesPerFloat;
    public static final int textureByteCount = textureElementCount * bytesPerFloat;

    // Byte offsets per parameter
    /** Constant <code>positionByteOffset=0</code> */
    public static final int positionByteOffset = 0;
    /** Constant <code>colorByteOffset=positionByteOffset + positionBytesCount</code> */
    public static final int colorByteOffset = positionByteOffset + positionBytesCount;
    /** Constant <code>textureByteOffset=colorByteOffset + colorByteCount</code> */
    public static final int textureByteOffset = colorByteOffset + colorByteCount;

    // The amount of elements that a vertex has
    /** Constant <code>elementCount=positionElementCount + colorElementCount + textureElementCount</code> */
    public static final int elementCount = positionElementCount + colorElementCount + textureElementCount;
    // The size of a vertex in bytes, like in C/C++: sizeof(Vertex)
    /** Constant <code>stride=positionBytesCount + colorByteCount + textureByteCount</code> */
    public static final int stride = positionBytesCount + colorByteCount + textureByteCount;


    public void setXYZ(float x, float y, float z) {
        this.setXYZW(x, y, z, 1f);
    }

    public void setNormal(float x, float y, float z) {
        this.normal = new float[] { x, y, z };
    }

    public void setXYZ(Vector3f vec) {
        this.setXYZW(vec.x, vec.y, vec.z, 1f);
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

    /**
     * <p>
     * getElements.
     * </p>
     *
     * @return an array of float.
     */
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

    /**
     * <p>
     * getXYZW.
     * </p>
     *
     * @return an array of float.
     */
    public float[] getXYZW() {
        return new float[] { this.xyzw[0], this.xyzw[1], this.xyzw[2], this.xyzw[3] };
    }

    /**
     * <p>
     * getRGBA.
     * </p>
     *
     * @return an array of float.
     */
    public float[] getRGBA() {
        return new float[] { this.rgba[0], this.rgba[1], this.rgba[2], this.rgba[3] };
    }

    /**
     * <p>
     * getST.
     * </p>
     *
     * @return an array of float.
     */
    public float[] getST() {
        return new float[] { this.st[0], this.st[1] };
    }
}
