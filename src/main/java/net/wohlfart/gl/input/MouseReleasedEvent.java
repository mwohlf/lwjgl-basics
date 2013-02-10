package net.wohlfart.gl.input;

public class MouseReleasedEvent {
    protected int key;
    protected long time;

    public interface Listener {
        public abstract void keyEvent(MouseReleasedEvent evt);
    }

    public MouseReleasedEvent(int key) {
        this.key = key;
        this.time = System.nanoTime() / 100000;
    }
}
