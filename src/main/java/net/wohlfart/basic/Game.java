package net.wohlfart.basic;

import net.wohlfart.basic.states.GameState;
import net.wohlfart.basic.states.GameStateEnum;
import net.wohlfart.basic.time.Timer;
import net.wohlfart.basic.time.TimerImpl;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.shader.GraphicContextManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.PixelFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * <p>
 * This class is bootstrapping the application
 * and handles state changes within the application.
 * </p>
 */
class Game implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    protected final GraphicContextManager graphContext = GraphicContextManager.INSTANCE;

    protected GameState currentState = GameStateEnum.NULL.getValue();

    // lwjgl, ... jogl, webgl (hopefully one day)
    protected Platform platform;
    protected Settings settings;
    protected InputDispatcher inputDispatcher;

    protected Timer globalGameTimer;
    protected InputSource userInputSource;

    /** we need to remember the initial display mode so we can reset it on exit*/
    private DisplayMode rememberDisplayMode;


    /**
     * <p>Setter for the field <code>platform</code>.</p>
     *
     * @param platform a {@link net.wohlfart.basic.Platform} object.
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    /**
     * <p>setGameSettings.</p>
     *
     * @param settings a {@link net.wohlfart.basic.Settings} object.
     */
    public void setGameSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * <p>setting the event bus for user input</P>
     *
     * @param inputDispatcher
     */
    public void setInputDispatcher(InputDispatcher inputDispatcher) {
        this.inputDispatcher = inputDispatcher;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(platform, "platform missing, you probably forgot to inject platform in the Game");
        Assert.notNull(settings, "settings missing, you probably forgot to inject settings in the Game");
        Assert.notNull(inputDispatcher, "inputDispatcher missing, you probably forgot to inject inputDispatcher in the Game");

        graphContext.setSettings(settings);
        graphContext.setInputDispatcher(inputDispatcher);
    }


    /**
     * <p>Entry point for the application
     * sets the initial state and fire up the main loop.</p>
     */
    void start() {
        try {
            startPlatform();
            globalGameTimer = new TimerImpl(platform.createClock());
            userInputSource = platform.createInputSource(inputDispatcher);
            //setCurrentState(GameStateEnum.SIMPLE);
            setCurrentState(GameStateEnum.LIGHTING);
            runApplicationLoop();
            shutdownGame();
            shutdownPlatform();
        } catch (final LWJGLException ex) {
            LOGGER.warn("Application startup failed", ex);
        }
    }


    /**
     * <p>This is the main loop that does all the work,
     *    this method returns when the application is exited by the user.</p>
     */
    private void runApplicationLoop() {
        float delta;
        while (!currentState.isDone()) {
            delta = globalGameTimer.getDelta();
            LOGGER.debug("[ms]/frame: {} ; frame/[s]: {}", delta, 1f / delta);
            // call the models to do their things
            currentState.update(delta);
            // clear the screen buffer, not needed if we have a skybox working
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            // do the render magic
            currentState.render();
            Display.sync(settings.getSync());
            // draw the (double-)buffer to the screen, read user input
            Display.update();
            // triggers the callbacks for user input
            userInputSource.createInputEvents(delta);
        }
    }

    /**
     * setup a OpenGL 3.3 environment
     */
    private void startPlatform() throws LWJGLException {
        if (settings.getFullscreen()) {
            setupFullscreen();
        } else {
            setupWindow();
        }
        // map the internal OpenGL coordinate system to the entire viewport
        GL11.glViewport(0, 0, settings.width, settings.height);
        // used for GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); not really needed if we have a skybox anyways
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT,GL11.GL_NICEST);
        GL11.glClearColor(0.5f, 0.5f, 0.5f, 0f);
        GL11.glClearDepth(1f);
        //GL11.glClearColor(0.0f, 0.0f, 0.0f, 0f);
        // turn culling off so it will be drawn regardless of which way a surface is facing
        GL11.glDisable(GL11.GL_CULL_FACE);  // enable for production
        GL11.glDisable(GL11.GL_DEPTH_TEST); // enable for production and check how this works with the skybox

        LOGGER.info("Vendor: " + GL11.glGetString(GL11.GL_VENDOR));
        LOGGER.info("Version: " + GL11.glGetString(GL11.GL_VERSION));
        LOGGER.info("max. Vertex Attributes: " + GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS));
        LOGGER.info("max. Texture Image Units: " + GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS));
    }

    // see: http://lwjgl.org/forum/index.php/topic,2951.0.html
    // for more about setting up a display...
    private void setupWindow() throws LWJGLException {
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtributes = new ContextAttribs(3, 3)
             .withForwardCompatible(true)
             .withProfileCore(true);
        Display.setDisplayMode(new DisplayMode(settings.getWidth(), settings.getHeight()));
        Display.setResizable(false);
        Display.setTitle(settings.getTitle());
        Display.create(pixelFormat, contextAtributes); // creates the GL context
    }

    private void setupFullscreen() throws LWJGLException {
        // TODO: figure out which one is the best mode and use it
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        DisplayMode bestFit = modes[0];
        for (DisplayMode mode : modes) {
            if ((bestFit == null) || (mode.getWidth() > bestFit.getWidth())) {
 //               bestFit = mode;
            }
        }
        rememberDisplayMode = Display.getDisplayMode();
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtributes = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
        Display.setDisplayMode(bestFit);
        Display.setFullscreen(true);
        Display.setDisplayMode(bestFit);
        Display.create(pixelFormat, contextAtributes); // creates the GL context
    }


    /**
     * <p>Setter for the field <code>currentState</code>.</p>
     *
     * @param newState a {@link net.wohlfart.basic.states.GameStateEnum} object.
     */
    public void setCurrentState(final GameStateEnum newState) {
        currentState.destroy();
        currentState = newState.getValue();
        currentState.setup();
    }

    private void shutdownPlatform() {
        if (rememberDisplayMode != null) {
            try {
                Display.setDisplayMode(rememberDisplayMode);
            } catch (LWJGLException ex) {
                LOGGER.warn("error while shutting down", ex);
            }
        }
        Display.destroy();
    }

    private void shutdownGame() {
        setCurrentState(GameStateEnum.NULL);
        graphContext.destroy();
        globalGameTimer.destroy();
        userInputSource.destroy();
    }

}
