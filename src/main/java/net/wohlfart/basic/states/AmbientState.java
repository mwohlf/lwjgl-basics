package net.wohlfart.basic.states;


import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.basic.container.DefaultRenderBucket;
import net.wohlfart.basic.container.ModelBucket;
import net.wohlfart.basic.container.RenderSet;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.NullHud;
import net.wohlfart.gl.elements.skybox.NullSkybox;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.spatial.CelestialBody;

import org.lwjgl.util.vector.Vector3f;
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
    private RenderSet elemBucket = new DefaultRenderBucket();

    private Hud hud = NullHud.INSTANCE;
    private ModelBucket modelBucket;
    private DefaultRenderSet planetBucket;


    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    public void setCelestialBucket(DefaultRenderSet planetBucket) {
        this.planetBucket = planetBucket;
    }

    public void setModelBucket(ModelBucket modelBucket) {
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
        Assert.notNull(planetBucket);
        Assert.notNull(modelBucket);
        Assert.notNull(elemBucket);
        Assert.notNull(hud);
    }

    @Override
    public void setup() {
        super.setup();

        skybox.setup();
        planetBucket.setup();
        modelBucket.setup();
        elemBucket.setup();
        hud.setup();

        /*
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

        VertexLight l1 = new VertexLight(0.00001f, new Vector4f(1f, 1f, 1f, 1.0f), new Vector3f( 0, 10, -17));
        VertexLight l2 = new VertexLight(0.00001f, new Vector4f(1f, 1f, 1f, 1.0f), new Vector3f( 0, 10, 17));

        new PositionFrame(l1).setup();
        modelBucket.addContent(l1);
        modelBucket.addContent(l2);

        modelBucket.addContent(new ParticleEmitter()); // FIXME: the particles need normals for this to work
        */
        CelestialBody body = new CelestialBody(0L);
        body.setPosition(new Vector3f(0,0,-7));
        planetBucket.add(body);

    }

    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
        modelBucket.update(tpf);
    }

    @Override
    public void render() {
        skybox.render();
        planetBucket.render();
        modelBucket.render();
        elemBucket.render();
        hud.render();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
