package net.wohlfart.model;

import net.wohlfart.gl.CanMove;
import net.wohlfart.gl.CanMoveImpl;
import net.wohlfart.gl.CanRotate;
import net.wohlfart.gl.CanRotateImpl;
import net.wohlfart.gl.input.CommandEvent;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.eventbus.Subscribe;

public class Avatar {
    // used for key triggered rotations, default rotation speed is one rotation per second
    private static final float ROT_SPEED = 0.1f;
    // used for key triggered moves, default move is 100 units per second
    private static final float MOVE_SPEED = 0.1f;

    private final CanRotate rotation;
    private final CanMove movement;

    public Avatar() {
        this(new CanRotateImpl(), new CanMoveImpl());
    }

    public Avatar(final CanRotate rotation, final CanMove movement) {
        this.rotation = rotation;
        this.movement = movement;
    }

    public Vector3f readDirection(Vector3f vector) {
        return rotation.getDir(vector);
    }

    public Quaternion getRotation() {
        return rotation.getRotation();
    }

    public Vector3f getDir(Vector3f vec) {
        return rotation.getDir(vec);
    }

    public Vector3f getUp(Vector3f vec) {
        return rotation.getUp(vec);
    }

    public Vector3f getRght(Vector3f vec) {
        return rotation.getRght(vec);
    }

    public Vector3f getPosition() {
        return movement.getPosition();
    }

    @Subscribe
    public void moveForward(CommandEvent.MoveForward evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.sub(pos, (Vector3f) rotation.getDir(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    @Subscribe
    public void moveBackward(CommandEvent.MoveBackward evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.add(pos, (Vector3f) rotation.getDir(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    @Subscribe
    public void moveLeft(CommandEvent.MoveLeft evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.sub(pos, (Vector3f) rotation.getRght(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    @Subscribe
    public void moveRight(CommandEvent.MoveRight evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.add(pos, (Vector3f) rotation.getRght(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    @Subscribe
    public void moveUp(CommandEvent.MoveUp evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.sub(pos, (Vector3f) rotation.getUp(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    @Subscribe
    public void moveDown(CommandEvent.MoveDown evt) {
        final Vector3f pos = movement.getPosition();
        Vector3f.add(pos, (Vector3f) rotation.getUp(new Vector3f()).scale(evt.getDelta() * MOVE_SPEED), pos);
    }

    @Subscribe
    public void rotateDown(CommandEvent.RotateDown evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(+1, 0, 0));
    }

    @Subscribe
    public void rotateUp(CommandEvent.RotateUp evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(-1, 0, 0));
    }

    @Subscribe
    public void rotateRight(CommandEvent.RotateRight evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(0, +1, 0));
    }

    @Subscribe
    public void rotateLeft(CommandEvent.RotateLeft evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(0, -1, 0));
    }

    @Subscribe
    public void rotateClockwise(CommandEvent.RotateClockwise evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(0, 0, +1));
    }

    @Subscribe
    public void rotateCounterClockwise(CommandEvent.RotateCounterClockwise evt) {
        rotation.rotate(evt.getDelta() * ROT_SPEED, new Vector3f(0, 0, -1));
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
