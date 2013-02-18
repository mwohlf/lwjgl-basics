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

    public interface IGraphicContext {

        int getLocation(ShaderAttributeHandle shaderAttributeHandle);

        int getLocation(ShaderUniformHandle shaderUniformHandle);

        void bind();

        void unbind();

        void dispose();

    }

    private IGraphicContext currentGraphicContext;
    // the projection matrix defines the lens of the camera, e.g. view angle
    private Matrix4f projectionMatrix;
    // game settings
    private Settings settings;

    private InputDispatcher inputDispatcher;
    //private int screenDiagonal;
    private Clock clock;
    private int screenWidth;
    private int screenHeight;


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
        assert settings == null: "settings is already set";
        this.settings = settings;
        assert projectionMatrix == null: "projection matrix is already set";
        projectionMatrix = createProjectionMatrix();
        screenWidth = settings.getWidth();
        screenHeight = settings.getHeight();
        //this.screenDiagonal = (int) Math.ceil(Math.sqrt( w * w + h * h));
    }

    // package private, only used by for ShaderAttributeHandle and ShaderUniformHandle
    IGraphicContext getCurrentGraphicContext() {
        return currentGraphicContext;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
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
     * - the view matrix defines the position and the direction of the camera
     * - the model matrix defines
     * the position and direction of each 3D model
     * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_with_Projection,_View_and_Model_matrices
     *
     * @return our projection matrix
     * @formatter:on
     */
    private Matrix4f createProjectionMatrix() {
        // Setup projection matrix
        final Matrix4f matrix = new Matrix4f();
        // the view angle in degree, 45 is fine
        final float fieldOfView = settings.getFieldOfView();
        final float aspectRatio = (float) settings.getWidth() / (float) settings.getHeight();
        final float nearPlane = settings.getNearPlane();
        final float farPlane = settings.getFarPlane();

        final float yScale = SimpleMath.coTan(SimpleMath.deg2rad(fieldOfView / 2f));
        final float xScale = yScale / aspectRatio;
        final float frustumLength = farPlane - nearPlane;

        matrix.m00 = xScale;
        matrix.m11 = yScale;
        matrix.m22 = -((farPlane + nearPlane) / frustumLength); // zScale
        matrix.m23 = -1;
        matrix.m32 = -(2 * nearPlane * farPlane / frustumLength);

        return matrix;
    }

}
