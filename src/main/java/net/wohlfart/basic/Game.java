package net.wohlfart.basic;

import java.nio.FloatBuffer;

import net.wohlfart.gl.IState;
import net.wohlfart.gl.NullState;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.model.CelestialState;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Game {

	protected InputSource inputProcessor = InputSource.INSTANCE;
	protected Settings settings = Settings.DEFAULT;
	protected IState currentState = NullState.INSTANCE;


	public void start() throws LWJGLException, InterruptedException {
		setupDisplay();
		setCurrentState(new CelestialState());
		loop();
		destroyDisplay();
	}
	
	
	private void loop() {
		long now, lastTimestamp = System.nanoTime();

		while (!currentState.isDone()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			inputProcessor.process(); // trigger the callbacks for user input
			now = System.nanoTime();
			currentState.update((now - lastTimestamp) / 1000000.0f);
			lastTimestamp = now;
			currentState.render();
			Display.update(); // draw the buffer to the screen, trigger input
			delay();
		}

	}


	private void delay() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}


	private void setupDisplay() throws LWJGLException {

		Display.setDisplayMode(settings.getDisplayMode());
		Display.setResizable(settings.getResizable());
		Display.setVSyncEnabled(true);
		Display.create(); // creates the GL context

		Display.setTitle("lwjgl OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

		// this color is used in GL11.glClear for each rendering loop
		GL11.glClearColor(0.1f, 0.1f, 0.3f, 0.0f);

		// setup the projection matrix stack,
		// the projection is responsible for creating the 2D image out of the 3D scene
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective( 45.0f, (float)settings.getWidth()/(float)settings.getHeight(), settings.getZNear(), settings.getZFar() );

		// setup the modelview matrix stack
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL); // enables opengl to use glColor3f to define material color
		// a single light
		FloatBuffer whiteLight = BufferUtils.createFloatBuffer(4);
		whiteLight.put(0.9f).put(0.9f).put(0.9f).put(0.9f).flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, whiteLight);
		GL11.glEnable(GL11.GL_LIGHT0);
	}


	private void destroyDisplay() {
		Display.destroy();
	}



	// set by spring
	public void setGameSettings(Settings settings) {
		this.settings = settings;
	}

	public void setCurrentState(final IState newState) {
		currentState = newState;
		currentState.setup();
	}


}
