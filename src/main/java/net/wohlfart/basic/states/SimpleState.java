package net.wohlfart.basic.states;

import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.NullHud;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MouseClickLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.NullSkybox;
import net.wohlfart.gl.elements.skybox.Skybox;
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

    private Skybox skybox = NullSkybox.INSTANCE;
    private DefaultRenderSet elemBucket;
    private Hud hud = NullHud.INSTANCE;

    private Statistics statistics;
    private MouseClickLabel mouseClickLabel;
    private ElementPicker elementPicker;

    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    public void setElemBucket(DefaultRenderSet elemBucket) {
        this.elemBucket = elemBucket;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(skybox);
        Assert.notNull(elemBucket);
        Assert.notNull(hud);
    }

    @Override
    public void setup() {
        super.setup();

        skybox.setup();
        elemBucket.setup();
        hud.setup();

        statistics = new Statistics(0, -40);
        mouseClickLabel = new MouseClickLabel(0, -20);
        elementPicker = new ElementPicker(elemBucket, getScreenWidth(), getScreenHeight());

        // elemBucket.addContent(new ControllerFrame().init().getCube());
        // elemBucket.addContent(SceneCreator.createRandomLocatedSpheres());

        // event bus registration
        getGraphContextManager().register(mouseClickLabel);
        getGraphContextManager().register(elementPicker);

        hud.add(statistics);
        hud.add(mouseClickLabel);
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
        getGraphContextManager().unregister(mouseClickLabel);
        getGraphContextManager().unregister(elementPicker);
        skybox.dispose();
        hud.dispose();
        super.destroy();
    }

}
