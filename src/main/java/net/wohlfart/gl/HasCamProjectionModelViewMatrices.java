package net.wohlfart.gl;

import org.lwjgl.util.vector.Matrix4f;

public interface HasCamProjectionModelViewMatrices {

    // current projection matrix in use (shouldn't change at all)
    public Matrix4f getProjectionMatrix();
    // current modelView matrix, changed when cam moves or rotates
    public Matrix4f getModelViewMatrix();


}
