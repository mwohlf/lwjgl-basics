package net.wohlfart.model;

import net.wohlfart.gl.CanMove;
import net.wohlfart.gl.CanRotate;
import net.wohlfart.gl.input.InputProcessor;
import net.wohlfart.gl.input.KeyPressedEvent;
import net.wohlfart.gl.input.MouseMotionEvent;
import net.wohlfart.gl.input.MouseWheelEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;


// this is a class that handles a canRotate and a canMove object
// FIXME: remove the dependency to the InputSource singleton and add
// configurable key/mouse actions instead of hardcoded ones
public class Avatar {
	// used for key triggered rotations
	private final float ROT_KEY_SPEED = 0.1f;
	// used for mouse movement triggered rotations
	private final float ROT_MOUSE_SPEED = 0.005f;
	// used for mouse wheel triggered moves
	private final float MOVE_WHEEL_SPEED = 0.005f;
	// used for key triggered moves
	private final float MOVE_KEY_SPEED = 0.5f;

	private InputProcessor inputSource;
	private final CanRotate rotation;
	private final CanMove position;

	public Avatar(final CanRotate rotation,
			      final CanMove position) {
		this.rotation = rotation;
		this.position = position;
	}


	public void setInputSource(InputProcessor inputSource) {
		this.inputSource = inputSource;
		registerMoves();
		registerRotations();
	}


	private void registerRotations() {
		inputSource.register(new KeyPressedEvent.Listener() {
			@Override
			public void keyEvent(KeyPressedEvent evt) {
				switch (evt.getKey()) {
				case Keyboard.KEY_UP:
					rotation.rotate(ROT_KEY_SPEED, new Vector3f(1,0,0));//camera.getRght(new Vector3f()));
					break;
				case Keyboard.KEY_DOWN:
					rotation.rotate(-ROT_KEY_SPEED, new Vector3f(1,0,0));//camera.getRght(new Vector3f()));
					break;
				case Keyboard.KEY_LEFT:
					rotation.rotate(ROT_KEY_SPEED, new Vector3f(0,1,0));//camera.getUp(new Vector3f()));
					break;
				case Keyboard.KEY_RIGHT:
					rotation.rotate(-ROT_KEY_SPEED, new Vector3f(0,1,0));//camera.getUp(new Vector3f()));
					break;
				case Keyboard.KEY_PRIOR:
					rotation.rotate(ROT_KEY_SPEED, new Vector3f(0,0,1));//camera.getDir(new Vector3f()));
					break;
				case Keyboard.KEY_NEXT:
					rotation.rotate(-ROT_KEY_SPEED, new Vector3f(0,0,1));//camera.getDir(new Vector3f()));
					break;
				}
			}
		});

		inputSource.register(new MouseMotionEvent.Listener() {
			@Override
			public void keyEvent(MouseMotionEvent evt) {
				if (evt.isLeftButtonPressed()) {
					int dx = evt.getDx();
					int dy = evt.getDy();
					rotation.rotate(-(float)dy * ROT_MOUSE_SPEED, new Vector3f(1,0,0));
					rotation.rotate(+(float)dx * ROT_MOUSE_SPEED, new Vector3f(0,1,0));
				}
			}
		});

		inputSource.register(new MouseWheelEvent.Listener() {
			@Override
			public void keyEvent(MouseWheelEvent evt) {
				int wheel = evt.getWheel();
				Vector3f pos = position.getPos();
				Vector3f move = rotation.getDir(new Vector3f());
				move.x *= wheel * MOVE_WHEEL_SPEED;
				move.y *= wheel * MOVE_WHEEL_SPEED;
				move.z *= wheel * MOVE_WHEEL_SPEED;
				Vector3f.sub(pos, move, pos);
				position.setPos(pos);
			}
		});
	};


	private void registerMoves() {
		inputSource.register(new KeyPressedEvent.Listener() {
			@Override
			public void keyEvent(KeyPressedEvent evt) {
				Vector3f pos = position.getPos();  // this is actually the pos itself not a copy!
				switch (evt.getKey()) {
				case Keyboard.KEY_W:
					Vector3f.sub(pos, (Vector3f)rotation.getDir(new Vector3f()).scale(MOVE_KEY_SPEED), pos);
					break;
				case Keyboard.KEY_Y:
					Vector3f.add(pos, (Vector3f)rotation.getDir(new Vector3f()).scale(MOVE_KEY_SPEED), pos);
					break;
				case Keyboard.KEY_A:
					Vector3f.sub(pos, (Vector3f)rotation.getRght(new Vector3f()).scale(MOVE_KEY_SPEED), pos);
					break;
				case Keyboard.KEY_S:
					Vector3f.add(pos, (Vector3f)rotation.getRght(new Vector3f()).scale(MOVE_KEY_SPEED), pos);
					break;
				case Keyboard.KEY_Q:
					Vector3f.add(pos, (Vector3f)rotation.getUp(new Vector3f()).scale(MOVE_KEY_SPEED), pos);
					break;
				case Keyboard.KEY_X:
					Vector3f.sub(pos, (Vector3f)rotation.getUp(new Vector3f()).scale(MOVE_KEY_SPEED), pos);
					break;
				}
				position.setPos(pos);
			}
		});
	}



}
