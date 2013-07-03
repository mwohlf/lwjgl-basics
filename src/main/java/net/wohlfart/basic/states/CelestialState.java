package net.wohlfart.basic.states;


import java.util.List;

import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.basic.container.ModelRenderSet;
import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.TexturedQuad;
import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.HudImpl;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MouseClickLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.NullSkybox;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.VertexLight;
import net.wohlfart.gl.spatial.CelestialBody;
import net.wohlfart.gl.spatial.Model;
import net.wohlfart.gl.view.ElementPicker;
import net.wohlfart.gl.view.PickingRay;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.google.common.eventbus.Subscribe;

public class CelestialState extends AbstractGraphicState {

    //private final Skybox skybox = new SkyboxImpl();   //NullSkybox.INSTANCE;
    private final Skybox skybox = NullSkybox.INSTANCE;

    private final DefaultRenderSet<CelestialBody> planetSet = new DefaultRenderSet<>();

    private final ModelRenderSet modelSet = new ModelRenderSet();

    private final DefaultRenderSet<IsRenderable> wireframeBucket = new DefaultRenderSet<>();

    private final DefaultRenderSet<IsRenderable> meshBucket = new DefaultRenderSet<>();

    private final Hud hud = new HudImpl();


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

        planetSet.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.AMBIENT_SHADER));
        CelestialBody body = new CelestialBody(0);
        body.setPosition(new Vector3f(7,0,0));
        planetSet.add(body);

        VertexLight light1 = new VertexLight(0.00001f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector3f( 0, 10, -17));
        VertexLight light2 = new VertexLight(0.00001f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector3f( 0, 10, 17));
        planetSet.add(light1);
        planetSet.add(light2);

        planetSet.setup();

        hud.add(new Label(0, 0, "hello world at (0,0)"));

        ElementPicker elementPicker = new ElementPicker(wireframeBucket);
        getGraphContextManager().register(elementPicker);

        hud.add(new Statistics(0, -70));
        hud.add(new MouseClickLabel(0, -35));

        hud.setup();
    }

    @Subscribe
    public void onPickEvent(PickingRay pickingRay) {
        wireframeBucket.add(Arrow.createLink(pickingRay.getStart(), pickingRay.getEnd()));
        List<Model> picklist = modelSet.pick(pickingRay);
        System.out.println("picking: " + picklist);
    }

    @Override
    public void render() {
        skybox.render();
        wireframeBucket.render();
        meshBucket.render();
        planetSet.render();
        hud.render();
    }

    @Override
    public void update(float tpf) {
        planetSet.update(tpf);
        wireframeBucket.update(tpf);
        meshBucket.update(tpf);
        hud.update(tpf);
    }

    @Override
    public void destroy() {
        skybox.destroy();
        planetSet.destroy();
        wireframeBucket.destroy();
        meshBucket.destroy();
        hud.destroy();
        super.destroy();
    }

}
