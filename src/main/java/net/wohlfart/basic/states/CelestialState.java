package net.wohlfart.basic.states;

import net.wohlfart.gl.Camera;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.input.LwjglInputSource;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.model.Avatar;
import net.wohlfart.model.CelestialScene;
import net.wohlfart.widgets.Renderer;
import net.wohlfart.widgets.Screen;

import org.lwjgl.opengl.Display;

import com.google.common.eventbus.Subscribe;

class CelestialState implements GameState {

	protected LwjglInputSource inputProcessor;
	protected CelestialScene scene = new CelestialScene();
	protected Camera camera = new Camera();
	protected Avatar avatar = new Avatar(camera, scene);


	protected Renderer renderer = new Renderer();
	protected Screen widgetSet = new Screen();

	protected boolean escPressed = false;



	// ui, sounds, cam

	@Override
	public void setup() {
		GraphicContextManager.INSTANCE.getInputDispatcher().register(this);
		GraphicContextManager.INSTANCE.getInputDispatcher().register(avatar);
	}

	@Subscribe
	public void onExitTriggered(CommandEvent.Exit exitEvent) {
		escPressed = true;
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
		GraphicContextManager.INSTANCE.getInputDispatcher().unregister(this);
		GraphicContextManager.INSTANCE.getInputDispatcher().unregister(avatar);

		scene.teardown();
	}


}
