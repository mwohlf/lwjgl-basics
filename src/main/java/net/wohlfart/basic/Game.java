package net.wohlfart.basic;

import net.wohlfart.basic.states.GameState;
import net.wohlfart.basic.states.GameStateEnum;
import net.wohlfart.basic.time.Timer;
import net.wohlfart.basic.time.TimerImpl;
import net.wohlfart.gl.input.DefaultInputDispatcher;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.shader.GraphicContextManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 *
 * bootstrapping the game and handling state changes within the game
 *
 */
class Game {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    protected Settings settings;

    protected GameState currentState = GameStateEnum.NULL.getValue();
    protected GraphicContextManager graphContext = GraphicContextManager.INSTANCE;
    protected DefaultInputDispatcher inputDispatcher = new DefaultInputDispatcher();

    // platform specific instances
    protected Platform platform;
    protected Timer timer;
    protected InputSource inputSource;

    private DisplayMode oldDisplayMode;

    /**
     * entry point for the application
     * set the initial state and fire up the main loop
     */
    void start() {
        try {
            graphContext.setSettings(settings);
            graphContext.setInputDispatcher(inputDispatcher);
            bootupOpenGL();
            graphContext.setClock(platform.createClock());
            timer = new TimerImpl(graphContext.getClock());
            inputSource = platform.createInputSource(inputDispatcher);
            setCurrentState(GameStateEnum.SIMPLE);
            runApplicationLoop();
            shutdownOpenGL();
            shutdownGame();
        } catch (final LWJGLException ex) {
            LOGGER.warn("Application startup failed", ex);
        }
    }

    public void setGameSettings(Settings settings) {
        this.settings = settings;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
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
            // clear the screen buffer, not needed if we have a skybox working
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
        if (settings.getFullscreen()) {
            setupFullscreen();
        } else {
            setupWindow();
        }
        // Map the internal OpenGL coordinate system to the entire screen
        GL11.glViewport(0, 0, settings.width, settings.height);
        // used for GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(0.1f, 0.7f, 0.1f, 0f);
        //GL11.glClearColor(0.0f, 0.0f, 0.0f, 0f);
        // turn culling off so it will be drawn regardless of what way it is facing
        GL11.glDisable(GL11.GL_CULL_FACE);
        // GL11.glEnable(GL11.GL_CULL_FACE);
        LOGGER.info("Vendor: " + GL11.glGetString(GL11.GL_VENDOR));
        LOGGER.info("Version: " + GL11.glGetString(GL11.GL_VERSION));
    }

    // see: http://lwjgl.org/forum/index.php/topic,2951.0.html
    // for more about setting up a display...
    private void setupWindow() throws LWJGLException {
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtributes = new ContextAttribs(3, 3); // OpenGL versions
        contextAtributes.withForwardCompatible(true);
        contextAtributes.withProfileCore(true);
        Display.setDisplayMode(new DisplayMode(settings.getWidth(), settings.getHeight()));
        Display.setResizable(false);
        Display.setTitle(settings.getTitle());
        Display.create(pixelFormat, contextAtributes); // creates the GL context
    }

    private void setupFullscreen() throws LWJGLException {
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        DisplayMode bestFit = modes[0];
        for (DisplayMode mode : modes) {
            if ((bestFit == null) || (mode.getWidth() > bestFit.getWidth())) {
 //               bestFit = mode;
            }
        }
        oldDisplayMode = Display.getDisplayMode();
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtributes = new ContextAttribs(3, 3); // OpenGL versions
        contextAtributes.withForwardCompatible(true);
        contextAtributes.withProfileCore(true);
        Display.setDisplayMode(bestFit);
        Display.setFullscreen(true);
        Display.setDisplayMode(bestFit);
        Display.create(pixelFormat, contextAtributes); // creates the GL context
    }


    public void setCurrentState(final GameStateEnum newState) {
        currentState.dispose();
        currentState = newState.getValue();
        currentState.setup();
    }

    private void shutdownOpenGL() {
        if (oldDisplayMode != null) {
            try {
                Display.setDisplayMode(oldDisplayMode);
            } catch (LWJGLException ex) {
                LOGGER.warn("error while shutting down", ex);
            }
        }
        Display.destroy();
    }

    private void shutdownGame() {
        graphContext.destroy();
        timer.destroy();
        inputSource.destroy();
    }

}
