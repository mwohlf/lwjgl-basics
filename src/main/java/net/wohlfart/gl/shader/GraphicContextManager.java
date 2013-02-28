package net.wohlfart.gl.shader;

import net.wohlfart.basic.Settings;
import net.wohlfart.basic.time.Clock;
import net.wohlfart.gl.PickingRay;
import net.wohlfart.gl.input.DefaultInputDispatcher;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
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
    private Matrix4f perspectiveProjMatrix;
    private Matrix4f orthographicProjMatrix;
    //
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
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
        perspectiveProjMatrix = new PerspectiveProjection() .create(settings);
        orthographicProjMatrix = new OrthographicProjection() .create(settings);
    }

    void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    void setViewMatrix(Matrix4f viewMatrix) {
        this.viewMatrix = viewMatrix;
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

    public PickingRay createPickingRay(float x, float y, Avatar avatar) {
        Matrix4f m = projectionMatrix;
        float width = settings.getWidth();
        float height = settings.getHeight();
        float nearPlane = settings.getNearPlane();
        float farPlane = settings.getFarPlane();
        float fieldOfView = settings.getFieldOfView();

        float dx = SimpleMath.tan(SimpleMath.deg2rad(fieldOfView)) * (x/(width*0.5f) - 1f);
        float dy = SimpleMath.tan(SimpleMath.deg2rad(fieldOfView)) * (y/(height*0.5f) - 1f);

        // unviewMat = (projectionMat * modelViewMat).inverse()
        Vector3f start = new Vector3f(dx * nearPlane/m.m00, dy * nearPlane/m.m11, nearPlane/m.m22);
        Vector3f end = new Vector3f(dx * farPlane/m.m00, dy * farPlane/m.m11, farPlane/m.m22);

        Quaternion rot = avatar.getRotation();
        rot.negate(rot);
        SimpleMath.mul(rot, start, start);
        SimpleMath.mul(rot, end, end);
        rot.negate(rot);

        Vector3f pos = avatar.getPosition();
        start.x += pos.x;
        start.y += pos.y;
        start.z += pos.z;
        end.x += pos.x;
        end.y += pos.y;
        end.z += pos.z;

        return new PickingRay(start, end);
    }


    public void destroy() {

    }

}
