package net.wohlfart.gl.shader;

import net.wohlfart.basic.Settings;
import net.wohlfart.basic.time.Clock;
import net.wohlfart.gl.input.InputDispatcher;

import org.lwjgl.util.vector.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>GraphicContextManager class.</p>
 *
 *
 *
 */
public enum GraphicContextManager {
    INSTANCE;

    /** Constant <code>LOGGER</code> */
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

    // game settings from the config file
    private Settings settings;
    // user input
    private InputDispatcher inputDispatcher;
    // timer
    private Clock clock;



    /**
     * <p>Setter for the field <code>currentGraphicContext</code>.</p>
     *
     * @param graphicContext a {@link net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext} object.
     */
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

    /**
     * <p>Setter for the field <code>settings</code>.</p>
     *
     * @param settings a {@link net.wohlfart.basic.Settings} object.
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
        perspectiveProjMatrix = new PerspectiveProjection() .create(settings);
        orthographicProjMatrix = new OrthographicProjection() .create(settings);
    }


    // the following methods are package private,
    // and shold only used by ShaderAttributeHandle and ShaderUniformHandle

    IGraphicContext getCurrentGraphicContext() {
        return currentGraphicContext;
    }



    /**
     * <p>Getter for the field <code>perspectiveProjMatrix</code>.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    public Matrix4f getPerspectiveProjMatrix() {
        return perspectiveProjMatrix;
    }

    /**
     * <p>Getter for the field <code>orthographicProjMatrix</code>.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    public Matrix4f getOrthographicProjMatrix() {
        return orthographicProjMatrix;
    }

    /**
     * <p>getScreenWidth.</p>
     *
     * @return a int.
     */
    public int getScreenWidth() {
        return settings.getWidth();
    }

    /**
     * <p>getScreenHeight.</p>
     *
     * @return a int.
     */
    public int getScreenHeight() {
        return settings.getHeight();
    }

    /**
     * <p>getNearPlane.</p>
     *
     * @return a float.
     */
    public float getNearPlane() {
        return settings.getNearPlane();    //   0.1
    }

    /**
     * <p>getFarPlane.</p>
     *
     * @return a float.
     */
    public float getFarPlane() {
        return settings.getFarPlane();      // 100
    }

    /**
     * <p>getFieldOfView.</p>
     *
     * @return a float.
     */
    public float getFieldOfView() {
        return settings.getFieldOfView();
    }

    /**
     * <p>Setter for the field <code>inputDispatcher</code>.</p>
     *
     * @param inputSource a {@link net.wohlfart.gl.input.DefaultInputDispatcher} object.
     */
    public void setInputDispatcher(InputDispatcher inputSource) {
        this.inputDispatcher = inputSource;
    }

    /**
     * <p>Getter for the field <code>inputDispatcher</code>.</p>
     *
     * @return a {@link net.wohlfart.gl.input.InputDispatcher} object.
     */
    public InputDispatcher getInputDispatcher() {
        return inputDispatcher;
    }

    /**
     * <p>destroy.</p>
     */
    public void destroy() {

    }

}
