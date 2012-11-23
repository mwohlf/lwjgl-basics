package net.wohlfart.gl.input;

import java.util.Collection;
import java.util.HashSet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


// central input processor
public enum InputSource {
	INSTANCE;

	{
		Keyboard.enableRepeatEvents(true);
	}

	private Collection<KeyPressedEvent.Listener> keyPressedListener = new HashSet<KeyPressedEvent.Listener>();
	private Collection<KeyReleasedEvent.Listener> keyReleasedListener = new HashSet<KeyReleasedEvent.Listener>();
	private Collection<KeyTypedEvent.Listener> keyTypedListener = new HashSet<KeyTypedEvent.Listener>();



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
			//boolean pressed = Mouse.getEventButtonState();
			//Integer button = Mouse.getEventButton();
			// to be continued
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

}
