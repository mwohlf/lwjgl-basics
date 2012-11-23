package net.wohlfart.model;

import net.wohlfart.gl.CanMove;
import net.wohlfart.gl.CanRotate;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.input.KeyPressedEvent;

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
				case Keyboard.KEY_LEFT:
					camera.rotate(speed, new Vector3f(0,1,0));//camera.getUp(new Vector3f()));
					break;
				case Keyboard.KEY_RIGHT:
					camera.rotate(-speed, new Vector3f(0,1,0));//camera.getUp(new Vector3f()));
					break;
				case Keyboard.KEY_UP:
					camera.rotate(speed, new Vector3f(1,0,0));//camera.getRght(new Vector3f()));
					break;
				case Keyboard.KEY_DOWN:
					camera.rotate(-speed, new Vector3f(1,0,0));//camera.getRght(new Vector3f()));
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
	};


	private void registerMoves() {
		final float speed = 0.8f;
		InputSource.INSTANCE.register(new KeyPressedEvent.Listener() {
			@Override
			public void keyEvent(KeyPressedEvent evt) {
				Vector3f pos = scene.getPos();  // this is actually the pos itself not a copy!
				switch (evt.getKey()) {
				case Keyboard.KEY_W:
					pos.z += speed;
					break;
				case Keyboard.KEY_Y:
					pos.z -= speed;
					break;
				case Keyboard.KEY_A:
					pos.x += speed;
					break;
				case Keyboard.KEY_S:
					pos.x -= speed;
					break;
				case Keyboard.KEY_Q:
					pos.y += speed;
					break;
				case Keyboard.KEY_X:
					pos.y -= speed;
					break;
				}
				scene.setPos(pos);

			}
		});
	}


}
