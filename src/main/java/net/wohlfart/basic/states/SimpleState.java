package net.wohlfart.basic.states;

import java.io.IOException;
import java.io.InputStream;

import net.wohlfart.gl.antlr4.GenericMeshLoader;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MousePositionLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.view.Camera;
import net.wohlfart.gl.view.MousePicker;
import net.wohlfart.tools.ControllerFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * state implementation that consists of (in the order of rendering):
 * - skybox
 * - elementBucket
 * - hud
 *
 */
final class SimpleState extends AbstractGraphicState {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleState.class);

    private GraphicContextManager.IGraphicContext defaultGraphicContext;
    private GraphicContextManager.IGraphicContext wireframeGraphicContext;
    private GraphicContextManager.IGraphicContext hudGraphicContext;

    private final Camera camera = new Camera();

    private final Skybox skybox = new Skybox();
    private final RenderBucket elemBucket = new RenderBucket();
    private final Hud hud = new Hud();

    private Statistics statistics;
    private MousePositionLabel mousePositionLabel;
    private MousePicker mousePicker;

    private final boolean skyboxOn = true;
    private final boolean elementsOn = true;
    private final boolean hudOn = true;
    private final boolean controlFrameOn = true;

    /** {@inheritDoc} */
    @Override
    public void setup() {
        super.setup();
        statistics = new Statistics(0, -40);
        mousePositionLabel = new MousePositionLabel(0, -20);
        mousePicker = new MousePicker(elemBucket, getScreenWidth(), getScreenHeight());

        // event bus registration
        InputDispatcher inputDispatcher = getInputDispatcher();
        inputDispatcher.register(mousePositionLabel);
        inputDispatcher.register(mousePicker);

        wireframeGraphicContext = new DefaultGraphicContext(ShaderRegistry.WIREFRAME_SHADER);
        defaultGraphicContext = new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER);
        hudGraphicContext = new DefaultGraphicContext(ShaderRegistry.HUD_SHADER);

        if (skyboxOn) {
            skybox.init(defaultGraphicContext, camera);
        }

        if (elementsOn) {
            elemBucket.init(wireframeGraphicContext, camera);
            elemBucket.add(ElementCreator.createCircles());
            elemBucket.add(ElementCreator.createSpheres());
            elemBucket.add(ElementCreator.createRandomElements());

            try (InputStream inputStream = ClassLoader.class.getResourceAsStream("/models/cube/cube.obj");) {
                setCurrentGraphicContext(wireframeGraphicContext);
                elemBucket.add(new GenericMeshLoader().getRenderable(inputStream));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (controlFrameOn) {
            ControllerFrame frame = new ControllerFrame();
            frame.init();
            elemBucket.add(frame.getCube());
        }

        if (hudOn) {
            hud.init(hudGraphicContext);
            hud.add(statistics);
            hud.add(mousePositionLabel);
            hud.add(new Label(0, 0, "hello world at (0,0)"));
        }
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
    public void destroy() {
        defaultGraphicContext.dispose();
        wireframeGraphicContext.dispose();
        hudGraphicContext.dispose();
        // event bus unregistration
        InputDispatcher inputDispatcher = getInputDispatcher();
        inputDispatcher.unregister(this);
        inputDispatcher.unregister(camera);
    }

}
