package net.wohlfart.basic.states;

import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.NullHud;
import net.wohlfart.gl.elements.skybox.NullSkybox;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.spatial.CelestialBody;

public class CelestialState extends AbstractGraphicState {

    private final Skybox skybox = NullSkybox.INSTANCE;

    private final DefaultRenderSet<CelestialBody> planetBucket = new DefaultRenderSet<>();


    private final Hud hud = NullHud.INSTANCE;



    @Override
    public void setup() {
        super.setup();
        skybox.setup();

        planetBucket.add(new CelestialBody(0));
        //planetBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER));
        //planetBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.WIREFRAME_SHADER));
        planetBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER));
        planetBucket.setup();

        hud.setup();

    }

    @Override
    public void render() {
        skybox.render();
        planetBucket.render();
        hud.render();
    }

    @Override
    public void update(float tpf) {
        planetBucket.update(tpf);
    }

    @Override
    public void destroy() {
        skybox.destroy();
        planetBucket.destroy();
        hud.destroy();
        super.destroy();
    }

}
