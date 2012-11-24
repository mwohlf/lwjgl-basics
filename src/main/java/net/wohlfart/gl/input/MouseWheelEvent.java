package net.wohlfart.gl.input;

import net.wohlfart.gl.input.InputSource.MouseButton;

public class MouseWheelEvent {
	protected int wheel;
	protected long time;
	private MouseButton mouseButton;

	public interface Listener {
		public abstract void keyEvent(MouseWheelEvent evt);
	}

	public MouseWheelEvent(int wheel, MouseButton mouseButton) {
		this.wheel = wheel;
		this.time = System.nanoTime() / 100000;
		this.mouseButton = mouseButton;
	}

	public int getWheel() {
		return wheel;
	}

}
