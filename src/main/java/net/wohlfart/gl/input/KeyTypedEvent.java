package net.wohlfart.gl.input;

public class KeyTypedEvent {
	protected int key;
	protected long time;

	public interface Listener {
		public abstract void keyEvent(KeyTypedEvent evt);
	}

	public KeyTypedEvent(int key) {
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
