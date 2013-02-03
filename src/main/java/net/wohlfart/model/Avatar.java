package net.wohlfart.model;

import net.wohlfart.gl.CanMove;
import net.wohlfart.gl.CanMoveImpl;
import net.wohlfart.gl.CanRotate;
import net.wohlfart.gl.CanRotateImpl;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.shader.GraphicContextManager;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.eventbus.Subscribe;


public class Avatar {
	// used for key triggered rotations
	private final float ROT_SPEED = 0.001f;
	// used for key triggered moves
	private final float MOVE_SPEED = 0.005f;

	private final CanRotate rotation;
	private final CanMove movement;


	public Avatar() {
		this(new CanRotateImpl(), new CanMoveImpl());
	}

	public Avatar(final CanRotate rotation,
			      final CanMove movement) {
		this.rotation = rotation;
		this.movement = movement;
	}


	public Quaternion getRotation() {
		return rotation.getRotation();
	}

	public Vector3f getPosition() {
		return movement.getPosition();
	}


	public void setup() {
		GraphicContextManager.INSTANCE.getInputDispatcher().register(this);
	}

	public void dispose() {
		GraphicContextManager.INSTANCE.getInputDispatcher().unregister(this);
	}


	@Subscribe
	public void moveForward(CommandEvent.MoveForward evt) {
		Vector3f pos = movement.getPosition();
		Vector3f.sub(pos, (Vector3f)rotation.getDir(new Vector3f()).scale(evt.getSpeed() * MOVE_SPEED), pos);
	}

	@Subscribe
	public void moveBackward(CommandEvent.MoveBackward evt) {
		Vector3f pos = movement.getPosition();
		Vector3f.add(pos, (Vector3f)rotation.getDir(new Vector3f()).scale(evt.getSpeed() * MOVE_SPEED), pos);
	}

	@Subscribe
	public void moveLeft(CommandEvent.MoveLeft evt) {
		Vector3f pos = movement.getPosition();
		Vector3f.sub(pos, (Vector3f)rotation.getRght(new Vector3f()).scale(evt.getSpeed() * MOVE_SPEED), pos);
	}

	@Subscribe
	public void moveRight(CommandEvent.MoveRight evt) {
		Vector3f pos = movement.getPosition();
		Vector3f.add(pos, (Vector3f)rotation.getRght(new Vector3f()).scale(evt.getSpeed() * MOVE_SPEED), pos);
	}

	@Subscribe
	public void moveUp(CommandEvent.MoveUp evt) {
		Vector3f pos = movement.getPosition();
		Vector3f.sub(pos, (Vector3f)rotation.getUp(new Vector3f()).scale(evt.getSpeed() * MOVE_SPEED), pos);
	}

	@Subscribe
	public void moveDown(CommandEvent.MoveDown evt) {
		Vector3f pos = movement.getPosition();
		Vector3f.add(pos, (Vector3f)rotation.getUp(new Vector3f()).scale(evt.getSpeed() * MOVE_SPEED), pos);
	}


	@Subscribe
	public void rotateUp(CommandEvent.TurnUp evt) {
		rotation.rotate(evt.getSpeed() * ROT_SPEED, new Vector3f(+1,0,0));
	}

	@Subscribe
	public void rotateDown(CommandEvent.TurnDown evt) {
		rotation.rotate(evt.getSpeed() * ROT_SPEED, new Vector3f(-1,0,0));
	}

	@Subscribe
	public void rotateLeft(CommandEvent.TurnLeft evt) {
		rotation.rotate(evt.getSpeed() * ROT_SPEED, new Vector3f(0,+1,0));
	}

	@Subscribe
	public void rotateRight(CommandEvent.TurnRight evt) {
		rotation.rotate(evt.getSpeed() * ROT_SPEED, new Vector3f(0,-1,0));
	}

	@Subscribe
	public void rotateClockwise(CommandEvent.TurnClockwise evt) {
		rotation.rotate(evt.getSpeed() * ROT_SPEED, new Vector3f(0,0,+1));
	}

	@Subscribe
	public void rotateCounterClockwise(CommandEvent.TurnCounterClockwise evt) {
		rotation.rotate(evt.getSpeed() * ROT_SPEED, new Vector3f(0,0,-1));
	}

}
