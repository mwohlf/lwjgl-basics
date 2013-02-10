package net.wohlfart.gl.input;

public class MouseMotionEvent {
    protected int dx;
    protected int dy;
    protected long time;
    protected byte buttons;

    public interface Listener {
        public abstract void keyEvent(MouseMotionEvent evt);
    }

    public MouseMotionEvent(int dx, int dy, int x, int y, byte buttons) {
        this.dx = dx;
        this.dy = dy;
        this.time = System.nanoTime() / 100000;
        this.buttons = buttons;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public boolean isLeftButtonPressed() {
        return (buttons & (byte) ((1 << MouseButton.LEFT.ordinal()) & 0xff)) != 0;
    }

    public boolean isMiddleButtonPressed() {
        return (buttons & (byte) ((1 << MouseButton.MIDDLE.ordinal()) & 0xff)) != 0;
    }

    public boolean isRightButtonPressed() {
        return (buttons & (byte) ((1 << MouseButton.RIGHT.ordinal()) & 0xff)) != 0;
    }

}
