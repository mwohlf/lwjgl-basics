package net.wohlfart.basic.states;

import net.wohlfart.gl.MousePicker;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MousePosition;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.DefaultShaderProgram;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.model.Avatar;

import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;


/*
 * state implementation that consists of (in the order of rendering):
 * - skybox
 * - elementBucket
 * - hud
 *
 */
class SimpleState implements GameState {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleState.class);

    private boolean quit = false;

    private GraphicContextManager.IGraphicContext defaultGraphicContext;
    private GraphicContextManager.IGraphicContext wireframeGraphicContext;
    private GraphicContextManager.IGraphicContext hudGraphicContext;

    protected GraphicContextManager graphContext = GraphicContextManager.INSTANCE;

    private final Avatar avatar = new Avatar();

    private final Skybox skybox = new Skybox();
    private final RenderBucket elemBucket = new RenderBucket();
    private final Hud hud = new Hud();

    private final boolean skyboxOn = false;
    private final boolean elementsOn = true;
    private final boolean hudOn = false;

    private Statistics statistics;
    private MousePosition mousePosition;
    private MousePicker mousePicker;
    private InputDispatcher inputDispatcher;

    @Override
    public void setup() {
        inputDispatcher = graphContext.getInputDispatcher();
        statistics = new Statistics(0, -40);
        mousePosition = new MousePosition(0, -20);
        mousePicker = new MousePicker(avatar, elemBucket);

        // event bus registration
        inputDispatcher.register(avatar);
        inputDispatcher.register(this);
        inputDispatcher.register(mousePosition);
        inputDispatcher.register(mousePicker);

        wireframeGraphicContext = new DefaultGraphicContext(
                new DefaultShaderProgram(DefaultShaderProgram.WIREFRAME_VERTEX_SHADER, DefaultShaderProgram.WIREFRAME_FRAGMENT_SHADER));
        defaultGraphicContext = new DefaultGraphicContext(
                new DefaultShaderProgram(DefaultShaderProgram.DEFAULT_VERTEX_SHADER, DefaultShaderProgram.DEFAULT_FRAGMENT_SHADER));
        hudGraphicContext = new DefaultGraphicContext(
                new DefaultShaderProgram(DefaultShaderProgram.HUD_VERTEX_SHADER, DefaultShaderProgram.HUD_FRAGMENT_SHADER));

        if (skyboxOn) {
            skybox.init(defaultGraphicContext, avatar);
        }

        if (elementsOn) {
            elemBucket.init(wireframeGraphicContext, avatar);
           // elemBucket.add(new ElementCreator().createCircles());
           // elemBucket.add(new ElementCreator().createRandomElements());
            //ControllerFrame frame = new ControllerFrame();
            //frame.init();
            //elemBucket.add(frame.getCube());
        }

        if (hudOn) {
            hud.init(hudGraphicContext, avatar);
            hud.add(statistics);
            hud.add(mousePosition);
            hud.add(new Label(0, 0, "hello world at (0,0)"));
        }

    }


    @Subscribe
    public synchronized void onExitTriggered(CommandEvent.Exit exitEvent) {
        quit = true;
    }

    @Subscribe
    public synchronized void onMouseMove(CommandEvent.PositionPointer positionEvent) {
        int x = positionEvent.getX();
        int y = positionEvent.getY();
    }

    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
        if (hudOn) {
            statistics.update(tpf);
        }
    }

    @Override
    public void render() {
        if (skyboxOn) {
            skybox.render();
        }
        if (elementsOn) {
            elemBucket.render();
        }
        if (hudOn) {
            hud.render();
        }
    }

    @Override
    public boolean isDone() {
        return Display.isCloseRequested() || quit;
    }

    @Override
    public void dispose() {
        defaultGraphicContext.dispose();
        wireframeGraphicContext.dispose();
        hudGraphicContext.dispose();
        // event bus unregistration
        inputDispatcher.unregister(this);
        inputDispatcher.unregister(avatar);
    }

}
