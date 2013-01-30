package net.wohlfart.gl.input;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;

import com.google.common.eventbus.EventBus;

public class InputProcessor extends EventBus {
	public static final String EVENTBUS_NAME = "inputProcessor";

	public interface InputSource {
		BitSet pressedButtons = new BitSet();
		void process(float delta, InputProcessor inputProcessor);
	}


	private final Collection<KeyPressedEvent.Listener> keyPressedListener = new HashSet<KeyPressedEvent.Listener>();
	private final Collection<KeyReleasedEvent.Listener> keyReleasedListener = new HashSet<KeyReleasedEvent.Listener>();
	private final Collection<KeyTypedEvent.Listener> keyTypedListener = new HashSet<KeyTypedEvent.Listener>();
	private final Collection<MousePressedEvent.Listener> mousePressedListener = new HashSet<MousePressedEvent.Listener>();
	private final Collection<MouseReleasedEvent.Listener> mouseReleasedListener = new HashSet<MouseReleasedEvent.Listener>();
	private final Collection<MouseMotionEvent.Listener> mouseMotionListener = new HashSet<MouseMotionEvent.Listener>();
	private final Collection<MouseWheelEvent.Listener> mouseWheelListener = new HashSet<MouseWheelEvent.Listener>();


    public enum Command {
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_UP,
        MOVE_DOWN,
        MOVE_FORWARD,
        MOVE_BACKWARD,
        MOVE_FORWARD_WHEEL,
        MOVE_BACKWARD_WHEEL,

        ROTATE_LEFT,
        ROTATE_RIGHT,
        ROTATE_UP,
        ROTATE_DOWN,
        ROTATE_CLOCKWISE,
        ROTATE_COUNTER_CLOCKWISE,

        EXIT,
    }

    private final InputMapper inputMapper;
	private final InputSource inputSource;


	public InputProcessor(InputSource inputSource) {
    	super(EVENTBUS_NAME);
    	inputMapper = new InputMapper();
    	this.inputSource = inputSource;
	}

	public void process(float delta) {
		inputSource.process(delta, this);
	}


	public void fire(MouseMotionEvent mouseMotionEvent) {
		for (MouseMotionEvent.Listener listener : mouseMotionListener) {
			listener.keyEvent(mouseMotionEvent);
		}
	}
	public void fire(MouseWheelEvent mouseWheelEvent) {
		for (MouseWheelEvent.Listener listener : mouseWheelListener) {
			listener.keyEvent(mouseWheelEvent);
		}
	}
	public void fire(MouseReleasedEvent event) {
		for (MouseReleasedEvent.Listener listener : mouseReleasedListener) {
			listener.keyEvent(event);
		}
	}
	public void fire(MousePressedEvent event) {
		for (MousePressedEvent.Listener listener : mousePressedListener) {
			listener.keyEvent(event);
		}
	}
	public void fire(final KeyPressedEvent event) {
		for (KeyPressedEvent.Listener listener : keyPressedListener) {
			listener.keyEvent(event);
		}
	}
	public void fire(final KeyReleasedEvent event) {
		for (KeyReleasedEvent.Listener listener : keyReleasedListener) {
			listener.keyEvent(event);
		}
	}
	public void fire(final KeyTypedEvent event) {
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
