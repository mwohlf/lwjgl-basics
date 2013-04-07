package net.wohlfart.gl.elements;

import net.wohlfart.gl.action.Action;
import net.wohlfart.gl.action.Action.Actor;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.renderer.IsUpdateable;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.view.CanMove;
import net.wohlfart.gl.view.CanMoveImpl;
import net.wohlfart.gl.view.CanRotate;
import net.wohlfart.gl.view.CanRotateImpl;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>Abstract AbstractRenderable class.</p>
 */
public abstract class AbstractRenderable implements IsRenderable, IsUpdateable, CanRotate, CanMove, Actor {

    // initial properties of the mesh
    protected final Vector3f translation = new Vector3f();
    protected final Quaternion rotation = new Quaternion();
    protected ReadableColor color = Color.BLUE;
    // a static mesh that is created lazy
    private IsRenderable delegate;

    // dynamic translation and rotation
    private boolean matrixIsOutdated = true;
    private final CanMoveImpl currentTranslation = new CanMoveImpl();
    private final CanRotateImpl currentRotation = new CanRotateImpl();
    private final Matrix4f modelToWorldMatrix = Matrix4f.setZero(new Matrix4f());
    private final Matrix3f normalMatrix = Matrix3f.setZero(new Matrix3f());

    private Action action = Action.NULL;


    /**
     * <p>This is the core method that needs to be implemented by subclasses,
     * use the  </p>
     *
     * @return a {@link net.wohlfart.gl.shader.mesh.IRenderable} object.
     */
    protected abstract IsRenderable setupMesh();

    /**
     * <p>This method has to be called to whenever something in the object data was changed
     *   in order to recreate the mesh data.</p>
     */
    protected void destroyMeshData() {
        delegate = null;
    }

    /**
     * <p>For setting up an initial translation, note this method is not
     * meant for moving this object around, it is rather used for setting
     * up an initial displacement of the object away from the origin.</p>
     *
     * @param translation a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link net.wohlfart.gl.elements.AbstractRenderable} object.
     */
    public AbstractRenderable withTranslation(Vector3f translation) {
        this.translation.set(translation);
        destroyMeshData();
        return this;
    }

    /**
     * <p>rotate.</p>
     *
     * @param rotation a {@link org.lwjgl.util.vector.Quaternion} object.
     * @return a {@link net.wohlfart.gl.elements.AbstractRenderable} object.
     */
    public AbstractRenderable withRotation(Quaternion rotation) {
        this.rotation.set(rotation);
        destroyMeshData();
        return this;
    }

    /**
     * <p>color.</p>
     *
     * @param color a {@link org.lwjgl.util.ReadableColor} object.
     * @return a {@link net.wohlfart.gl.elements.debug.AbstractRenderableWireframe} object.
     */
    public AbstractRenderable withColor(ReadableColor color) {
        this.color = color;
        destroyMeshData();
        return this;
    }

    /**
     * <p>Setter for the field <code>translation</code>.</p>
     *
     * @param currentTranslation a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    @Override
    public void setPosition(Vector3f vector) {
        this.currentTranslation.setPosition(vector);
        matrixIsOutdated = true;
    }

    /**
     * <p>Setter for the field <code>rotation</code>.</p>
     *
     * @param currentRotation a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    @Override
    public void setRotation(Quaternion quaternion) {
        this.currentRotation.setRotation(quaternion);
        matrixIsOutdated = true;
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        if (delegate == null) {
            delegate = setupMesh();
        }
        if (matrixIsOutdated) {
            SimpleMath.convert(currentTranslation, currentRotation, modelToWorldMatrix);
            SimpleMath.calculateNormalMatrix(modelToWorldMatrix, normalMatrix);
            matrixIsOutdated = false;
        }
        ShaderUniformHandle.MODEL_TO_WORLD.set(modelToWorldMatrix);
        ShaderUniformHandle.NORMAL.set(normalMatrix);

        delegate.render();
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
    }

    @Override
    public Vector3f getPosition() {
        return currentTranslation.getPosition();
    }

    @Override
    public void rotate(float deltaAngle, Vector3f axis) {
        currentRotation.rotate(deltaAngle, axis);
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
    public Vector3f getDir(Vector3f result) {
        return currentRotation.getDir(result);
    }

    @Override
    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public void update(float timeInSec) {
        action.perform(this, timeInSec);
    }

}
