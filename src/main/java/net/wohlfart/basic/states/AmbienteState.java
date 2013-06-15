package net.wohlfart.basic.states;

import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.NullHud;
import net.wohlfart.gl.elements.skybox.NullSkybox;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.renderer.ModelBucket;
import net.wohlfart.gl.renderer.NullRenderBucket;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.renderer.RenderBucketImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 *
 *
 */
final class AmbienteState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmbienteState.class);

    private Skybox skybox = NullSkybox.INSTANCE;
    private RenderBucket elemBucket = NullRenderBucket.INSTANCE;
    private Hud hud = NullHud.INSTANCE;
    private ModelBucket modelBucket;


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
