package net.wohlfart.basic;

import net.wohlfart.basic.states.GameState;
import net.wohlfart.basic.states.GameStateEnum;
import net.wohlfart.basic.time.LwjglClockImpl;
import net.wohlfart.basic.time.Timer;
import net.wohlfart.basic.time.TimerImpl;
import net.wohlfart.gl.input.DefaultInputDispatcher;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.input.LwjglInputAdaptor;
import net.wohlfart.gl.input.LwjglInputSource;
import net.wohlfart.gl.shader.GraphicContextManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Game {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    protected Settings settings = new Settings(); // just a default in case nothing gets injected
    protected Timer timer = new TimerImpl(new LwjglClockImpl());
    protected GameState currentState = GameStateEnum.NULL.getValue();
    protected GraphicContextManager graphContext = GraphicContextManager.INSTANCE;

    protected DefaultInputDispatcher inputDispatcher = new DefaultInputDispatcher();
    protected InputSource inputSource = new LwjglInputSource(new LwjglInputAdaptor(inputDispatcher));

    /**
     * set the initial state and fire up the main loop
     */
    void start() {
        try {
            graphContext.setSettings(settings);
            graphContext.setInputDispatcher(inputDispatcher);
            bootupOpenGL();
            setCurrentState(GameStateEnum.SIMPLE);
            runApplicationLoop();
            shutdownOpenGL();
        } catch (final LWJGLException ex) {
            LOGGER.warn("Application startup failed", ex);
        }
    }

    /**
     * this is the main loop that does all the work
     */
    private void runApplicationLoop() {
        float delta;
        while (!currentState.isDone()) {
            delta = timer.getDelta();
            LOGGER.debug("[ms]/frame: {} ; frame/[s]: {}", delta, 1f / delta);
            // call the model to do their things
            currentState.update(delta);
            // clear the screen buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            // do the magic
            currentState.render();
            Display.sync(settings.getSync());
            // draw the (double-)buffer to the screen, read user input
            Display.update();
            // triggers the callbacks for user input
            inputSource.createInputEvents(delta);
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
        GL11.glClearColor(0.1f, 0.7f, 0.1f, 0f);
        // turn culling off so it will be drawn regardless of what way it is facing
        GL11.glDisable(GL11.GL_CULL_FACE);
        // GL11.glEnable(GL11.GL_CULL_FACE);
    }

    // see: http://lwjgl.org/forum/index.php/topic,2951.0.html
    // for more about setting up a display...
    private void setupDisplay() throws LWJGLException {
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtributes = new ContextAttribs(3, 3); // OpenGL versions
        contextAtributes.withForwardCompatible(true);
        contextAtributes.withProfileCore(true);
        Display.setDisplayMode(new DisplayMode(settings.getWidth(), settings.getHeight()));
        Display.setResizable(false);
        Display.setTitle(settings.getTitle());
        Display.setVSyncEnabled(true);
        Display.create(pixelFormat, contextAtributes); // creates the GL context
        LOGGER.info("Vendor: " + GL11.glGetString(GL11.GL_VENDOR));
        LOGGER.info("Version: " + GL11.glGetString(GL11.GL_VERSION));
    }


    private void shutdownOpenGL() {
        Display.destroy();
    }

    public void setCurrentState(final GameStateEnum newState) {
        currentState.dispose();
        currentState = newState.getValue();
        currentState.setup();
    }

    public void setGameSettings(final Settings settings) {
        this.settings = settings;
    }

}
