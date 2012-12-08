package net.wohlfart.basic;

import net.wohlfart.basic.states.GameStates;
import net.wohlfart.basic.states.IGameState;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Game {
	protected static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

	protected InputSource inputProcessor = InputSource.INSTANCE;
	protected IGameState currentState = GameStates.NULL.getValue();
	protected Settings settings = new Settings(); // default in case nothing gets injected

	private long now;
	private long lastTimestamp;
	private float delta; // in [s]

	private Matrix4f projectionMatrix;

	/**
	 * set the initial state and fire up the main loop
	 */
	public void start() {
		try {
			lastTimestamp = Sys.getTime();
			projectionMatrix = createProjectionMatrix();
			bootupOpenGL();
			setCurrentState(GameStates.SIMPLE);
			runApplicationLoop();
			shutdownOpenGL();
		} catch (LWJGLException ex) {
			LOGGER.warn("Application startup failed", ex);
		}
	}

	/**
	 * this is the main loop that does all the work
	 */
	private void runApplicationLoop() {
		while (!currentState.isDone()) {
			// calculate the time since last loop
			now = Sys.getTime();
			delta = (now - lastTimestamp) / (float)Sys.getTimerResolution();
			lastTimestamp = now;
			LOGGER.debug("[ms]/frame: {} ; frame/[s]: {}", delta, 1f/delta);
			// call the model to do their things
			currentState.update(delta);
			// do the magic
			currentState.render();
			Display.sync(settings.getSync());
			// draw the (double-)buffer to the screen, trigger input
			Display.update();
			// triggers the callbacks for user input
			inputProcessor.process();
		}
	}

	/**
	 * setup a OpenGL 3.3 environment
	 */
	private void bootupOpenGL() throws LWJGLException {
		setupDisplay();
		// Map the internal OpenGL coordinate system to the entire screen
		GL11.glViewport(0, 0, settings.width, settings.height);
		// used for GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0.4f, 0.1f, 0.1f, 0f);
		// turn culling off so it will be drawn regardless of what way it is facing
		GL11.glDisable(GL11.GL_CULL_FACE);
		//GL11.glEnable(GL11.GL_CULL_FACE);


	}

	// see: http://lwjgl.org/forum/index.php/topic,2951.0.html
	// for more about setting up a display...
	private void setupDisplay() throws LWJGLException {
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAtributes = new ContextAttribs(3, 3); // OpenGL versions
		contextAtributes.withForwardCompatible(true);
		contextAtributes.withProfileCore(true);
		Display.setDisplayMode(new DisplayMode(settings.getWidth(), settings.getHeight()));
		Display.setResizable(false); // doesn't work anyways
		Display.setTitle(settings.getTitle());
		//Display.setVSyncEnabled(true);
		Display.create(pixelFormat, contextAtributes); // creates the GL context
		LOGGER.info("Vendor: " + GL11.glGetString(GL11.GL_VENDOR));
		LOGGER.info("Version: " + GL11.glGetString(GL11.GL_VERSION));
	}

	/**
	 * - the projection matrix defines the lens of the camera
	 * - the view matrix defines the position and the direction of the camera
	 * - the model matrix defines the position and direction of each 3D model
	 * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_with_Projection,_View_and_Model_matrices
	 * @return  our projection matrix
	 */
	public Matrix4f createProjectionMatrix() {
		// Setup projection matrix
		Matrix4f matrix = new Matrix4f();
		float fieldOfView = settings.getFieldOfView();
		float aspectRatio = (float)settings.width / (float)settings.height;
		float nearPlane = settings.getNearPlane();
		float farPlane = settings.getFarPlane();

		float yScale = SimpleMath.coTan(SimpleMath.deg2rad(fieldOfView / 2f));
		float xScale = yScale / aspectRatio;
		float frustumLength = farPlane - nearPlane;

		matrix.m00 = xScale;
		matrix.m11 = yScale;
		matrix.m22 = -((farPlane + nearPlane) / frustumLength);
		matrix.m23 = -1;
		matrix.m32 = -((2 * nearPlane * farPlane) / frustumLength);

		return matrix;
	}


	private void shutdownOpenGL() {
		Display.destroy();
	}

	public void setCurrentState(final GameStates newState) {
		currentState.teardown(this);
		currentState = newState.getValue();
		currentState.setup(this, projectionMatrix);
	}

	public void setGameSettings(final Settings settings) {
		this.settings = settings;
	}

}
