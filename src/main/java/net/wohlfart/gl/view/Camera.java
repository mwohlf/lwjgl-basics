package net.wohlfart.gl.view;

import net.wohlfart.gl.input.MoveEvent;
import net.wohlfart.gl.input.RotateEvent;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.eventbus.Subscribe;


public class Camera implements CanRotate, CanMove {
    private final Quaternion TMP_QUATERNION = new Quaternion();
    private final Vector3f TMP_VECTOR = new Vector3f();
    // reserve some memory for the render loop:  FIXME: need to be Thread local
    private final Vector3f posVector = new Vector3f();
    private final Matrix4f posMatrix = new Matrix4f();
    private final Matrix4f rotMatrix = new Matrix4f();


    private final CanRotate rotation;
    private final CanMove movement;

    public Camera() {
        this(new CanRotateImpl(), new CanMoveImpl());
    }

    private Camera(final CanRotate rotation, final CanMove movement) {
        this.rotation = rotation;
        this.movement = movement;
    }

    @Override
    public Quaternion getRotation() {
        return rotation.getRotation();
    }

    @Override
    public Vector3f getForward(Vector3f vec) {
        return rotation.getForward(vec);
    }

    @Override
    public Vector3f getUp(Vector3f vec) {
        return rotation.getUp(vec);
    }

    @Override
    public Vector3f getRght(Vector3f vec) {
        return rotation.getRght(vec);
    }

    @Override
    public Vector3f getPosition() {
        return movement.getPosition();
    }

    // --- moving

    @Subscribe
    public void move(MoveEvent evt) {
        Quaternion.negate(rotation.getRotation(), TMP_QUATERNION);
        SimpleMath.mul(TMP_QUATERNION, evt, TMP_VECTOR);
        Vector3f.add(movement.getPosition(), TMP_VECTOR, movement.getPosition());
    }

    @Override
    public void move(Vector3f vector) {
        movement.move(vector);
    }

    @Override
    public void setPosition(Vector3f vector) {
        movement.setPosition(vector);
    }

    // --- rotating

    @Subscribe
    public void rotate(RotateEvent evt) {
        Quaternion.mul(evt, rotation.getRotation(), rotation.getRotation());
    }

    @Override
    public void rotate(float deltaAngle, Vector3f axis) {
        rotation.rotate(deltaAngle, axis);
    }

    @Override
    public void setRotation(Quaternion quaternion) {
        rotation.setRotation(quaternion);
    }

    public Matrix4f getWorldToCamMatrix() {
        final Matrix4f rotPosMatrix = new Matrix4f();
        SimpleMath.convert(getPosition().negate(posVector), posMatrix);
        SimpleMath.convert(getRotation(), rotMatrix);
        Matrix4f.mul(rotMatrix, posMatrix, rotPosMatrix);
        return rotPosMatrix;
    }


    @Override
    public String toString() {
        final Vector3f pos = movement.getPosition();
        final Vector3f dir = rotation.getForward(new Vector3f());
        final Vector3f up = rotation.getUp(new Vector3f());
        final Vector3f rght = rotation.getRght(new Vector3f());// @formatter:off
        return ""
        + "Position: (" + pos.x + "," + pos.y + "," + pos.z + ") "
        + " Direction: (" + dir.x + "," + dir.y + "," + dir.z + ")"
        + " Up: (" + up.x + "," + up.y + "," + up.z + ")"
        + " Rght: (" + rght.x + "," + rght.y + "," + rght.z + ")"
        + " Direction.Up: " + Vector3f.dot(dir, up)
        + " Direction.Right: " + Vector3f.dot(dir, rght)
        + " Up.Right: " + Vector3f.dot(up, rght);
    }  // @formatter:on

}
