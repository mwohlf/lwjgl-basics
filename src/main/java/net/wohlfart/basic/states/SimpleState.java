package net.wohlfart.basic.states;

import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MousePositionLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.renderer.RenderBucketImpl;
import net.wohlfart.gl.view.ElementPicker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @formatter:off state implementation that consists of (in the order of rendering): - skyboxImpl - elementBucket - hudImpl
 * 
 * @formatter:on
 */
final class SimpleState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleState.class);

    private Statistics statistics;
    private MousePositionLabel mousePositionLabel;
    private ElementPicker elementPicker;

    private Skybox skybox;
    private RenderBucketImpl elemBucket;
    private Hud hud;

    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
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
        Assert.notNull(skybox, "skybox missing, you probably forgot to inject skybox in the configs");
        Assert.notNull(elemBucket, "elemBucket missing, you probably forgot to inject elemBucket in the configs");
        Assert.notNull(hud, "hud missing, you probably forgot to inject hud in the configs");
    }

    @Override
    public void setup() {
        super.setup();

        skybox.setup();
        elemBucket.setup();
        hud.setup();

        statistics = new Statistics(0, -40);
        mousePositionLabel = new MousePositionLabel(0, -20);
        elementPicker = new ElementPicker(elemBucket, getScreenWidth(), getScreenHeight());

        // elemBucket.addContent(new ControllerFrame().init().getCube());
        // elemBucket.addContent(SceneCreator.createRandomLocatedSpheres());

        // event bus registration
        final InputDispatcher inputDispatcher = getInputDispatcher();
        inputDispatcher.register(mousePositionLabel);
        inputDispatcher.register(elementPicker);

        hud.add(statistics);
        hud.add(mousePositionLabel);
        hud.add(new Label(0, 0, "hello world at (0,0)"));

    }

    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
        statistics.update(tpf);
    }

    @Override
    public void render() {
        skybox.render();
        elemBucket.render();
        hud.render();
    }

    @Override
    public void destroy() {
        skybox.dispose();
        elemBucket.dispose();
        hud.dispose();
        super.destroy();
    }

}
