package net.wohlfart.gl.input;

public class KeyPressedEvent {
	protected int key;
	protected long time;

	public interface Listener {
		public abstract void keyEvent(KeyPressedEvent evt);
	}

	public KeyPressedEvent(int key) {
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
