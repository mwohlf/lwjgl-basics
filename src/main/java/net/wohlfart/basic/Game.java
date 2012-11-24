package net.wohlfart.basic;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import net.wohlfart.gl.IState;
import net.wohlfart.gl.NullState;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.model.CelestialState;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Game {

	protected InputSource inputProcessor = InputSource.INSTANCE;

	protected Settings settings = Settings.DEFAULT;
	protected IState currentState = NullState.INSTANCE;

	public void start() throws LWJGLException, InterruptedException {
		long now, lastTimestamp = System.nanoTime();

		setupDisplay();
		setCurrentState(new CelestialState());

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

		destroyDisplay();
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
		Display.setTitle("lwjgl");
		Display.setResizable(settings.getResizable());
		Display.create();

		// reset the projection matrix
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective( 45.0f, (float)settings.getWidth()/(float)settings.getHeight(), settings.getZNear(), settings.getZFar() );


		// reset the global matrix
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
		GL11.glShadeModel (GL11.GL_SMOOTH);
		GL11.glClearColor(0.3f, 0.3f, 0.6f, 0.0f);

		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		float lightAmbient[] = {0.5f, 0.5f, 0.5f, 1.0f};  // Ambient Light Values
		float lightDiffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};  // Diffuse Light Values
		float lightPosition[] = {0.0f, 0.0f, 2.0f, 1.0f}; // Light Position

		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer) temp.asFloatBuffer().put(lightAmbient).flip());              // Setup The Ambient Light
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer) temp.asFloatBuffer().put(lightDiffuse).flip());              // Setup The Diffuse Light
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, (FloatBuffer) temp.asFloatBuffer().put(lightPosition).flip());         // Position The Light
		GL11.glEnable(GL11.GL_LIGHT1);

		Display.setVSyncEnabled(true);

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
