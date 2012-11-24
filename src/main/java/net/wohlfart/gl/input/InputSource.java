package net.wohlfart.gl.input;

import java.util.Collection;
import java.util.HashSet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


// central input processor
public enum InputSource {
	INSTANCE;


	public byte pressedButtons = 0;


	private InputSource () {
		Keyboard.enableRepeatEvents(true);
	}

	private Collection<KeyPressedEvent.Listener> keyPressedListener = new HashSet<KeyPressedEvent.Listener>();
	private Collection<KeyReleasedEvent.Listener> keyReleasedListener = new HashSet<KeyReleasedEvent.Listener>();
	private Collection<KeyTypedEvent.Listener> keyTypedListener = new HashSet<KeyTypedEvent.Listener>();
	private Collection<MousePressedEvent.Listener> mousePressedListener = new HashSet<MousePressedEvent.Listener>();
	private Collection<MouseReleasedEvent.Listener> mouseReleasedListener = new HashSet<MouseReleasedEvent.Listener>();
	private Collection<MouseMotionEvent.Listener> mouseMotionListener = new HashSet<MouseMotionEvent.Listener>();
	private Collection<MouseWheelEvent.Listener> mouseWheelListener = new HashSet<MouseWheelEvent.Listener>();



	// called from the main loop
	public void process() {
		processKeyboard();
		processMouse();
	}


	// note that no new events are created as long as a key stays pressed
	private void processKeyboard() {
		while (Keyboard.next()) {
			boolean pressed = Keyboard.getEventKeyState();
			if (pressed) {
				fire(new KeyPressedEvent(Keyboard.getEventKey()));
			} else {
				fire(new KeyReleasedEvent(Keyboard.getEventKey()));
				fire(new KeyTypedEvent(Keyboard.getEventKey()));
			}
		}
	}



	private void processMouse() {
		while (Mouse.next()) {
			int buttonChanged = Mouse.getEventButton();
			if (buttonChanged != -1) {
				// a button changed
				boolean pressed = Mouse.getEventButtonState();
				if (pressed) {
					fire(new MousePressedEvent(buttonChanged));
					pressedButtons = (byte) (pressedButtons | (1 << buttonChanged));
				} else {
					fire(new MouseReleasedEvent(buttonChanged));
					pressedButtons = (byte) (pressedButtons & ((1 << buttonChanged) ^ 0xff));
				}
			};

			int wheel = Mouse.getDWheel();
			if (wheel != 0) {
				fire(new MouseWheelEvent(wheel, MouseButton.values()[buttonChanged + 1]));
			}
			int dx = Mouse.getDX();
			int dy = Mouse.getDY();
			if ((dx != 0) || (dy != 0)) {
				fire(new MouseMotionEvent(dx, dy, Mouse.getX(), Mouse.getY(), pressedButtons));
			}
		}
	}



	private void fire(MouseMotionEvent mouseMotionEvent) {
		for (MouseMotionEvent.Listener listener : mouseMotionListener) {
			listener.keyEvent(mouseMotionEvent);
		}
	}
	private void fire(MouseWheelEvent mouseWheelEvent) {
		for (MouseWheelEvent.Listener listener : mouseWheelListener) {
			listener.keyEvent(mouseWheelEvent);
		}
	}
	private void fire(MouseReleasedEvent event) {
		for (MouseReleasedEvent.Listener listener : mouseReleasedListener) {
			listener.keyEvent(event);
		}
	}
	private void fire(MousePressedEvent event) {
		for (MousePressedEvent.Listener listener : mousePressedListener) {
			listener.keyEvent(event);
		}
	}
	private void fire(final KeyPressedEvent event) {
		for (KeyPressedEvent.Listener listener : keyPressedListener) {
			listener.keyEvent(event);
		}
	}
	private void fire(final KeyReleasedEvent event) {
		for (KeyReleasedEvent.Listener listener : keyReleasedListener) {
			listener.keyEvent(event);
		}
	}
	private void fire(final KeyTypedEvent event) {
		for (KeyTypedEvent.Listener listener : keyTypedListener) {
			listener.keyEvent(event);
		}
	}



	public void register(final MouseReleasedEvent.Listener listener) {
		mouseReleasedListener.add(listener);
	}

	public void unregister(final MouseReleasedEvent.Listener listener) {
		mouseReleasedListener.remove(listener);
	}

	public void register(final MousePressedEvent.Listener listener) {
		mousePressedListener.add(listener);
	}

	public void unregister(final MousePressedEvent.Listener listener) {
		mousePressedListener.remove(listener);
	}

	public void register(final KeyPressedEvent.Listener listener) {
		keyPressedListener.add(listener);
	}

	public void unregister(final KeyPressedEvent.Listener listener) {
		keyPressedListener.remove(listener);
	}

	public void register(final KeyReleasedEvent.Listener listener) {
		keyReleasedListener.add(listener);
	}

	public void unregister(final KeyReleasedEvent.Listener listener) {
		keyReleasedListener.remove(listener);
	}

	public void register(final KeyTypedEvent.Listener listener) {
		keyTypedListener.add(listener);
	}

	public void unregister(final KeyTypedEvent.Listener listener) {
		keyTypedListener.remove(listener);
	}

	public void register(final MouseMotionEvent.Listener listener) {
		mouseMotionListener.add(listener);
	}

	public void unregister(final MouseMotionEvent.Listener listener) {
		mouseMotionListener.remove(listener);
	}

	public void register(final MouseWheelEvent.Listener listener) {
		mouseWheelListener.add(listener);
	}

	public void unregister(final MouseWheelEvent.Listener listener) {
		mouseWheelListener.remove(listener);
	}


}
