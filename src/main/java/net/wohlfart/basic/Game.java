package net.wohlfart.basic;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.wohlfart.basic.states.GameState;
import net.wohlfart.basic.states.GameStateEnum;
import net.wohlfart.basic.time.Clock;
import net.wohlfart.basic.time.TimerImpl;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.view.Camera;

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
 * This class is bootstrapping the application and
 * also handles state changes within the application.
 */
class Game implements InitializingBean { // REVIEWED
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    protected final GraphicContextManager graphContext = GraphicContextManager.INSTANCE;

    protected GameState initialState = GameStateEnum.NULL;
    protected GameState currentState = GameStateEnum.NULL;

    protected Settings settings;
    protected ResourceManager resourceManager;
    protected Clock globalClock;
    protected Camera camera;
    protected InputSource inputSource;
    protected InputDispatcher inputDispatcher;


    /** we need to remember the initial display mode so we can reset it on exit */
    private DisplayMode origDisplayMode;


    public void setGameSettings(Settings settings) {
        this.settings = settings;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setGlobalClock(Clock globalClock) {
        this.globalClock = globalClock;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setInputSource(InputSource userInputSource) {
        this.inputSource = userInputSource;
    }

    public void setInputDispatcher(InputDispatcher inputDispatcher) {
        this.inputDispatcher = inputDispatcher;
    }

    public void setInitialState(GameStateEnum initialState) {
        this.initialState = initialState;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(settings);
        Assert.notNull(camera);
        Assert.notNull(resourceManager);
        Assert.notNull(globalClock);
        Assert.notNull(inputSource);
        Assert.notNull(inputDispatcher);
    }

    /**
     * Entry point for the application sets the initial state and fire up the main loop.
     */
    void start() {
        try {
            // side effect during startup is fixing the settings' height/width value
            // and initializing the OpenGL environment
            startPlatform();
            runApplicationLoop();
            shutdownGame();
            shutdownPlatform();
        } catch (final LWJGLException | IOException ex) {
            LOGGER.warn("Application startup failed", ex);
        }
    }

    /**
     * This is the main loop that does all the work,
     * this method returns when the application is exited by the user.
     */
    private void runApplicationLoop() {
        final TimerImpl globalTimer = new TimerImpl(globalClock);
        while (!currentState.isDone()) {
            float delta = globalTimer.getDelta();
            LOGGER.debug("[ms]/frame: {} ; frame/[s]: {}", delta, 1f / delta);
            // call the models to do their things
            currentState.update(delta);
            // clear the screen buffer, not needed if we have a skybox working
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
            // do the render magic
            currentState.render();
            Display.sync(settings.getSync());
            // draw the (double-)buffer to the screen, read user input
            Display.update();
            // triggers the callbacks for user input
            inputSource.createInputEvents(delta);
        }
    }

    /**
     * Setup a OpenGL 3.3 environment, side effect is fixing height/width in the settings.
     *
     * @throws IOException, LWJGLException
     */
    private void startPlatform() throws LWJGLException, IOException {
        Display.setIcon(new ByteBuffer[] { // @formatter:off
                resourceManager.loadIcon(resourceManager.getGfxUrl("icons/main128.png")),
                resourceManager.loadIcon(resourceManager.getGfxUrl("icons/main32.png")),
                resourceManager.loadIcon(resourceManager.getGfxUrl("icons/main16.png")),
        }); // @formatter:on
        if (settings.getFullscreen()) {
            setupFullscreen();
        } else {
            setupWindow();
        }
        // if nothing exploded so far we have a valid OpenGL context

        // map the internal OpenGL coordinate system to the entire viewport
        GL11.glViewport(0, 0, settings.width, settings.height);
        // used for GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); not really needed if we have a skybox anyways
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glClearColor(0.5f, 0.5f, 0.5f, 0f);
        GL11.glClearDepth(1f);
        // turn culling off so it will be drawn regardless of which way a surface is facing
        GL11.glDisable(GL11.GL_CULL_FACE); // enable for production
        GL11.glDisable(GL11.GL_DEPTH_TEST); // enable for production and check how this works with the skybox

        // wire the stuff after the display has been created and the settings have been fixed
        graphContext.setSettings(settings);
        graphContext.setInputDispatcher(inputDispatcher);
        graphContext.setCamera(camera);
        // activate inputs
        inputSource.setInputDispatcher(inputDispatcher);
        setCurrentState(initialState);

        LOGGER.info("Vendor: " + GL11.glGetString(GL11.GL_VENDOR));
        LOGGER.info("Version: " + GL11.glGetString(GL11.GL_VERSION));
        LOGGER.info("max. Vertex Attributes: " + GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS));
        LOGGER.info("max. Texture Image Units: " + GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS));
    }

    private void setupWindow() throws LWJGLException {
        // see: http://lwjgl.org/forum/index.php/topic,2951.0.html
        // for more about setting up a display...
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtributes = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
        Display.setDisplayMode(new DisplayMode(settings.getWidth(), settings.getHeight()));
        Display.setResizable(false);
        Display.setTitle(settings.getTitle());
        Display.create(pixelFormat, contextAtributes); // creates the GL context
    }

    private void setupFullscreen() throws LWJGLException {
        final DisplayMode[] modes = Display.getAvailableDisplayModes();
        DisplayMode requestedResolution = null;
        DisplayMode bestResolution = null;
        for (final DisplayMode mode : modes) {
            if (bestResolution == null || mode.getWidth() > bestResolution.getWidth() || mode.getHeight() > bestResolution.getHeight()) {
                bestResolution = mode;
            }
            if (mode.getWidth() == settings.width && mode.getHeight() == settings.height) {
                requestedResolution = mode;
            }
        }
        origDisplayMode = Display.getDisplayMode();
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtributes = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
        Display.setDisplayMode(bestResolution);
        Display.setFullscreen(true);
        if (requestedResolution != null) {
            Display.setDisplayMode(requestedResolution);
        } else {
            Display.setDisplayMode(bestResolution);
            final int width = bestResolution.getWidth();
            final int height = bestResolution.getHeight();
            LOGGER.info("fixing width/height to: {}/{}", width, height);
            settings.setWidth(width);
            settings.setHeight(height);
        }
        Display.create(pixelFormat, contextAtributes); // creates the GL context
    }

    /**
     * Setter for the field <code>currentState</code>, destroy() is called on the current state
     * and setup() is called on the new state.
     */
    public void setCurrentState(final GameState newState) {
        currentState.destroy();
        currentState = newState;
        currentState.setup();
    }

    private void shutdownPlatform() {
        if (origDisplayMode != null) {
            try {
                Display.setDisplayMode(origDisplayMode);
            } catch (final LWJGLException ex) {
                LOGGER.warn("error while shutting down", ex);
            }
        }
        Display.destroy();
    }

    private void shutdownGame() {
        setCurrentState(GameStateEnum.NULL);
        graphContext.destroy();
        globalClock.destroy();
        inputSource.destroy();
    }

}
