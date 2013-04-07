package net.wohlfart.basic.states;

import net.wohlfart.gl.antlr4.Model;
import net.wohlfart.gl.elements.hud.widgets.MousePositionLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.renderer.ModelBucket;
import net.wohlfart.gl.renderer.RenderableBucket;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.view.MousePicker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * REVIEW:
 *
 * @author michael
 *
 */
final class LightingState extends AbstractGraphicState {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightingState.class);

    private GraphicContextManager.IGraphicContext lightingGraphicContext;
    private GraphicContextManager.IGraphicContext defaultGraphicContext;
    private GraphicContextManager.IGraphicContext wireframeGraphicContext;


    private final Skybox skybox = new Skybox();
    private final RenderableBucket elemBucket = new RenderableBucket();
    private final ModelBucket modelBucket = new ModelBucket();

    private Statistics statistics;
    private MousePositionLabel mousePositionLabel;
    private MousePicker mousePicker;


    @Override
    public void setup() {
        super.setup();
        statistics = new Statistics(0, -40);
        mousePositionLabel = new MousePositionLabel(0, -20);
        mousePicker = new MousePicker(elemBucket, getScreenWidth(), getScreenHeight());

        lightingGraphicContext = new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER);
        defaultGraphicContext = new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER);
        wireframeGraphicContext = new DefaultGraphicContext(ShaderRegistry.WIREFRAME_SHADER);


        skybox.init(defaultGraphicContext, getCamera());
        modelBucket.init(lightingGraphicContext, getCamera());
        elemBucket.init(wireframeGraphicContext, getCamera());

        getInputDispatcher().register(mousePicker);


        /*
        Model icosphere = SceneCreator.loadModelFromFile("/models/icosphere/icosphere.obj");
        icosphere.setPosition(new Vector3f(0,0,-5));
        icosphere.setAction(OrbitAction.create());
        //icosphere.setAction(new MoveAction());
        //icosphere.setAction(new MoveAction());
        modelBucket.add(icosphere);

        Model icosphere2 = SceneCreator.loadModelFromFile("/models/icosphere/icosphere.obj");
        icosphere2.setPosition(new Vector3f(0,0,0));
        //icosphere2.setAction(new OrbitAction());
        //icosphere.setAction(new MoveAction());
        //icosphere.setAction(new MoveAction());
        modelBucket.add(icosphere2);
        */



        int count = 5000;

        for (int i = 0; i < count ; i++) {
            Model icosphere = SceneCreator.loadModelFromFile("/models/icosphere/icosphere.obj");
            icosphere.setPosition(SceneCreator.getRandomPosition());
            icosphere.setAction(SceneCreator.getRandomAction());
            modelBucket.add(icosphere);
        }

        for (int i = 0; i < count ; i++) {
            Model cube = SceneCreator.loadModelFromFile("/models/cube/cube.obj");
            cube.setPosition(SceneCreator.getRandomPosition());
            cube.setAction(SceneCreator.getRandomAction());
            modelBucket.add(cube);
        }



    }


    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
        modelBucket.update(tpf);
    }

    @Override
    public void render() {
        skybox.render();
        modelBucket.render();
        elemBucket.render();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
