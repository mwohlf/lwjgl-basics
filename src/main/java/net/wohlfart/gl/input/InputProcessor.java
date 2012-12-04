package net.wohlfart.gl.input;

public interface InputProcessor {

    // @formatter:off
    public enum Command {
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_UP,
        MOVE_DOWN,
        MOVE_FORWARD,
        MOVE_BACKWARD,
        MOVE_FORWARD_WHEEL,
        MOVE_BACKWARD_WHEEL,

        ROTATE_LEFT,
        ROTATE_RIGHT,
        ROTATE_UP,
        ROTATE_DOWN,
        ROTATE_CLOCKWISE,
        ROTATE_COUNTER_CLOCKWISE
    }



}
