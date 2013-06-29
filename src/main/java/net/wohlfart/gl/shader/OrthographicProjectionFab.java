package net.wohlfart.gl.shader;

import net.wohlfart.basic.Settings;

import org.lwjgl.util.vector.Matrix4f;

/**
 * creating a simple orthographic matrix
 */
class OrthographicProjectionFab {

    // this is not used and probably not correct yet
    Matrix4f create(Settings settings) {
        final int screenWidth = settings.getWidth();
        final int screenHeight = settings.getHeight();
        final float nearPlane = settings.getNearPlane();
        final float farPlane = settings.getFarPlane();

        final Matrix4f matrix = new Matrix4f();

        final float frustumLength = farPlane - nearPlane;

        matrix.m00 = 2f / screenWidth;
        matrix.m01 = 0;
        matrix.m02 = 0;
        matrix.m03 = 0;

        matrix.m10 = 0;
        matrix.m11 = 2f / screenHeight;
        matrix.m12 = 0;
        matrix.m13 = 0;

        matrix.m20 = 0;
        matrix.m21 = 0;
        matrix.m22 = -2 / frustumLength;
        matrix.m23 = 0;

        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = -(nearPlane + farPlane) / frustumLength;
        matrix.m33 = 1;

        return matrix;
    }

}
