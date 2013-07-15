package net.wohlfart.gl.view;

import org.lwjgl.util.vector.Vector3f;

/**
 * PickingRay class.
 */
public class PickingRay {

    private final Vector3f start;
    private final Vector3f end;


    public PickingRay(Vector3f start, Vector3f end) {
        this.start = start;
        this.end = end;
    }


    public Vector3f getStart() {
        return start;
    }


    public Vector3f getEnd() {
        return end;
    }

}
