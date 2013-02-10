package net.wohlfart.gl.input;

public class MouseWheelEvent {
    protected int wheel;
    protected long time;
    private final MouseButton mouseButton;

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

    public MouseButton getMouseButton() {
        return mouseButton;
    }

}
