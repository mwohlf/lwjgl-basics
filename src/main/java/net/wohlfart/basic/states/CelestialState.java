package net.wohlfart.basic.states;

import net.wohlfart.gl.Camera;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.input.KeyTypedEvent;
import net.wohlfart.model.Avatar;
import net.wohlfart.model.CelestialScene;
import net.wohlfart.widgets.Renderer;
import net.wohlfart.widgets.Screen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class CelestialState implements GameState {

	protected InputSource inputProcessor;
	protected CelestialScene scene;
	protected Camera camera;
	protected Avatar avatar;

	protected Renderer renderer;
	protected Screen widgetSet;

	protected boolean escPressed = false;


	CelestialState() {
	}


	// ui, sounds, cam

	@Override
	public void setup() {
		camera = new Camera();
		scene = new CelestialScene();
		avatar = new Avatar(camera, scene);
		avatar.setInputSource(InputSource.INSTANCE);
		InputSource.INSTANCE.register(new KeyTypedEvent.Listener() {
			@Override
			public void keyEvent(KeyTypedEvent evt) {
				if (evt.getKey() == Keyboard.KEY_ESCAPE) {
					escPressed = true;
				}
			}
		});
		widgetSet = new Screen();
		renderer = new Renderer();
	}


	@Override
	public void update(float tpf) {
		scene.update(tpf);
	}


	@Override
	public void render() {
		//GL11.glLoadIdentity();
		camera.lookThrough();
		scene.render();


		widgetSet.paint(renderer);
	}


	@Override
	public boolean isDone() {
		return Display.isCloseRequested() || escPressed;
	}


	@Override
	public void dispose() {
		scene.teardown();
	}


}
