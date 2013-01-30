package net.wohlfart.gl.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


// central input processor for handling Mouse, Keyboard and Controllers
public class DefaultLwjglInputSource implements InputProcessor.InputSource {

	public DefaultLwjglInputSource () {
		Keyboard.enableRepeatEvents(false);
	}


	// called from the main loop
	@Override
	public void process(float delta, InputProcessor inputProcessor) {
		processKeyboard(delta, inputProcessor);
		processMouse(delta, inputProcessor);
		processControllers(delta, inputProcessor);
	}


	private void processKeyboard(final float delta, InputProcessor inputProcessor) {
		while (Keyboard.next()) {
			boolean pressed = Keyboard.getEventKeyState();
			if (pressed) {
				inputProcessor.fire(new KeyPressedEvent(Keyboard.getEventKey()));
			} else {
				inputProcessor.fire(new KeyReleasedEvent(Keyboard.getEventKey()));
				inputProcessor.fire(new KeyTypedEvent(Keyboard.getEventKey()));
			}
		}
		// Keyboard.poll();
		// Keyboard.is
	}


	private byte pressedButtons = 0;
	private void processMouse(final float delta, InputProcessor inputProcessor) {
		while (Mouse.next()) {
			int buttonChanged = Mouse.getEventButton();
			if (buttonChanged != -1) {
				// a button changed
				boolean pressed = Mouse.getEventButtonState();
				if (pressed) {
					inputProcessor.fire(new MousePressedEvent(buttonChanged));
					pressedButtons = (byte) (pressedButtons | (1 << buttonChanged));
				} else {
					inputProcessor.fire(new MouseReleasedEvent(buttonChanged));
					pressedButtons = (byte) (pressedButtons & ((1 << buttonChanged) ^ 0xff));
				}
			};

			int wheel = Mouse.getDWheel();
			if (wheel != 0) {
				inputProcessor.fire(new MouseWheelEvent(wheel, MouseButton.values()[buttonChanged + 1]));
			}
			int dx = Mouse.getDX();
			int dy = Mouse.getDY();
			if ((dx != 0) || (dy != 0)) {
				inputProcessor.fire(new MouseMotionEvent(dx, dy, Mouse.getX(), Mouse.getY(), pressedButtons));
			}
		}
	}




	private void processControllers(final float delta, InputProcessor inputProcessor) {

	}

}
