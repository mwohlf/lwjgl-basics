package net.wohlfart.basic.states;

import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.TexturedQuad;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.HudImpl;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MousePositionLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.elements.skybox.SkyboxImpl;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.spatial.CelestialBody;
import net.wohlfart.gl.view.ElementPicker;

import org.lwjgl.util.vector.Vector3f;

public class CelestialState extends AbstractGraphicState {

    private final Skybox skybox = new SkyboxImpl();   //NullSkybox.INSTANCE;

    private final DefaultRenderSet<CelestialBody> planetBucket = new DefaultRenderSet<>();

    private final DefaultRenderSet<IsRenderable> wireframeBucket = new DefaultRenderSet<>();

    private final DefaultRenderSet<IsRenderable> meshBucket = new DefaultRenderSet<>();

    private final Hud hud = new HudImpl();


    private Statistics statistics;
    private MousePositionLabel mousePositionLabel;
    private ElementPicker elementPicker;


    @Override
    public void setup() {
        super.setup();

        skybox.setup();

        wireframeBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.WIREFRAME_SHADER));
        wireframeBucket.addAll(SceneCreator.createOriginAxis());
        wireframeBucket.setup();

        meshBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER));
        TexturedQuad tex = new TexturedQuad();
        tex.setPosition(new Vector3f(0,-10,-10));
        meshBucket.add(tex);
        meshBucket.setup();


        planetBucket.add(new CelestialBody(0));
        // simple wireframe, no light, no texture, no normals, planes will be black
        //planetBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.WIREFRAME_SHADER));
        // texture, no light sources, no normals
        planetBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER));

        //planetBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER));
        planetBucket.setup();


        statistics = new Statistics(0, -40);
        mousePositionLabel = new MousePositionLabel(0, -20);
        hud.add(statistics);
        hud.add(mousePositionLabel);
        hud.add(new Label(0, 0, "hello world at (0,0)"));

        hud.setup();

    }

    @Override
    public void render() {
        skybox.render();
        wireframeBucket.render();
        meshBucket.render();
        planetBucket.render();
        hud.render();
    }

    @Override
    public void update(float tpf) {
        planetBucket.update(tpf);
        wireframeBucket.update(tpf);
        meshBucket.update(tpf);
        hud.update(tpf);
    }

    @Override
    public void destroy() {
        skybox.destroy();
        planetBucket.destroy();
        wireframeBucket.destroy();
        meshBucket.destroy();
        hud.destroy();
        super.destroy();
    }

}
