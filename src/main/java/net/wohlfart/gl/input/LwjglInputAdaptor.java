package net.wohlfart.gl.input;

import java.util.HashMap;

import net.wohlfart.gl.input.CommandEvent.KeybasedEvent;
import net.wohlfart.gl.input.CommandEvent.PositionEvent;
import net.wohlfart.gl.input.CommandEvent.PositionPointer;

import org.lwjgl.input.Keyboard;

/**
 * <p>LwjglInputAdaptor class.</p>
 *
 *
 *
 */
public class LwjglInputAdaptor implements InputAdaptor {

    // event sink, parent component we need to post the events to
    private final InputDispatcher inputDispatcher;

    // core of the adaptor, mapping platform keys to command events, the events are reused
    // @formatter:off
    @SuppressWarnings("serial")
    private final HashMap<Integer, KeybasedEvent> keyMap = new HashMap<Integer, KeybasedEvent>() {{
            put(Keyboard.KEY_ESCAPE, new CommandEvent.Exit());
            // turning
            put(Keyboard.KEY_LEFT, new CommandEvent.RotateLeft());
            put(Keyboard.KEY_RIGHT, new CommandEvent.RotateRight());
            put(Keyboard.KEY_UP, new CommandEvent.RotateUp());
            put(Keyboard.KEY_DOWN, new CommandEvent.RotateDown());
            put(Keyboard.KEY_PRIOR, new CommandEvent.RotateClockwise());
            put(Keyboard.KEY_NEXT, new CommandEvent.RotateCounterClockwise());
            // moving
            put(Keyboard.KEY_W, new CommandEvent.MoveForward());
            put(Keyboard.KEY_Y, new CommandEvent.MoveBackward());
            put(Keyboard.KEY_A, new CommandEvent.MoveLeft());
            put(Keyboard.KEY_S, new CommandEvent.MoveRight());
            put(Keyboard.KEY_Q, new CommandEvent.MoveUp());
            put(Keyboard.KEY_X, new CommandEvent.MoveDown());
    }};

    @SuppressWarnings("serial")
    private final HashMap<Integer, PositionEvent> mouseMap = new HashMap<Integer, PositionEvent>() {{
        put(0, new CommandEvent.LeftClick());
        put(1, new CommandEvent.RightClick());
        put(2, new CommandEvent.MiddleClick());
    }};
    private final CommandEvent.MoveForward wheelForward =  new CommandEvent.MoveForward();
    private final CommandEvent.MoveBackward wheelBackward =  new CommandEvent.MoveBackward();


    PositionPointer positionPointer = new PositionPointer();
    // @formatter:on

    /**
     * <p>Constructor for LwjglInputAdaptor.</p>
     *
     * @param inputDispatcher a {@link net.wohlfart.gl.input.InputDispatcher} object.
     */
    public LwjglInputAdaptor(InputDispatcher inputDispatcher) {
        this.inputDispatcher = inputDispatcher;
    }

    private final KeyEventDispatcher keyboardDevice = new KeyEventDispatcher() {

        @Override
        public void down(int key) {

        }

        @Override
        public void up(int key) {
        }

        @Override
        public void pressed(int key, float time) {
            final KeybasedEvent command = keyMap.get(key);
            if (command != null) {
                command.setTime(time);
                inputDispatcher.post(command);
            }
        }

    };

    private final PositionEventDispatcher mouseDevice = new PositionEventDispatcher() {

        @Override
        public void wheel(int amount) {
            if (amount > 0) {
                wheelForward.setWheelAmount(amount);
                inputDispatcher.post(wheelForward);
            } else {
                wheelBackward.setWheelAmount(-amount);
                inputDispatcher.post(wheelBackward);
            }
        }

        @Override
        public void up(int key, int x, int y, float time) {
        }

        @Override
        public void down(int key, int x, int y, float time) {
            PositionEvent event = mouseMap.get(key);
            event.setPosition(x, y);
            inputDispatcher.post(event);
        }

        @Override
        public void position(int posX, int posY) {
            positionPointer.setPosition(posX, posY);
            inputDispatcher.post(positionPointer);
        }

    };



    /** {@inheritDoc} */
    @Override
    public KeyEventDispatcher getKeyboardDevice() {
        return keyboardDevice;
    }

    /** {@inheritDoc} */
    @Override
    public PositionEventDispatcher getMouseDevice() {
        return mouseDevice;
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        inputDispatcher.destroy();
    }

}
