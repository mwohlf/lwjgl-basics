package net.wohlfart.gl.view;

import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * PickingRay class.
 * </p>
 */
public class PickingRay {

    private final Vector3f start;
    private final Vector3f end;

    /**
     * <p>
     * Constructor for PickingRay.
     * </p>
     * 
     * @param start
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param end
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public PickingRay(Vector3f start, Vector3f end) {
        this.start = start;
        this.end = end;
    }

    /**
     * <p>
     * Getter for the field <code>start</code>.
     * </p>
     * 
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Vector3f getStart() {
        return start;
    }

    /**
     * <p>
     * Getter for the field <code>end</code>.
     * </p>
     * 
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public Vector3f getEnd() {
        return end;
    }

}
