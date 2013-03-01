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
import org.lwjgl.util.vector.Vector4f;
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
    private Matrix4f modelViewMatrix;
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
        //System.out.println("new projection matrix: \n" + projectionMatrix);
        this.projectionMatrix = projectionMatrix;
    }

    void setModelViewMatrix(Matrix4f modelViewMatrix) {
        //System.out.println("new modelView matrix: \n" + modelViewMatrix);
        this.modelViewMatrix = modelViewMatrix;
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

    //  http://gamedev.stackexchange.com/questions/8974/converting-a-mouse-click-to-a-ray
    // http://gamedev.stackexchange.com/questions/8974/converting-a-mouse-click-to-a-ray
    public PickingRay createPickingRay(float x, float y) {
        System.out.println("------------createPickingRay for: " + x + "," + y + "------------");

        Matrix4f m = new Matrix4f();
        Matrix4f transformMatrix = new Matrix4f();
        Matrix4f.mul(projectionMatrix, modelViewMatrix, m);
        transformMatrix = Matrix4f.invert(m, transformMatrix);

        float width = settings.getWidth();
        float height = settings.getHeight();
        float nearPlane = settings.getNearPlane();
        float farPlane = settings.getFarPlane();
        float fieldOfView = settings.getFieldOfView();

        Vector4f cameraSpaceNear = new Vector4f(x / width * 2f - 1f, y / height * 2f - 1f, -1.0f, 1.0f);
        Vector4f cameraSpaceFar = new Vector4f(x / width * 2f - 1f, y / height * 2f - 1f,  1.0f, 1.0f);

        System.out.println("cameraSpaceNear: " + cameraSpaceNear);
        System.out.println("cameraSpaceFar: " + cameraSpaceFar);
        System.out.println("viewMatrix: \n" + modelViewMatrix);
        System.out.println("projectionMatrix: \n" + projectionMatrix);
        System.out.println("transformMatrix: \n" + transformMatrix);

        System.out.println("inverted: \n" + transformMatrix);
        Vector4f worldSpaceNear = new Vector4f();
        Matrix4f.transform(transformMatrix, cameraSpaceNear, worldSpaceNear);
        //Matrix4f.transform(SimpleMath.UNION_MATRIX, cameraSpaceNear, worldSpaceNear);

        Vector4f worldSpaceFar = new Vector4f();
        Matrix4f.transform(transformMatrix, cameraSpaceFar, worldSpaceFar);
        //Matrix4f.transform(SimpleMath.UNION_MATRIX, cameraSpaceFar, worldSpaceFar);

        //Vector3f start = new Vector3f(worldSpaceNear.x/m.m00, worldSpaceNear.y/m.m11, worldSpaceNear.z/m.m22);
        //Vector3f end = new Vector3f(worldSpaceFar.x/m.m00, worldSpaceFar.y/m.m11, worldSpaceFar.z/m.m22);
        //Vector3f start = new Vector3f(worldSpaceNear.x, worldSpaceNear.y, -nearPlane);
        //Vector3f end = new Vector3f(worldSpaceFar.x, worldSpaceFar.y, -farPlane);
        Vector3f start = new Vector3f(worldSpaceNear.x / worldSpaceNear.w,
                                      worldSpaceNear.y / worldSpaceNear.w,
                                      worldSpaceNear.z / worldSpaceNear.w);
        Vector3f end = new Vector3f(worldSpaceFar.x / worldSpaceFar.w,
                                    worldSpaceFar.y / worldSpaceFar.w,
                                    worldSpaceFar.z / worldSpaceFar.w);

        System.out.println("finalDraw: start: " + start + " end: " + end);

        return new PickingRay(start, end);
    }

    public PickingRay createPickingRayBroken(float x, float y, Avatar avatar) {
        Matrix4f m = projectionMatrix;
        float width = settings.getWidth();
        float height = settings.getHeight();
        float nearPlane = settings.getNearPlane();
        float farPlane = settings.getFarPlane();
        float fieldOfView = settings.getFieldOfView();

        float dx = SimpleMath.tan(SimpleMath.deg2rad(fieldOfView)) * (x/(width*0.5f) - 1f);
        float dy = SimpleMath.tan(SimpleMath.deg2rad(fieldOfView)) * (y/(height*0.5f) - 1f);

        // unviewMat = (projectionMat * modelViewMat).inverse()
        Vector4f cameraSpaceNear = new Vector4f(dx * nearPlane, dy * nearPlane, -nearPlane, 1);
        Vector4f cameraSpaceFar = new Vector4f(dx * farPlane, dy * farPlane, -farPlane, 1);

        Matrix4f tmpView = Matrix4f.mul(modelViewMatrix, projectionMatrix, new Matrix4f());
        Matrix4f invertedViewMatrix = (Matrix4f) tmpView.invert();

        Vector4f worldSpaceNear = new Vector4f();
        Matrix4f.transform(invertedViewMatrix, cameraSpaceNear, worldSpaceNear);

        Vector4f worldSpaceFar = new Vector4f();
        Matrix4f.transform(invertedViewMatrix, cameraSpaceFar, worldSpaceFar);

        Vector3f start = new Vector3f(worldSpaceNear.x, worldSpaceNear.y, worldSpaceNear.z);
        Vector3f end = new Vector3f(worldSpaceFar.x, worldSpaceFar.y, worldSpaceFar.z);

        return new PickingRay(start, end);
    }

    public PickingRay createPickingRayWorking(float x, float y, Avatar avatar) {
        System.out.println("------------createPickingRay for: " + x + "," + y + "------------");

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

        System.out.println("------------final vectors: " + start + "," + end + "------------");
        return new PickingRay(start, end);
    }

    public void destroy() {

    }

}
