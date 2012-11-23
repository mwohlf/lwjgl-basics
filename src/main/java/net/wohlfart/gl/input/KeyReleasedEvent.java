package net.wohlfart.gl.input;

public class KeyReleasedEvent {
	protected int key;
	protected long time;

	interface Listener {
		public abstract void keyEvent(KeyReleasedEvent evt);
	}

	public KeyReleasedEvent(int key) {
		this.key = key;
		this.time = System.nanoTime() / 100000;
	}

	public int getKey() {
		return key;
	}

	public long getTime() {
		return time;
	}

}
