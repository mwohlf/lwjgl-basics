package net.wohlfart.gl.input;

import java.util.HashMap;

import net.wohlfart.gl.input.CommandEvent.KeybasedEvent;
import net.wohlfart.gl.input.CommandEvent.PositionEvent;
import net.wohlfart.gl.input.CommandEvent.PositionPointer;

import org.lwjgl.input.Keyboard;

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

    PositionPointer positionPointer = new PositionPointer();
    // @formatter:on

    public LwjglInputAdaptor(InputDispatcher inputDispatcher) {
        this.inputDispatcher = inputDispatcher;
    }

    private final KeyEventDispatcher keyboardDigitalDevice = new KeyEventDispatcher() {

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

    private final PositionEventDispatcher mouseDigitalDevice = new PositionEventDispatcher() {

        @Override
        public void up(int key, int x, int y, float time) {
        }

        @Override
        public void down(int key, int x, int y, float time) {
        }

        @Override
        public void position(int posX, int posY) {
            positionPointer.setPosition(posX, posY);
            inputDispatcher.post(positionPointer);
        }

    };



    @Override
    public KeyEventDispatcher getKeyboardDevice() {
        return keyboardDigitalDevice;
    }

    @Override
    public PositionEventDispatcher getMouseDevice() {
        return mouseDigitalDevice;
    }

    @Override
    public void destroy() {
        inputDispatcher.destroy();
    }

}
