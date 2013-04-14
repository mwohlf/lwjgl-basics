package net.wohlfart.gl.input;


/*
 * mapping low level user triggered events to high level commands
 * this is the union set of all possible input devices
 *
 *
 */
/**
 * <p>InputAdaptor interface.</p>
 *
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

        void wheel(int amount);

        void down(int key, int x, int y, float time);

        void up(int key, int x, int y, float time);

        void position(int posX, int posY);

        void move(int key, int deltaX, int deltaY);

    }


    /**
     * <p>getKeyboardDevice.</p>
     *
     * @return a {@link net.wohlfart.gl.input.InputAdaptor.KeyEventDispatcher} object.
     */
    KeyEventDispatcher getKeyboardDevice();

    /**
     * <p>getMouseDevice.</p>
     *
     * @return a {@link net.wohlfart.gl.input.InputAdaptor.PositionEventDispatcher} object.
     */
    PositionEventDispatcher getMouseDevice();

    /**
     * <p>destroy.</p>
     */
    void destroy();

}
