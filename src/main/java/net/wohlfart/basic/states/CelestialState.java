package net.wohlfart.basic.states;

import java.util.List;

import net.wohlfart.basic.container.DefaultRenderSet;
import net.wohlfart.basic.container.ModelRenderSet;
import net.wohlfart.basic.container.ModelToolkit;
import net.wohlfart.basic.container.WireframeToolkit;
import net.wohlfart.basic.elements.IsUpdateable;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.google.common.eventbus.Subscribe;

public class CelestialState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CelestialState.class);


    //private final Skybox skybox = new SkyboxImpl();   //NullSkybox.INSTANCE;
    private final Skybox skybox = NullSkybox.INSTANCE;

    private final DefaultRenderSet<CelestialBody> planetSet = new DefaultRenderSet<>();

    private final ModelRenderSet modelSet = new ModelRenderSet();

    private final DefaultRenderSet<IsUpdateable> wireframeBucket = new DefaultRenderSet<>();

    private final DefaultRenderSet<IsUpdateable> meshBucket = new DefaultRenderSet<>();

    private final Hud hud = new HudImpl();

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(skybox);
        Assert.notNull(planetSet);
        Assert.notNull(modelSet);
        Assert.notNull(wireframeBucket);
        Assert.notNull(meshBucket);
        Assert.notNull(hud);
    }

    @Override
    public void setup() {
        super.setup();

        skybox.setup();

        modelSet.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.AMBIENT_SHADER));
        Model cube = ModelToolkit.createCube();
        cube.setPosition(new Vector3f(30,20,10));
        modelSet.add(cube);
        Model icosphere = ModelToolkit.createIcosphere();
        icosphere.setPosition(new Vector3f(30,20,-10));
        modelSet.add(icosphere);
        Model ship01 = ModelToolkit.createShip01();
        ship01.setPosition(new Vector3f(20,10,10));
        modelSet.add(ship01);
        Model ship02 = ModelToolkit.createShip02();
        ship02.setPosition(new Vector3f(20,10,-10));
        modelSet.add(ship02);
        modelSet.setup();


        for (int x = -5; x <=5; x++) {
            for (int z = -5; z <=5; z++) {
                Model model = ModelToolkit.loadModelFromFile("/models/cube/cube.obj");
                model.setPosition(new Vector3f(x * 7, 0, z * 7));
                modelSet.add(model);
            }
        }

        VertexLight light1 = new VertexLight(0.001f, new Vector4f(1f, 0.5f, 0.5f, 1.0f), new Vector3f( 0, 10, -17));
        light1.setPosition(new Vector3f(0,10,20));
        VertexLight light2 = new VertexLight(0.001f, new Vector4f(0.5f, 0.5f, 1f, 1.0f), new Vector3f( 0, 10, 17));
        light2.setPosition(new Vector3f(0,10,-20));

        //new PositionFrame(l1).setup();
        modelSet.add(light1);
        modelSet.add(light2);


        wireframeBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.WIREFRAME_SHADER));
        wireframeBucket.addAll(WireframeToolkit.createCircledTarget());
        wireframeBucket.addAll(WireframeToolkit.createDebugElements());
        wireframeBucket.addAll(WireframeToolkit.createOriginAxis());
        wireframeBucket.addAll(WireframeToolkit.createRandomElements());
        wireframeBucket.addAll(WireframeToolkit.createRandomLocatedSpheres());
        wireframeBucket.setup();

        meshBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER));
        TexturedQuad tex = new TexturedQuad();
        tex.setPosition(new Vector3f(0,-10,-10));
        meshBucket.add(tex);
        meshBucket.setup();

        planetSet.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.AMBIENT_SHADER));
        CelestialBody body01 = new CelestialBody(0);
        body01.setPosition(new Vector3f(7,0,0));
        planetSet.add(body01);
        planetSet.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.AMBIENT_SHADER));
        CelestialBody body02 = new CelestialBody(0);
        body02.setPosition(new Vector3f(7,0,0));
        planetSet.add(body02);

        VertexLight light3 = new VertexLight(0.00001f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector3f( 0, 10, -17));
        VertexLight light4 = new VertexLight(0.00001f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector3f( 0, 10, 17));
        planetSet.add(light3);
        planetSet.add(light4);

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
        planetSet.render();
        modelSet.render();
        wireframeBucket.render();
        meshBucket.render();
        hud.render();
    }

    @Override
    public void update(float tpf) {
        planetSet.update(tpf);
        modelSet.update(tpf);
        wireframeBucket.update(tpf);
        meshBucket.update(tpf);
        hud.update(tpf);
    }

    @Override
    public void destroy() {
        skybox.destroy();
        planetSet.destroy();
        modelSet.destroy();
        wireframeBucket.destroy();
        meshBucket.destroy();
        hud.destroy();
        super.destroy();
    }

}
