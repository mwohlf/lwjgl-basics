package net.wohlfart.gl.elements;

import net.wohlfart.basic.action.SpatialActor;
import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.SpatialEntity;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.view.CanMoveImpl;
import net.wohlfart.gl.view.CanRotateImpl;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * Abstract AbstractRenderable class.
 */
public abstract class AbstractRenderable implements SpatialEntity {

    private final CanMoveImpl currentTranslation = new CanMoveImpl();
    private final CanRotateImpl currentRotation = new CanRotateImpl();
    private final Matrix4f modelToWorldMatrix = Matrix4f.setZero(new Matrix4f());
    private final Matrix3f normalMatrix = Matrix3f.setZero(new Matrix3f());

    private Action currentAction = SpatialActor.NULL_ACTION;

    // a static mesh that is created lazy
    private IsRenderable delegate;

    private boolean reCreateMatrice = true;
    private boolean reCreateRenderable = true;

    // initial properties of this mesh
    protected final Vector3f initialTranslation = new Vector3f();
    protected final Quaternion initialRotation = new Quaternion();
    protected ReadableColor color = Color.BLUE;

    /**
     * <p>
     * This is the core method that needs to be implemented by subclasses.
     * </p>
     *
     * @return a {@link net.wohlfart.gl.shader.mesh.IRenderable} object.
     */
    protected abstract IsRenderable setupMesh();

    /**
     * <p>
     * This method has to be called to whenever something in the object data was changed in order to recreate the mesh data.
     * </p>
     */
    protected void destroyMeshData() {
        delegate = null;
    }

    /**
     * <p>
     * For setting up an initial translation, note this method is not meant for moving this object around, it is rather used for setting up an initial
     * displacement of the object away from the origin.
     * </p>
     *
     * @param initialTranslation
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link net.wohlfart.gl.elements.AbstractRenderable} object.
     */
    public AbstractRenderable withTranslation(Vector3f newTranslation) {
        initialTranslation.set(newTranslation);
        reCreateRenderable = true;
        return this;
    }

    /**
     * <p>
     * rotate.
     * </p>
     *
     * @param initialRotation
     *            a {@link org.lwjgl.util.vector.Quaternion} object.
     * @return a {@link net.wohlfart.gl.elements.AbstractRenderable} object.
     */
    public AbstractRenderable withRotation(Quaternion newRotation) {
        initialRotation.set(newRotation);
        reCreateRenderable = true;
        return this;
    }

    /**
     * <p>
     * color.
     * </p>
     *
     * @param color
     *            a {@link org.lwjgl.util.ReadableColor} object.
     * @return a {@link net.wohlfart.gl.elements.debug.AbstractRenderableWireframe} object.
     */
    public AbstractRenderable withColor(ReadableColor color) {
        this.color = color;
        reCreateRenderable = true;
        return this;
    }

    /**
     * <p>
     * Setter for the field <code>translation</code>.
     * </p>
     *
     * @param currentTranslation
     *            a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    @Override
    public void setPosition(Vector3f vector) {
        this.currentTranslation.setPosition(vector);
        reCreateMatrice = true;
    }

    /**
     * <p>
     * Setter for the field <code>rotation</code>.
     * </p>
     *
     * @param currentRotation
     *            a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    @Override
    public void setRotation(Quaternion quaternion) {
        this.currentRotation.setRotation(quaternion);
        reCreateMatrice = true;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        if (reCreateMatrice) {
            SimpleMath.convert(currentTranslation, currentRotation, modelToWorldMatrix);
            SimpleMath.calculateNormalMatrix(modelToWorldMatrix, normalMatrix);
            reCreateMatrice = false;
        }
        ShaderUniformHandle.MODEL_TO_WORLD.set(modelToWorldMatrix);
        ShaderUniformHandle.NORMAL_MATRIX.set(normalMatrix);

        if (reCreateRenderable) {
            delegate = setupMesh();
            reCreateRenderable = false;
        }
        delegate.render();
    }

    protected void reCreateRenderable(boolean reCreateRenderable) {
        this.reCreateRenderable = reCreateRenderable;
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        delegate.destroy();
        delegate = null;
    }

    @Override
    public void move(Vector3f vector) {
        currentTranslation.move(vector);
        reCreateMatrice = true;
    }

    @Override
    public Vector3f getPosition() {
        return currentTranslation.getPosition();
    }

    @Override
    public void rotate(float deltaAngle, Vector3f axis) {
        currentRotation.rotate(deltaAngle, axis);
        reCreateMatrice = true;
    }

    @Override
    public Quaternion getRotation() {
        return currentRotation.getRotation();
    }

    @Override
    public Vector3f getRght(Vector3f vector3f) {
        return currentRotation.getRght(vector3f);
    }

    @Override
    public Vector3f getUp(Vector3f result) {
        return currentRotation.getUp(result);
    }

    @Override
    public Vector3f getForward(Vector3f result) {
        return currentRotation.getForward(result);
    }

    @Override
    public void setAction(Action action) {
        this.currentAction = action;
    }

    @Override
    public void update(float timeInSec) {
        currentAction.perform(this, timeInSec);
    }

}
