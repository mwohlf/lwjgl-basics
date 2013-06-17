package net.wohlfart.basic.states;


import net.wohlfart.gl.elements.debug.PositionFrame;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.NullHud;
import net.wohlfart.gl.elements.skybox.NullSkybox;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.renderer.ModelBucket;
import net.wohlfart.gl.renderer.NullRenderBucket;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.renderer.RenderBucketImpl;
import net.wohlfart.gl.shader.LightSource;
import net.wohlfart.gl.spatial.Model;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 *
 *
 */
final class AmbientState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmbientState.class);

    private Skybox skybox = NullSkybox.INSTANCE;
    private RenderBucket elemBucket = new RenderBucketImpl();
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

        for (int x = -5; x <=5; x++) {
            for (int z = -5; z <=5; z++) {
                Model model = SceneCreator.loadModelFromFile("/models/cube/cube.obj");
                model.setPosition(new Vector3f(x * 7, 0, z * 7));
                modelBucket.addContent(model);
            }
        }

        for (IsRenderable renderable : SceneCreator.createOriginAxis()) {
            elemBucket.addContent(renderable);
        }

        LightSource l = new LightSource(
                        // ambient: light that comes from all directions equally and is scattered in all directions equally
                        new Vector4f(0.1f, 0.1f, 0.1f, 1.0f),
                        // diffuse: light that comes from a particular point source and radiates from the surface in all directions
                        new Vector4f(1f, 1f, 1f, 1.0f),
                        // specular: light that comes from a particular point source and radiates from the surface like a mirror
                        new Vector4f( 0, 0, 0, 1),
                        // position of the light source if needed
                        new Vector3f(0, 0, 0),
                        // direction of the light if needed
                        new Vector3f(0,0,0));

        new PositionFrame(l).setup();
        modelBucket.addContent(l);
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
