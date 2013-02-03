package net.wohlfart.gl.input;


/*
 * the high level commands
 */
public class CommandEvent {

	private int key;
	private float speed = 1;

	void setKey(int key) {
		this.key = key;
	}

	int getKey() {
		return key;
	}


	public float getSpeed() {
		return speed;
	}

    void setSpeed(float speed) {
		this.speed = speed;
	}





	// -- event classes
	public static class Null extends CommandEvent {
	}

	public static class Exit extends CommandEvent {
	}

	public static class TurnLeft extends CommandEvent {
	}

	public static class TurnRight extends CommandEvent {
	}

	public static class TurnUp extends CommandEvent {
	}

	public static class TurnDown extends CommandEvent {
	}

	public static class TurnClockwise extends CommandEvent {
	}

	public static class TurnCounterClockwise extends CommandEvent {
	}

	public static class MoveLeft extends CommandEvent {
	}

	public static class MoveRight extends CommandEvent {
	}

	public static class MoveUp extends CommandEvent {
	}

	public static class MoveDown extends CommandEvent {
	}

	public static class MoveForward extends CommandEvent {

	}

	public static class MoveBackward extends CommandEvent {
	}

}
