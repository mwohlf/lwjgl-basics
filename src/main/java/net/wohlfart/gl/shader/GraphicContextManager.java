package net.wohlfart.gl.shader;

import net.wohlfart.basic.Settings;
import net.wohlfart.basic.time.Clock;
import net.wohlfart.gl.input.DefaultInputDispatcher;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @formatter:off
 * wrapping a GraphicContext, if we ever need to switch the GraphicContext
 *
 * todo: implement stuff from: http://www.lwjgl.org/wiki/index.php?title=GLSL_Utility_Class
 * @formatter:on
 */
public enum GraphicContextManager {
    INSTANCE;

    protected static final Logger LOGGER = LoggerFactory.getLogger(GraphicContextManager.class);

    private static final float FIELD_OF_VIEW_LIMIT = 100; // << 180

    public interface IGraphicContext {

        int getLocation(ShaderAttributeHandle shaderAttributeHandle);

        int getLocation(ShaderUniformHandle shaderUniformHandle);

        void bind();

        void unbind();

        void dispose();

    }

    private IGraphicContext currentGraphicContext;
    // the projection matrix defines the lens of the camera, e.g. view angle
    private Matrix4f perspectiveProjMatrix;
    private Matrix4f orthographicProjMatrix;
    // game settings
    private Settings settings;

    private InputDispatcher inputDispatcher;
    private Clock clock;


    public void setCurrentGraphicContext(IGraphicContext graphicContext) {
        LOGGER.debug("setting gfx context to '{}'", graphicContext);

        if (currentGraphicContext != null) {
            currentGraphicContext.unbind();
        }
        currentGraphicContext = graphicContext;
        if (currentGraphicContext != null) {
            currentGraphicContext.bind();
        }
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        perspectiveProjMatrix = createPerspectiveProjectionMatrix();
        orthographicProjMatrix = createOrthographicProjectionMatrix();
    }

    // package private, only used by for ShaderAttributeHandle and ShaderUniformHandle
    IGraphicContext getCurrentGraphicContext() {
        return currentGraphicContext;
    }

    public Matrix4f getPerspectiveProjMatrix() {
        return perspectiveProjMatrix;
    }

    public Matrix4f getOrthographicProjMatrix() {
        return orthographicProjMatrix;
    }

    public int getScreenWidth() {
        return settings.getWidth();
    }

    public int getScreenHeight() {
        return settings.getHeight();
    }

    public float getNearPlane() {
        return settings.getNearPlane();    //   0.1
    }

    public float getFarPlane() {
        return settings.getFarPlane();      // 100
    }

    public float getFieldOfView() {
        return settings.getFieldOfView();
    }


    public void setInputDispatcher(DefaultInputDispatcher inputSource) {
        this.inputDispatcher = inputSource;
    }

    public InputDispatcher getInputDispatcher() {
        return inputDispatcher;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public Clock getClock() {
        return clock;
    }

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
    private Matrix4f createPerspectiveProjectionMatrix() {

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
        matrix.m32 = -((2 * nearPlane * farPlane) / frustumLength);
        matrix.m33 = 0;

        return matrix;
    }

    // this is not used and probably not correct yet
    private Matrix4f createOrthographicProjectionMatrix() {
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


    public void destroy() {

    }

}
