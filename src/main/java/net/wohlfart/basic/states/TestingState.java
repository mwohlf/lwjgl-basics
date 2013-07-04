package net.wohlfart.basic.states;

import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.HudImpl;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.VertexLight;
import net.wohlfart.gl.spatial.CelestialBody;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class TestingState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleState.class);

    private final Hud hud = new HudImpl();

    private final DefaultRenderSet<CelestialBody> planetSet = new DefaultRenderSet<>();


    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(hud);
    }

    @Override
    public void setup() {
        super.setup();
        hud.setup();
        hud.add(new Label(0, 0, "hello world at (0,0)"));

        planetSet.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.AMBIENT_SHADER));
        CelestialBody body01 = new CelestialBody(0);
        body01.setPosition(new Vector3f(7,0,0));
        planetSet.add(body01);
        CelestialBody body02 = new CelestialBody(0);
        body02.setPosition(new Vector3f(7,0,7));
        planetSet.add(body02);

        VertexLight light3 = new VertexLight(0.00001f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector3f( 0, 10, -17));
        VertexLight light4 = new VertexLight(0.00001f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector3f( 0, 10, 17));
        planetSet.add(light3);
        planetSet.add(light4);

        planetSet.setup();

    }

    @Override
    public void update(float tpf) {
        hud.update(tpf);
        planetSet.update(tpf);
    }

    @Override
    public void render() {
        hud.render();
        planetSet.render();
    }

    @Override
    public void destroy() {
        hud.destroy();
        planetSet.destroy();
        super.destroy();
    }

}

