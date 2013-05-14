package net.wohlfart.gl.view;

import net.wohlfart.gl.input.CommandEvent;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.eventbus.Subscribe;

/**
 * <p>Camera class.</p>
 */
public class Camera implements CanRotate, CanMove {
    // used for key triggered rotations, default rotation speed is one rotation per second
    private static final float ROT_SPEED = 0.1f;
    // used for key triggered moves, default move is 100 units per second
    private static final float MOVE_SPEED = 0.1f;

    private final CanRotate rotation;
    private final CanMove movement;

    /**
     * <p>Constructor for Camera.</p>
     */
    public Camera() {
        this(new CanRotateImpl(), new CanMoveImpl());
    }

    /**
     * <p>Constructor for Camera.</p>
     *
     * @param rotation a {@link net.wohlfart.gl.view.CanRotate} object.
     * @param movement a {@link net.wohlfart.gl.view.CanMove} object.
     */
    private Camera(final CanRotate rotation, final CanMove movement) {
        this.rotation = rotation;
        this.movement = movement;
    }

    /**
     * <p>Getter for the field <code>rotation</code>.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Quaternion} object.
     */
    @Override
    public Quaternion getRotation() {
        return rotation.getRotation();
    }

    /**
     * <p>getDir.</p>
     *
     * @param vec a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    @Override
    public Vector3f getDir(Vector3f vec) {
        return rotation.getDir(vec);
    }

    /**
     * <p>getUp.</p>
     *
     * @param vec a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    @Override
    public Vector3f getUp(Vector3f vec) {
        return rotation.getUp(vec);
    }

    /**
     * <p>getRght.</p>
     *
     * @param vec a {@link org.lwjgl.util.vector.Vector3f} object.
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    @Override
    public Vector3f getRght(Vector3f vec) {
        return rotation.getRght(vec);
    }

    /**
     * <p>getPosition.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    @Override
    public Vector3f getPosition() {
        return movement.getPosition();
    }

    /**
     * <p>moveForward.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.MoveForward} object.
     */
    @Subscribe
    public void moveForward(CommandEvent.MoveForward evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.sub(pos, (Vector3f) rotation.getDir(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    /**
     * <p>moveBackward.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.MoveBackward} object.
     */
    @Subscribe
    public void moveBackward(CommandEvent.MoveBackward evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.add(pos, (Vector3f) rotation.getDir(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    /**
     * <p>moveLeft.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.MoveLeft} object.
     */
    @Subscribe
    public void moveLeft(CommandEvent.MoveLeft evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.sub(pos, (Vector3f) rotation.getRght(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    /**
     * <p>moveRight.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.MoveRight} object.
     */
    @Subscribe
    public void moveRight(CommandEvent.MoveRight evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.add(pos, (Vector3f) rotation.getRght(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    /**
     * <p>moveUp.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.MoveUp} object.
     */
    @Subscribe
    public void moveUp(CommandEvent.MoveUp evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.sub(pos, (Vector3f) rotation.getUp(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    /**
     * <p>moveDown.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.MoveDown} object.
     */
    @Subscribe
    public void moveDown(CommandEvent.MoveDown evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.add(pos, (Vector3f) rotation.getUp(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    /**
     * <p>rotateDown.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.RotateDown} object.
     */
    @Subscribe
    public void rotateDown(CommandEvent.RotateDown evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(+1, 0, 0));
    }

    /**
     * <p>rotateUp.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.RotateUp} object.
     */
    @Subscribe
    public void rotateUp(CommandEvent.RotateUp evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(-1, 0, 0));
    }

    /**
     * <p>rotateRight.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.RotateRight} object.
     */
    @Subscribe
    public void rotateRight(CommandEvent.RotateRight evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(0, +1, 0));
    }

    /**
     * <p>rotateLeft.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.RotateLeft} object.
     */
    @Subscribe
    public void rotateLeft(CommandEvent.RotateLeft evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(0, -1, 0));
    }

    /**
     * <p>rotateClockwise.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.RotateClockwise} object.
     */
    @Subscribe
    public void rotateClockwise(CommandEvent.RotateClockwise evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(0, 0, +1));
    }

    /**
     * <p>rotateCounterClockwise.</p>
     *
     * @param evt a {@link net.wohlfart.gl.input.CommandEvent.RotateCounterClockwise} object.
     */
    @Subscribe
    public void rotateCounterClockwise(CommandEvent.RotateCounterClockwise evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(0, 0, -1));
    }

    @Override
    public void rotate(float deltaAngle, Vector3f axis) {
        rotation.rotate(deltaAngle, axis);
    }

    @Override
    public void setRotation(Quaternion quaternion) {
        rotation.setRotation(quaternion);
    }

    @Override
    public void move(Vector3f vector) {
        movement.move(vector);
    }

    @Override
    public void setPosition(Vector3f vector) {
        movement.setPosition(vector);
    }


    @Override
    public String toString() {
        Vector3f pos = movement.getPosition();
        Vector3f dir = rotation.getDir(new Vector3f());
        Vector3f up = rotation.getUp(new Vector3f());
        return ""
           + "Position: (" + pos.x + "," + pos.y + "," + pos.z + ") "
           + "Direction: (" + dir.x + "," + dir.y + "," + dir.z + ") Up: (" + up.x + "," + up.y + "," + up.z + ")";
    }


}
