package net.wohlfart.gl.input;

import java.util.HashSet;
import java.util.Iterator;

import net.wohlfart.gl.input.InputAdaptor.DigitalEventDispatcher;

import org.lwjgl.input.Keyboard;


// central input processor for handling Mouse, Keyboard and Controllers
public class LwjglInputSource implements InputSource {

	private final InputAdaptor inputAdaptor;

	public LwjglInputSource (InputAdaptor inputAdaptor) {
		this.inputAdaptor = inputAdaptor;
		Keyboard.enableRepeatEvents(false);
	}

	// called from the main loop
	@Override
	public void createInputEvents(float delta)  {
		processKeyboardState(delta);
		processKeyboardChanges(delta);
		//processMouse(delta);
		//processControllers(delta);
	}

	private final HashSet<Integer> pressedKeys = new HashSet<Integer>();
	private void processKeyboardChanges(float delta) {
		DigitalEventDispatcher keyboardDevice = inputAdaptor.getKeyboardDigitalDevice();
		while (Keyboard.next()) {
			boolean pressed = Keyboard.getEventKeyState();
			int keyCode = Keyboard.getEventKey();
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
		DigitalEventDispatcher keyboardDevice = inputAdaptor.getKeyboardDigitalDevice();
		Keyboard.poll();
		Iterator<Integer> it = pressedKeys.iterator();
		while (it.hasNext()) {
			int keyCode = it.next();
			if (Keyboard.isKeyDown(keyCode)) {
				keyboardDevice.pressed(keyCode);
			} else {
				it.remove();
			}
		}
	}

/*
	private byte pressedButtons = 0;
	private void processMouse(final float delta) {
		while (Mouse.next()) {
			int buttonChanged = Mouse.getEventButton();
			if (buttonChanged != -1) {
				// a button changed
				boolean pressed = Mouse.getEventButtonState();
				if (pressed) {
//					inputProcessor.fire(new MousePressedEvent(buttonChanged));
					pressedButtons = (byte) (pressedButtons | (1 << buttonChanged));
				} else {
//					inputProcessor.fire(new MouseReleasedEvent(buttonChanged));
					pressedButtons = (byte) (pressedButtons & ((1 << buttonChanged) ^ 0xff));
				}
			};

			int wheel = Mouse.getDWheel();
			if (wheel != 0) {
//				inputProcessor.fire(new MouseWheelEvent(wheel, MouseButton.values()[buttonChanged + 1]));
			}
			int dx = Mouse.getDX();
			int dy = Mouse.getDY();
			if ((dx != 0) || (dy != 0)) {
//				inputProcessor.fire(new MouseMotionEvent(dx, dy, Mouse.getX(), Mouse.getY(), pressedButtons));
			}
		}
	}




	private void processControllers(final float delta) {

	}

*/
}
