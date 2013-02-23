package net.wohlfart.gl.input;


/*
 * mapping low level user triggered events to high level commands
 * this is the union set of all possible input devices
 *
 *
 */
public interface InputAdaptor {

    interface KeyEventDispatcher {

        void down(int key);

        void pressed(int key, float time);

        void up(int key);

    }

    interface PositionEventDispatcher {

        void down(int key, int x, int y, float time);

        void up(int key, int x, int y, float time);

        void position(int posX, int posY);

    }


    KeyEventDispatcher getKeyboardDevice();

    PositionEventDispatcher getMouseDevice();


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
        ROTATE_COUNTER_CLOCKWISE,

        EXIT,
    }
    // @formatter:on

    void destroy();

}
