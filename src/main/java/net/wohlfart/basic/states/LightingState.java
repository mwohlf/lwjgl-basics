package net.wohlfart.basic.states;

import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.NullHud;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MousePositionLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.NullSkybox;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.renderer.ModelBucket;
import net.wohlfart.gl.renderer.NullRenderBucket;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.renderer.RenderBucketImpl;
import net.wohlfart.gl.view.ElementPicker;

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

    private Skybox skybox = NullSkybox.INSTANCE;
    private ModelBucket modelBucket;
    private RenderBucket elemBucket = NullRenderBucket.INSTANCE;
    private Hud hud = NullHud.INSTANCE;

    private Statistics statistics;
    private MousePositionLabel mousePositionLabel;
    private ElementPicker elementPicker;

    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    public void setModelBucket(ModelBucket modelBucket) {
        this.modelBucket = modelBucket;
    }

    public void setElemBucket(RenderBucketImpl elemBucket) {
        this.elemBucket = elemBucket;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(skybox);
        Assert.notNull(modelBucket);
        Assert.notNull(elemBucket);
        Assert.notNull(hud);
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
        elementPicker = new ElementPicker(elemBucket, getScreenWidth(), getScreenHeight());
        elementPicker.setRenderBucket(elemBucket);
        elementPicker.setModelBucket(modelBucket);

        hud.add(statistics);
        hud.add(mousePositionLabel);
        hud.add(new Label(0, 0, "hello world at (0,0)"));

        getInputDispatcher().register(elementPicker);



        //modelBucket.addContent(new ColorPointEmitter(500));

        /*
         * Model icosphere = SceneCreator.loadModelFromFile("/models/icosphere/icosphere.obj"); icosphere.setPosition(new Vector3f(0,0,-5));
         * icosphere.setAction(OrbitAction.create()); //icosphere.setAction(new MoveAction()); //icosphere.setAction(new MoveAction());
         * modelBucket.add(icosphere);
         *
         * Model icosphere2 = SceneCreator.loadModelFromFile("/models/icosphere/icosphere.obj"); icosphere2.setPosition(new Vector3f(0,0,0));
         * //icosphere2.setAction(new OrbitAction()); //icosphere.setAction(new MoveAction()); //icosphere.setAction(new MoveAction());
         * modelBucket.add(icosphere2);
         */

        /*
         *
         * int count = 500;
         *
         * for (int i = 0; i < count ; i++) { Model icosphere = SceneCreator.loadModelFromFile("/models/ship/ship.obj");
         * icosphere.setPosition(SceneCreator.getRandomPosition()); icosphere.setAction(SceneCreator.getRandomAction()); modelBucket.add(icosphere); }
         *
         *
         *
         * for (int i = 0; i < count ; i++) { Model icosphere = SceneCreator.loadModelFromFile("/models/icosphere/icosphere.obj");
         * icosphere.setPosition(SceneCreator.getRandomPosition()); icosphere.setAction(SceneCreator.getRandomAction()); modelBucket.add(icosphere); }
         *
         * for (int i = 0; i < count ; i++) { Model cube = SceneCreator.loadModelFromFile("/models/cube/cube.obj");
         * cube.setPosition(SceneCreator.getRandomPosition()); cube.setAction(SceneCreator.getRandomAction()); modelBucket.add(cube); }
         */

    }

    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
        modelBucket.update(tpf);
        statistics.update(tpf);
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
