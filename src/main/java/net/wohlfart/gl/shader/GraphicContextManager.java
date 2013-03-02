package net.wohlfart.gl.shader;

import net.wohlfart.basic.Settings;
import net.wohlfart.basic.time.Clock;
import net.wohlfart.gl.input.DefaultInputDispatcher;
import net.wohlfart.gl.input.InputDispatcher;

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

    // current shader and stuff
    private IGraphicContext currentGraphicContext;
    // pre-calculated projection matrices to select from
    private Matrix4f perspectiveProjMatrix;
    private Matrix4f orthographicProjMatrix;

    // current projection matrix in use (shouldn't change at all)
    public Matrix4f projectionMatrix;
    // current modelView matrix, changed when cam moves or rotates
    public Matrix4f modelViewMatrix;

    // game settings from the config file
    private Settings settings;
    // user input
    private InputDispatcher inputDispatcher;
    // timer
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
        perspectiveProjMatrix = new PerspectiveProjection() .create(settings);
        orthographicProjMatrix = new OrthographicProjection() .create(settings);
    }


    // the following methods are package private,
    // and shold only used by ShaderAttributeHandle and ShaderUniformHandle

    void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    void setModelViewMatrix(Matrix4f modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
    }

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

    public void destroy() {

    }

}
