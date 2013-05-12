package net.wohlfart.basic.states;

import net.wohlfart.gl.antlr4.Model;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MousePositionLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.renderer.ModelBucket;
import net.wohlfart.gl.renderer.RenderableBucket;
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


    private Skybox skybox;
    private ModelBucket modelBucket;
    private RenderableBucket elemBucket;
    private Hud hud;

    private Statistics statistics;
    private MousePositionLabel mousePositionLabel;
    private MousePicker mousePicker;


    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    public void setModelBucket(ModelBucket modelBucket) {
        this.modelBucket = modelBucket;
    }

    public void setElemBucket(RenderableBucket elemBucket) {
        this.elemBucket = elemBucket;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(skybox, "skyboxImpl missing, you probably forgot to inject skyboxImpl in the LightingState");
    }


    @Override
    public void setup() {
        super.setup();

        skybox.setup();
        modelBucket.setup();
        elemBucket.setup();
        hud.setup();

        statistics = new Statistics(0, -40);
        mousePositionLabel = new MousePositionLabel(0, -20);
        mousePicker = new MousePicker(elemBucket, getScreenWidth(), getScreenHeight());


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
        modelBucket.addContent(ship1);

        Model ship2 = SceneCreator.loadModelFromFile("/models/ships/02.obj");
        ship2.setPosition(new Vector3f(+5,0,-10));
        modelBucket.addContent(ship2);
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
