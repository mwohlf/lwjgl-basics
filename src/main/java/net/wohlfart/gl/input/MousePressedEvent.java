package net.wohlfart.gl.input;

public class MousePressedEvent {
	protected int key;
	protected long time;

	public interface Listener {
		public abstract void keyEvent(MousePressedEvent evt);
	}
	
	public MousePressedEvent(int key) {
		this.key = key;
		this.time = System.nanoTime() / 100000;
	}
}
