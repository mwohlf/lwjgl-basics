package net.wohlfart.gl.shader;

import net.wohlfart.basic.Settings;

import org.lwjgl.util.vector.Matrix4f;

class OrthographicProjection {

    // this is not used and probably not correct yet
    Matrix4f create(Settings settings) {
        int screenWidth = settings.getWidth();
        int screenHeight = settings.getHeight();
        float nearPlane = settings.getNearPlane();    // 0.1
        float farPlane = settings.getFarPlane();      // 100

        // Setup projection matrix
        final Matrix4f matrix = new Matrix4f();
        // the view angle in degree, 45 is fine

        final float frustumLength = farPlane - nearPlane;

        matrix.m00 = 2f/screenWidth;
        matrix.m01 = 0;
        matrix.m02 = 0;
        matrix.m03 = 0;

        matrix.m10 = 0;
        matrix.m11 = 2f/screenHeight;
        matrix.m12 = 0;
        matrix.m13 = 0;

        matrix.m20 = 0;
        matrix.m21 = 0;
        matrix.m22 = -2/frustumLength; // zScale
        matrix.m23 = 0;

        matrix.m30 = 0; //reenWidth/2;
        matrix.m31 = 0; // -screenHeight/2;
        matrix.m32 = -(nearPlane + farPlane) / frustumLength;
        matrix.m33 = 1;

        return matrix;
    }

}
