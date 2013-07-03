package net.wohlfart.basic.states;

import net.wohlfart.basic.container.DefaultRenderBucket;
import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.basic.container.ModelRenderSet;
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
 *
 *
 */
final class LightingState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightingState.class);

    private Skybox skybox = NullSkybox.INSTANCE;
    private DefaultRenderSet elemBucket;
    private Hud hud = NullHud.INSTANCE;
    private ModelRenderSet modelBucket;

    private Statistics statistics;
    private MouseClickLabel mouseClickLabel;
    private ElementPicker elementPicker;

    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    public void setModelBucket(ModelRenderSet modelBucket) {
        this.modelBucket = modelBucket;
    }

    public void setElemBucket(DefaultRenderBucket elemBucket) {
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
        mouseClickLabel = new MouseClickLabel(0, -20);

        hud.add(statistics);
        hud.add(mouseClickLabel);
        hud.add(new Label(0, 0, "hello world at (0,0)"));

        getGraphContextManager().register(elementPicker);

        //modelBucket.addContent(new ParticleEmitter());
        //modelBucket.addContent(new ColorPointEmitter(500));

    }

    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
        modelBucket.update(tpf);
        elemBucket.update(tpf);
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
        getGraphContextManager().unregister(elementPicker);
        super.destroy();
    }

}
