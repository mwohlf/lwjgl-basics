package net.wohlfart.gl.shader;

import net.wohlfart.basic.Settings;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @formatter:off
 * - the projection matrix defines the lens of the camera
 *   it translates the world space into 2D screen space
 *
 * - the view matrix defines the position and the direction of the camera
 *   it is set once per rendering pass and defined in which direction the cam is looking
 *
 * - the model matrix defines the position and direction of each 3D model
 *   it is used to move and rotate a model in the world space around
 *   each model can set its individual matrix before rendering so it is
 *   set for each model object
 *
 * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_with_Projection,_View_and_Model_matrices
 * see: http://db-in.com/blog/2011/04/cameras-on-opengl-es-2-x/
 * see: http://www.songho.ca/opengl/gl_projectionmatrix.html
 *
 * @return our projection matrix
 * @formatter:on
 */
class PerspectiveProjection {
    static final Logger LOGGER = LoggerFactory.getLogger(PerspectiveProjection.class);

    private static final float FIELD_OF_VIEW_LIMIT = 100; // << 180

    Matrix4f create(Settings settings) {

        // Setup projection matrix
        final Matrix4f matrix = new Matrix4f();
        // the view angle in degree, 45 is fine

        float fieldOfView = settings.getFieldOfView();      //  45 degree

        if (fieldOfView > FIELD_OF_VIEW_LIMIT) {
            LOGGER.warn("field of view must be < {} found: '{}', resetting to {}", FIELD_OF_VIEW_LIMIT, fieldOfView, FIELD_OF_VIEW_LIMIT);
            fieldOfView = Math.min(fieldOfView, FIELD_OF_VIEW_LIMIT);
        }

        float nearPlane = settings.getNearPlane();    // 0.1
        float farPlane = settings.getFarPlane();      // 100
        float frustumLength = farPlane - nearPlane;
        float aspectRatio = (float)settings.getWidth() / (float)settings.getHeight();
        float yScale = SimpleMath.coTan(SimpleMath.deg2rad(fieldOfView / 2f));
        float xScale = yScale / aspectRatio;
        float zScale = -((farPlane + nearPlane) / frustumLength);

        matrix.m00 = xScale;
        matrix.m01 = 0;
        matrix.m02 = 0;
        matrix.m03 = 0;

        matrix.m10 = 0;
        matrix.m11 = yScale;
        matrix.m12 = 0;
        matrix.m13 = 0;

        matrix.m20 = 0;
        matrix.m21 = 0;
        matrix.m22 = zScale;
        matrix.m23 = -1;

        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = -((2f * nearPlane * farPlane) / frustumLength);
        matrix.m33 = 0;

        return matrix;
    }

}
