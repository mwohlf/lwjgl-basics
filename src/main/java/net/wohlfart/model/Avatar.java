package net.wohlfart.model;

import net.wohlfart.gl.CanMove;
import net.wohlfart.gl.CanRotate;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.input.KeyPressedEvent;
import net.wohlfart.gl.input.MouseMotionEvent;
import net.wohlfart.gl.input.MouseWheelEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Avatar {

	private final CanRotate camera;
	private final CanMove scene;

	public Avatar(final CanRotate camera, final CanMove scene) {
		this.camera = camera;
		this.scene = scene;

		registerMoves();
		registerRotations();
	}

	private void registerRotations() {
		final float speed = 0.01f;
		InputSource.INSTANCE.register(new KeyPressedEvent.Listener() {
			@Override
			public void keyEvent(KeyPressedEvent evt) {
				switch (evt.getKey()) {
				case Keyboard.KEY_UP:
					camera.rotate(speed, new Vector3f(1,0,0));//camera.getRght(new Vector3f()));
					break;
				case Keyboard.KEY_DOWN:
					camera.rotate(-speed, new Vector3f(1,0,0));//camera.getRght(new Vector3f()));
					break;
				case Keyboard.KEY_LEFT:
					camera.rotate(speed, new Vector3f(0,1,0));//camera.getUp(new Vector3f()));
					break;
				case Keyboard.KEY_RIGHT:
					camera.rotate(-speed, new Vector3f(0,1,0));//camera.getUp(new Vector3f()));
					break;
				case Keyboard.KEY_PRIOR:
					camera.rotate(speed, new Vector3f(0,0,1));//camera.getDir(new Vector3f()));
					break;
				case Keyboard.KEY_NEXT:
					camera.rotate(-speed, new Vector3f(0,0,1));//camera.getDir(new Vector3f()));
					break;
				}
				System.out.println(camera.toString());
			}
		});

		InputSource.INSTANCE.register(new MouseMotionEvent.Listener() {
			@Override
			public void keyEvent(MouseMotionEvent evt) {
				if (evt.isLeftButtonPressed()) {
					int dx = evt.getDx();
					int dy = evt.getDy();
					camera.rotate(-(float)dy/200f, new Vector3f(1,0,0));
					camera.rotate((float)dx/200f, new Vector3f(0,1,0));
				}
			}
		});

		InputSource.INSTANCE.register(new MouseWheelEvent.Listener() {
			@Override
			public void keyEvent(MouseWheelEvent evt) {
				int wheel = evt.getWheel();
				// camera.rotate(-(float)wheel/2000f, new Vector3f(0,0,1));
				Vector3f pos = scene.getPos();
				Vector3f move = camera.getDir(new Vector3f());
				move.x *= (float)wheel/10f;
				move.y *= (float)wheel/10f;
				move.z *= (float)wheel/10f;
				Vector3f.sub(pos, move, pos);
				scene.setPos(pos);
			}
		});

	};


	private void registerMoves() {
		final float speed = 0.8f;
		InputSource.INSTANCE.register(new KeyPressedEvent.Listener() {
			@Override
			public void keyEvent(KeyPressedEvent evt) {
				Vector3f pos = scene.getPos();  // this is actually the pos itself not a copy!
				switch (evt.getKey()) {
				case Keyboard.KEY_W:
					Vector3f.sub(pos, camera.getDir(new Vector3f()), pos);
					break;
				case Keyboard.KEY_Y:
					Vector3f.add(pos, camera.getDir(new Vector3f()), pos);
					break;
				case Keyboard.KEY_A:
					Vector3f.sub(pos, camera.getRght(new Vector3f()), pos);
					break;
				case Keyboard.KEY_S:
					Vector3f.add(pos, camera.getRght(new Vector3f()), pos);
					break;
				case Keyboard.KEY_Q:
					Vector3f.add(pos, camera.getUp(new Vector3f()), pos);
					break;
				case Keyboard.KEY_X:
					Vector3f.sub(pos, camera.getUp(new Vector3f()), pos);
					break;
				}
				scene.setPos(pos);

			}
		});
	}


}
