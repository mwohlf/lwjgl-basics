package net.wohlfart.gl.input;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;


public class LwjglInputAdaptor implements InputAdaptor {

	// event sink, parent component to post events to
	private final InputDispatcher inputDispatcher;


	// core of the adaptor, mapping platform keys to command events
	@SuppressWarnings("serial")
	private final HashMap<Integer, CommandEvent> keyMap = new HashMap<Integer, CommandEvent>() {{
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


	public LwjglInputAdaptor(InputDispatcher inputDispatcher) {
		this.inputDispatcher = inputDispatcher;
	}



	private final DigitalEventDispatcher keyboardDigitalDevice = new DigitalEventDispatcher() {

		@Override
		public void down(int key) {
		}

		@Override
		public void up(int key) {
		}

		@Override
		public void pressed(int key, float time) {
			CommandEvent command = keyMap.get(key);
			if (command != null) {
				command.setTime(time);
				inputDispatcher.post(command);
			}
		}

	};

	private final DigitalEventDispatcher mouseDigitalDevice = new DigitalEventDispatcher() {

		@Override
		public void down(int key) {
		}

		@Override
		public void up(int key) {
		}

		@Override
		public void pressed(int key, float time) {
		}

	};


	private final AnalogEventDispatcher mouseAnalogDevice = new AnalogEventDispatcher() {

		@Override
		public void changed(int axis, int amout) {
			//inputDispatcher
		}

	};


	@Override
	public DigitalEventDispatcher getKeyboardDigitalDevice() {
		return keyboardDigitalDevice;
	}

	@Override
	public DigitalEventDispatcher getMouseDigitalDevice() {
		return mouseDigitalDevice;
	}

	@Override
	public AnalogEventDispatcher getMouseAnalogDevice() {
		return mouseAnalogDevice;
	}

}
