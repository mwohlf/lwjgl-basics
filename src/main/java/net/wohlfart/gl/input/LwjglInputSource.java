package net.wohlfart.gl.input;

import java.util.HashSet;
import java.util.Iterator;

import net.wohlfart.basic.GenericGameException;
import net.wohlfart.gl.input.InputAdaptor.PositionEventDispatcher;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * <p>
 * LwjglInputSource class is the central input processor
 * for handling Mouse, Keyboard and Controllers for lwjgl
 * </p>
 *
 */
public class LwjglInputSource implements InputSource {

    // the current keyboard state

    private final KeyboardDevice keyboardDevice = new KeyboardDevice();
    private PositionEventDispatcher mouseDevice;
    private Integer lastButton = 0;

    private InputDispatcher inputDispatcher;



    // the lwjgl Display must be created before calling this method
    @Override
    public void setInputDispatcher(InputDispatcher inputDispatcher) {
        try {
            this.inputDispatcher = inputDispatcher;
            Keyboard.enableRepeatEvents(false);
            Mouse.create();
        } catch (LWJGLException ex) {
            throw new GenericGameException("can't create input source for lwjgl" , ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void createInputEvents(float delta) {
        processKeyboardChanges(delta);
        processKeyboardState(delta);
        /*
        processMouseButtonChanges(delta);
        processMousePosition(delta);
        processMouseWheel(delta);
        processMouseMove(delta);
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
        mouseDevice.wheel(deltaWheel);
    }


    private void processMouseButtonChanges(final float delta) {
        while (Mouse.next()) {
            final boolean pressed = Mouse.getEventButtonState();
            final int buttonCode = Mouse.getEventButton();
            final int x = Mouse.getEventX();
            final int y = Mouse.getEventY();
            if (pressed) {
                lastButton = buttonCode;
                mouseDevice.down(buttonCode, x, y, delta);
                //           pressedButtons.add(buttonCode);
            } else {
                lastButton = 0;
                mouseDevice.up(buttonCode, x, y, delta);
                //           pressedButtons.remove(buttonCode);
            }
        }
    }

    private void processMousePosition(float delta) {
        int screenX = Mouse.getX();
        int screenY = Mouse.getY();
        mouseDevice.position(screenX, screenY);
    }

    private void processMouseMove(float delta) {
        int deltaX = Mouse.getDX();
        int deltaY = Mouse.getDY();
        //final Iterator<Integer> it = pressedButtons.iterator();
        mouseDevice.move(lastButton, deltaX, deltaY);
    }




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
            //Keyboard.poll();
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
