package net.wohlfart.gl.input;

import java.util.HashSet;
import java.util.Iterator;

import net.wohlfart.basic.GenericGameException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * LwjglInputSource class is the central input processor for handling Mouse, Keyboard and Controllers for lwjgl
 */
public class LwjglInputSource implements InputSource {

    // the current keyboard state
    private final KeyboardDevice keyboardDevice = new KeyboardDevice();
    private final MouseDevice mouseDevice = new MouseDevice();
    private final Integer lastButton = 0;

    private InputDispatcher inputDispatcher;

    // the lwjgl Display must be created before calling this method
    @Override
    public void setInputDispatcher(InputDispatcher inputDispatcher) {
        try {
            this.inputDispatcher = inputDispatcher;
            Keyboard.enableRepeatEvents(false);
            Mouse.create();
        } catch (final LWJGLException ex) {
            throw new GenericGameException("can't create input source for lwjgl", ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void createInputEvents(float delta) {
        processKeyboardChanges(delta);
        processKeyboardState(delta);
        processMouseWheel(delta);
        processMouseButtonChanges(delta);

        /*
         * processMousePosition(delta); processMouseMove(delta);
         */
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {

    }

    // processing the keyboard events from the queue
    private void processKeyboardChanges(float delta) {
        while (Keyboard.next()) {
            final boolean pressed = Keyboard.getEventKeyState();
            final int keyCode = Keyboard.getEventKey();
            if (pressed) {
                keyboardDevice.down(keyCode);
            } else {
                keyboardDevice.up(keyCode);
            }
        }
    }

    // processing the current keyboard state
    private void processKeyboardState(float delta) {
        keyboardDevice.processPressedKeys(delta);
    }

    private void processMouseWheel(float delta) {
        final int deltaWheel = Mouse.getDWheel();
        mouseDevice.wheel(delta, deltaWheel);
    }

    private void processMouseButtonChanges(final float delta) {
        while (Mouse.next()) {
            final boolean pressed = Mouse.getEventButtonState();
            final int buttonCode = Mouse.getEventButton();
            final int x = Mouse.getEventX();
            final int y = Mouse.getEventY();
            if (pressed) {
                mouseDevice.down(buttonCode, x, y, delta);
            } else {
                mouseDevice.up(buttonCode, x, y, delta);
            }
        }
    }

    private void processMousePosition(float delta) {
        final int screenX = Mouse.getX();
        final int screenY = Mouse.getY();
        mouseDevice.position(screenX, screenY);
    }

    private void processMouseMove(float delta) {
        final int deltaX = Mouse.getDX();
        final int deltaY = Mouse.getDY();
        // final Iterator<Integer> it = pressedButtons.iterator();
        mouseDevice.move(lastButton, deltaX, deltaY);
    }

    private class MouseDevice {
        private static final int MOUSE_KEY0 = 0;
        private static final int MOUSE_KEY1 = 1;
        private static final int MOUSE_KEY2 = 2;

        public void wheel(float delta, int amount) {
            inputDispatcher.post(MoveEvent.wheel(delta, amount));
        }

        public void up(int key, int x, int y, float time) {
        }

        public void down(int key, int x, int y, float time) {
            switch (key) {
            case MOUSE_KEY0:
                inputDispatcher.post(PointEvent.create(x, y, PointEvent.Key.PICK));
                break;
            case MOUSE_KEY1:
                break;
            case MOUSE_KEY2:
                inputDispatcher.post(PointEvent.create(x, y, PointEvent.Key.CONTEXT));
                break;
            }
        }

        public void position(int posX, int posY) {
            /*
             * positionPointer.setPosition(posX, posY); inputDispatcher.post(positionPointer);
             */
        }

        public void move(int key, int deltaX, int deltaY) {
            switch (key) {
            case MOUSE_KEY0:
                break;
            case MOUSE_KEY1:
                break;
            case MOUSE_KEY2:
                break;
            }
        }

    };

    private class KeyboardDevice {
        private final HashSet<Integer> pressedKeys = new HashSet<Integer>(8);

        public void down(int key) {
            pressedKeys.add(key);
            switch (key) {
            case Keyboard.KEY_ESCAPE:
                inputDispatcher.post(CommandEvent.exit());
                break;
            default:
                // do nothing
            }
        }

        public void up(int key) {
            pressedKeys.remove(key);

        }

        public void processPressedKeys(float time) {
            // Keyboard.poll();
            final Iterator<Integer> it = pressedKeys.iterator();
            while (it.hasNext()) {
                final int keyCode = it.next();
                if (Keyboard.isKeyDown(keyCode)) {
                    processPressedKey(time, keyCode);
                } else {
                    it.remove();
                }
            }
        }

        private void processPressedKey(float time, int key) {

            switch (key) {
            case Keyboard.KEY_W:
                inputDispatcher.post(MoveEvent.moveForward(time));
                break;
            case Keyboard.KEY_Y:
                inputDispatcher.post(MoveEvent.moveBack(time));
                break;
            case Keyboard.KEY_A:
                inputDispatcher.post(MoveEvent.moveLeft(time));
                break;
            case Keyboard.KEY_S:
                inputDispatcher.post(MoveEvent.moveRight(time));
                break;
            case Keyboard.KEY_Q:
                inputDispatcher.post(MoveEvent.moveUp(time));
                break;
            case Keyboard.KEY_X:
                inputDispatcher.post(MoveEvent.moveDown(time));
                break;
            case Keyboard.KEY_LEFT:
                inputDispatcher.post(RotateEvent.rotateLeft(time));
                break;
            case Keyboard.KEY_RIGHT:
                inputDispatcher.post(RotateEvent.rotateRight(time));
                break;
            case Keyboard.KEY_UP:
                inputDispatcher.post(RotateEvent.rotateUp(time));
                break;
            case Keyboard.KEY_DOWN:
                inputDispatcher.post(RotateEvent.rotateDown(time));
                break;
            case Keyboard.KEY_PRIOR:
                inputDispatcher.post(RotateEvent.rotateClockwise(time));
                break;
            case Keyboard.KEY_NEXT:
                inputDispatcher.post(RotateEvent.rotateCounterClockwise(time));
                break;
            default:
                // do nothing
            }
        }

    };

}
