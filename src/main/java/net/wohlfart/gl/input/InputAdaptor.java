package net.wohlfart.gl.input;

/*
 * mapping low level user triggered events to high level commands
 * this is the union set of all possible input devices
 *
 *
 */
public interface InputAdaptor {

    KeyEventDispatcher getKeyboardKeyDevice();

    KeyEventDispatcher getMouseKeyDevice();

    AnalogEventDispatcher getMouseAnalogDevice();

    PositionEventDispatcher getMousePositionDevice();


    interface KeyEventDispatcher {

        void down(int key);

        void pressed(int key, float time);

        void up(int key);

    }

    interface AnalogEventDispatcher {

        void changed(int axis, int amout);
    }

    interface PositionEventDispatcher {

        void move(int posX, int posY);

    }

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
