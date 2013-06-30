package net.wohlfart.basic.states;

import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.TexturedQuad;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.NullHud;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.elements.skybox.SkyboxImpl;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.spatial.CelestialBody;

public class CelestialState extends AbstractGraphicState {

    private final Skybox skybox = new SkyboxImpl();   //NullSkybox.INSTANCE;

    private final DefaultRenderSet<CelestialBody> planetBucket = new DefaultRenderSet<>();

    private final DefaultRenderSet<IsRenderable> wireframeBucket = new DefaultRenderSet<>();

    private final DefaultRenderSet<IsRenderable> meshBucket = new DefaultRenderSet<>();


    private final Hud hud = NullHud.INSTANCE;



    @Override
    public void setup() {
        super.setup();
        skybox.setup();

        wireframeBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.WIREFRAME_SHADER));
        wireframeBucket.addAll(SceneCreator.createOriginAxis());
        wireframeBucket.setup();


        meshBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER));
        meshBucket.add(new TexturedQuad());
        meshBucket.setup();


        planetBucket.add(new CelestialBody(0));
        // simple wireframe, no light, no texture, no normals, planes will be black
        //planetBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.WIREFRAME_SHADER));
        // texture, no lightsources, no normals
        planetBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER));

        //planetBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER));
        planetBucket.setup();

        hud.setup();

    }

    @Override
    public void render() {
        skybox.render();
        wireframeBucket.render();
        meshBucket.render();
    //    planetBucket.render();
    //    hud.render();
    }

    @Override
    public void update(float tpf) {
        planetBucket.update(tpf);
        wireframeBucket.render();

    }

    @Override
    public void destroy() {
        skybox.destroy();
        planetBucket.destroy();
        hud.destroy();
        super.destroy();
    }

}
