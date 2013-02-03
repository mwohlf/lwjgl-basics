package net.wohlfart.gl.input;

import net.wohlfart.tools.SimpleMath;


/*
 * the high level commands
 */
public class CommandEvent {
	private static final float ROTATION_SPEED = SimpleMath.TWO_PI; // one rotation per sec
	private static final float MOVE_SPEED = 100f; // 100 units per sec


	private int key;
	private float delta;

	void setKey(int key) {
		this.key = key;
	}

	int getKey() {
		return key;
	}


	public void setTime(float time) {
		setDelta(1f * time);
	}

	public float getDelta() {
		return delta;
	}

    void setDelta(float delta) {
		this.delta = delta;
	}



    private static class RotationEvent extends CommandEvent {
		@Override
		public void setTime(float time) {
    		super.setDelta(ROTATION_SPEED * time);
    	}
    }

    private static class MoveEvent extends CommandEvent {
		@Override
		public void setTime(float time) {
    		super.setDelta(MOVE_SPEED * time);
    	}
    }




	// -- event classes
	public static class Null extends CommandEvent {
	}

	public static class Exit extends CommandEvent {
	}

	public static class RotateLeft extends RotationEvent {
	}

	public static class RotateRight extends RotationEvent {
	}

	public static class RotateUp extends RotationEvent {
	}

	public static class RotateDown extends RotationEvent {
	}

	public static class RotateClockwise extends RotationEvent {
	}

	public static class RotateCounterClockwise extends RotationEvent {
	}

	public static class MoveLeft extends MoveEvent {
	}

	public static class MoveRight extends MoveEvent {
	}

	public static class MoveUp extends MoveEvent {
	}

	public static class MoveDown extends MoveEvent {
	}

	public static class MoveForward extends MoveEvent {

	}

	public static class MoveBackward extends MoveEvent {
	}


}
