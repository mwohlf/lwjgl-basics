package net.wohlfart.gl.input;

import java.util.HashSet;
import java.util.Iterator;

import net.wohlfart.gl.input.InputAdaptor.KeyEventDispatcher;
import net.wohlfart.gl.input.InputAdaptor.PositionEventDispatcher;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

// central input processor for handling Mouse, Keyboard and Controllers
public class LwjglInputSource implements InputSource {

    private final InputAdaptor inputAdaptor;
    private final KeyEventDispatcher keyboardDevice;
    private final PositionEventDispatcher positionDevice;

    public LwjglInputSource(InputAdaptor inputAdaptor) {
        this.inputAdaptor = inputAdaptor;
        this.keyboardDevice = inputAdaptor.getKeyboardKeyDevice();
        this.positionDevice = inputAdaptor.getMousePositionDevice();

        Keyboard.enableRepeatEvents(false);
        try {
            Mouse.create();
        } catch (LWJGLException ex) {
            throw new RuntimeException("can't create input source for lwjgl" , ex);
        }
    }

    // called from the main loop
    @Override
    public void createInputEvents(float delta) {
        processKeyboardState(delta);
        processKeyboardChanges(delta);
        processMouse(delta);
        // processControllers(delta);
    }

    private final HashSet<Integer> pressedKeys = new HashSet<Integer>();

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

    private void processKeyboardState(float delta) {
        Keyboard.poll();
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

    @Override
    public void destroy() {
        inputAdaptor.destroy();
    }

    private void processMouse(final float delta) {
        int screenX = Mouse.getX();
        int screenY = Mouse.getY();
        positionDevice.move(screenX, screenY);
    }

}
