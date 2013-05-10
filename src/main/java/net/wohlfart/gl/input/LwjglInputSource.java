package net.wohlfart.gl.input;

import java.util.HashSet;
import java.util.Iterator;

import net.wohlfart.basic.GenericGameException;
import net.wohlfart.gl.input.InputAdaptor.KeyEventDispatcher;
import net.wohlfart.gl.input.InputAdaptor.PositionEventDispatcher;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * <p>
 * LwjglInputSource class is the central input processor
 * for handling Mouse, Keyboard and Controllers.
 * </p>
 *
 */
public class LwjglInputSource implements InputSource {

    // the current keyboard state
    private final HashSet<Integer> pressedKeys = new HashSet<Integer>(8);
    private final HashSet<Integer> pressedButtons = new HashSet<Integer>(4);
    private Integer lastButton = 0;

    private KeyEventDispatcher keyboardDevice;
    private PositionEventDispatcher mouseDevice;


    // the lwjgl Display must be created before calling this method
    public void setInputDispatcher(InputDispatcher inputDispatcher) {
        try {
            InputAdaptor inputAdaptor = new LwjglInputAdaptor(inputDispatcher);
            keyboardDevice = inputAdaptor.getKeyboardDevice();
            mouseDevice = inputAdaptor.getMouseDevice();
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
        processMouseButtonChanges(delta);
        processMousePosition(delta);
        processMouseWheel(delta);
        processMouseMove(delta);
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
                pressedKeys.add(keyCode);
            } else {
                keyboardDevice.up(keyCode);
                pressedKeys.remove(keyCode);
            }
        }
    }

    // processing the current keyboard state
    private void processKeyboardState(float delta) {
        //Keyboard.poll();
        final Iterator<Integer> it = pressedKeys.iterator();
        while (it.hasNext()) {
            final int keyCode = it.next();
            if (Keyboard.isKeyDown(keyCode)) {
                keyboardDevice.pressed(keyCode, delta);
            } else {
                it.remove();
            }
        }
    }


    private void processMouseWheel(final float delta) {
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
                pressedButtons.add(buttonCode);
            } else {
                lastButton = 0;
                mouseDevice.up(buttonCode, x, y, delta);
                pressedButtons.remove(buttonCode);
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
        final Iterator<Integer> it = pressedButtons.iterator();
        mouseDevice.move(lastButton, deltaX, deltaY);
    }

}
