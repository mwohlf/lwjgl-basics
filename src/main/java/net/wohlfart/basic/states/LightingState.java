package net.wohlfart.basic.states;

import net.wohlfart.gl.antlr4.Model;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.NullHud;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MousePositionLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.NullSkybox;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.renderer.ModelBucket;
import net.wohlfart.gl.renderer.RenderableBucket;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.view.MousePicker;

import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * REVIEW:
 *
 * @author michael
 *
 */
final class LightingState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightingState.class);

    private GraphicContextManager.IGraphicContext lightingGraphicContext;
    private GraphicContextManager.IGraphicContext defaultGraphicContext;
    private GraphicContextManager.IGraphicContext wireframeGraphicContext;
    private GraphicContextManager.IGraphicContext hudGraphicContext;


    private final RenderableBucket elemBucket = new RenderableBucket();
    private final ModelBucket modelBucket = new ModelBucket();

    private Statistics statistics;
    private MousePositionLabel mousePositionLabel;
    private MousePicker mousePicker;


    private Skybox skybox = new NullSkybox();
    private Hud hud = new NullHud();


    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }


    public void setLightingGraphicContext(DefaultGraphicContext lightingGraphicContext) {
        this.lightingGraphicContext = lightingGraphicContext;
    }

    public void setWireframeGraphicContext(DefaultGraphicContext wireframeGraphicContext) {
        this.wireframeGraphicContext = wireframeGraphicContext;
    }

    public void setDefaultGraphicContext(DefaultGraphicContext defaultGraphicContext) {
        this.defaultGraphicContext = defaultGraphicContext;
    }

    public void setHudGraphicContext(DefaultGraphicContext hudGraphicContext) {
        this.hudGraphicContext = hudGraphicContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(skybox, "skyboxImpl missing, you probably forgot to inject skyboxImpl in the LightingState");
    }


    @Override
    public void setup() {
        super.setup();
        statistics = new Statistics(0, -40);
        mousePositionLabel = new MousePositionLabel(0, -20);
        mousePicker = new MousePicker(elemBucket, getScreenWidth(), getScreenHeight());

        skybox.setup();
        hud.setup();

        lightingGraphicContext.setup();
        defaultGraphicContext.setup();
        wireframeGraphicContext.setup();
        hudGraphicContext.setup();

        modelBucket.init(lightingGraphicContext, getCamera());
        //elemBucket.init(wireframeGraphicContext, getCamera());

        hud.add(statistics);
        hud.add(mousePositionLabel);
        hud.add(new Label(0, 0, "hello world at (0,0)"));


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


        /*

        int count = 500;

        for (int i = 0; i < count ; i++) {
            Model icosphere = SceneCreator.loadModelFromFile("/models/ship/ship.obj");
            icosphere.setPosition(SceneCreator.getRandomPosition());
            icosphere.setAction(SceneCreator.getRandomAction());
            modelBucket.add(icosphere);
        }



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

         */


        Model ship1 = SceneCreator.loadModelFromFile("/models/ships/01.obj");
        ship1.setPosition(new Vector3f(-5,0,-10));
        modelBucket.add(ship1);

        Model ship2 = SceneCreator.loadModelFromFile("/models/ships/02.obj");
        ship2.setPosition(new Vector3f(+5,0,-10));
        modelBucket.add(ship2);
        //  icosphere.setAction(SceneCreator.getRandomAction());


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
        hud.render();
    }

    @Override
    public void destroy() {
        super.destroy();
    }



}
