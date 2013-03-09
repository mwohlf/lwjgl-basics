package net.wohlfart.basic.states;

import java.io.IOException;
import java.io.InputStream;

import net.wohlfart.antlr4.ModelLoader;
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
import net.wohlfart.gl.shader.mesh.GenericMeshBuilder;
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
    /** Constant <code>LOGGER</code> */
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

    private final boolean skyboxOn = true;
    private final boolean elementsOn = true;
    private final boolean hudOn = true;

    private Statistics statistics;
    private MousePosition mousePosition;
    private MousePicker mousePicker;
    private InputDispatcher inputDispatcher;

    /** {@inheritDoc} */
    @Override
    public void setup() {
        inputDispatcher = graphContext.getInputDispatcher();
        statistics = new Statistics(0, -40);
        mousePosition = new MousePosition(0, -20);
        mousePicker = new MousePicker(elemBucket, graphContext.getScreenWidth(), graphContext.getScreenHeight());

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
            elemBucket.add(new ElementCreator().createCircles());
            //elemBucket.add(new ElementCreator().createRandomElements());
            //ControllerFrame frame = new ControllerFrame();
            //frame.init();
            //elemBucket.add(frame.getCube());
            try (InputStream inputStream = ClassLoader.class.getResourceAsStream("/models/cube/cube.obj");) {
                graphContext.setCurrentGraphicContext(wireframeGraphicContext);
                GenericMeshBuilder builder = new ModelLoader().getBuilder(inputStream);
                elemBucket.add(builder.build());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (hudOn) {
            hud.init(hudGraphicContext);
            hud.add(statistics);
            hud.add(mousePosition);
            hud.add(new Label(0, 0, "hello world at (0,0)"));
        }

    }


    /**
     * <p>onExitTriggered.</p>
     *
     * @param exitEvent a {@link net.wohlfart.gl.input.CommandEvent.Exit} object.
     */
    @Subscribe
    public synchronized void onExitTriggered(CommandEvent.Exit exitEvent) {
        quit = true;
    }

    /** {@inheritDoc} */
    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
        if (hudOn) {
            statistics.update(tpf);
        }
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public boolean isDone() {
        return Display.isCloseRequested() || quit;
    }

    /** {@inheritDoc} */
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
