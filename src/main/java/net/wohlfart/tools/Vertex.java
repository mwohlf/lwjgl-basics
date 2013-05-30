package net.wohlfart.tools;

import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * Vertex class.<br/>
 * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_interleaved
 * </p>
 */
public class Vertex {
    // Vertex data
    private float[] xyzw = new float[] { 0f, 0f, 0f, 1f };
    private float[] rgba = new float[] { 1f, 1f, 1f, 1f };
    private float[] st = new float[] { 0f, 0f };

    /** The amount of bytes an element(float) has */
    public static final int elementBytes = 4;

    // Elements per parameter
    /** Constant <code>positionElementCount=4</code> */
    public static final int positionElementCount = 4;
    /** Constant <code>colorElementCount=4</code> */
    public static final int colorElementCount = 4;
    /** Constant <code>textureElementCount=2</code> */
    public static final int textureElementCount = 2;

    // Bytes per parameter
    /** Constant <code>positionBytesCount=positionElementCount * elementBytes</code> */
    public static final int positionBytesCount = positionElementCount * elementBytes;
    /** Constant <code>colorByteCount=colorElementCount * elementBytes</code> */
    public static final int colorByteCount = colorElementCount * elementBytes;
    /** Constant <code>textureByteCount=textureElementCount * elementBytes</code> */
    public static final int textureByteCount = textureElementCount * elementBytes;

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

    // Setters
    /**
     * <p>
     * setXYZ.
     * </p>
     * 
     * @param x
     *            a float.
     * @param y
     *            a float.
     * @param z
     *            a float.
     */
    public void setXYZ(float x, float y, float z) {
        this.setXYZW(x, y, z, 1f);
    }

    /**
     * <p>
     * setXYZ.
     * </p>
     * 
     * @param vec
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void setXYZ(Vector3f vec) {
        this.setXYZW(vec.x, vec.y, vec.z, 1f);
    }

    /**
     * <p>
     * setRGB.
     * </p>
     * 
     * @param r
     *            a float.
     * @param g
     *            a float.
     * @param b
     *            a float.
     */
    public void setRGB(float r, float g, float b) {
        this.setRGBA(r, g, b, 1f);
    }

    /**
     * <p>
     * setXYZW.
     * </p>
     * 
     * @param x
     *            a float.
     * @param y
     *            a float.
     * @param z
     *            a float.
     * @param w
     *            a float.
     */
    public void setXYZW(float x, float y, float z, float w) {
        this.xyzw = new float[] { x, y, z, w };
    }

    /**
     * <p>
     * setST.
     * </p>
     * 
     * @param s
     *            a float.
     * @param t
     *            a float.
     */
    public void setST(float s, float t) {
        this.st = new float[] { s, t };
    }

    /**
     * <p>
     * setRGBA.
     * </p>
     * 
     * @param r
     *            a float.
     * @param g
     *            a float.
     * @param b
     *            a float.
     * @param a
     *            a float.
     */
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
    public float[] getElements() {
        final float[] out = new float[Vertex.elementCount];
        int i = 0;

        // Insert XYZW elements
        out[i++] = this.xyzw[0];
        out[i++] = this.xyzw[1];
        out[i++] = this.xyzw[2];
        out[i++] = this.xyzw[3];
        // Insert RGBA elements
        out[i++] = this.rgba[0];
        out[i++] = this.rgba[1];
        out[i++] = this.rgba[2];
        out[i++] = this.rgba[3];
        // Insert ST elements
        out[i++] = this.st[0];
        out[i++] = this.st[1];

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
