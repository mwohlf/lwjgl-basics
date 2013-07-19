package net.wohlfart.basic.states;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;

import java.util.List;

import net.wohlfart.basic.container.DefaultRenderBatch;
import net.wohlfart.basic.container.ModelRenderBatch;
import net.wohlfart.basic.container.ModelToolkit;
import net.wohlfart.basic.container.WireframeToolkit;
import net.wohlfart.basic.elements.IsUpdatable;
import net.wohlfart.gl.elements.TexturedQuad;
import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.HudImpl;
import net.wohlfart.gl.elements.hud.widgets.MouseClickLabel;
import net.wohlfart.gl.elements.hud.widgets.SimpleLabel;
import net.wohlfart.gl.elements.hud.widgets.StatisticLabel;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.elements.skybox.SkyboxImpl;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.PerspectiveProjectionFab;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.VertexLight;
import net.wohlfart.gl.spatial.CelestialBody;
import net.wohlfart.gl.spatial.Model;
import net.wohlfart.gl.spatial.ParticleEmitter;
import net.wohlfart.gl.texture.CelestialType;
import net.wohlfart.gl.view.Camera;
import net.wohlfart.gl.view.ElementPicker;
import net.wohlfart.gl.view.PickEvent;
import net.wohlfart.gl.view.PickingRay;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.google.common.eventbus.Subscribe;

public class CelestialState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CelestialState.class);

    private final Skybox skybox = new SkyboxImpl();   //NullSkybox.INSTANCE;
    //private final Skybox skybox = NullSkybox.INSTANCE;

    private final DefaultRenderBatch<CelestialBody> planetSet = new DefaultRenderBatch<>();

    private final DefaultRenderBatch<CelestialBody> sunSet = new DefaultRenderBatch<>();

    private final ModelRenderBatch modelSet = new ModelRenderBatch();

    private final DefaultRenderBatch<ParticleEmitter> emitterSet = new DefaultRenderBatch<>();

    private final DefaultRenderBatch<IsUpdatable> wireframeBucket = new DefaultRenderBatch<>();

    private final DefaultRenderBatch<IsUpdatable> meshBucket = new DefaultRenderBatch<>();

    private final Hud hud = new HudImpl();

    private final ElementPicker elementPicker = new ElementPicker();

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

        modelSet.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER));
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
        //modelSet.add(light1);
        //modelSet.add(light2);

        ParticleEmitter particleEmitter = new ParticleEmitter();
        particleEmitter.setPosition(new Vector3f(20,0,0));
        emitterSet.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER));
        emitterSet.add(particleEmitter);
        emitterSet.setup();

        wireframeBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.WIREFRAME_SHADER));
        wireframeBucket.addAll(WireframeToolkit.createCircledTarget());
        wireframeBucket.addAll(WireframeToolkit.createDebugElements());
        wireframeBucket.addAll(WireframeToolkit.createOriginAxis());
        wireframeBucket.addAll(WireframeToolkit.createRandomElements());
//        wireframeBucket.addAll(WireframeToolkit.createRandomLocatedSpheres());
        wireframeBucket.setup();

        meshBucket.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER));
        TexturedQuad tex = new TexturedQuad();
        tex.setPosition(new Vector3f(0,-10,-10));
        tex.setTexFilename("/gfx/images/ash_uvgrid01.png");
        meshBucket.add(tex);
        meshBucket.setup();

        planetSet.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER));
        for (int i = 0 ; i < 20; i++) {
            CelestialBody body = new CelestialBody(i);
            body.setPosition(new Vector3f(7*i,0,0));
            planetSet.add(body);
        }

        VertexLight light6 = new VertexLight(0.001f, new Vector4f(0.9f, 0.9f, 0.9f, 1.0f), new Vector3f( 7, 7, -13));
        VertexLight light7 = new VertexLight(0.001f, new Vector4f(0.9f, 0.9f, 0.9f, 1.0f), new Vector3f( 7, 7, 13));
        planetSet.add(light6);
        planetSet.add(light7);
        planetSet.setup();


        sunSet.setGraphicContext(new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER));

        CelestialBody sun1 = new CelestialBody(1L, CelestialType.SUN, 1f);
        sun1.setPosition(light6.getPosition());
        sunSet.add(sun1);
        CelestialBody sun2 = new CelestialBody(1L, CelestialType.SUN, 1f);
        sun2.setPosition(light7.getPosition());
        sunSet.add(sun2);
        sunSet.setup();


        hud.add(new SimpleLabel(0, 0, "hello world at (0,0)"));

        elementPicker.setup();

        hud.add(new StatisticLabel(0, -70));
        hud.add(new MouseClickLabel(0, -35));

        hud.setup();
    }

    @Subscribe
    public void onPickEvent(PickEvent pickEvent) {

        Matrix4f projectionMatrix = new PerspectiveProjectionFab().create(CONTEXT_HOLDER.getSettings());

        final Camera camera = CONTEXT_HOLDER.getCamera();       // FIXME: this calculation should be moved into the camera class
        Vector3f posVector = new Vector3f();
        Matrix4f posMatrix = new Matrix4f();
        SimpleMath.convert(camera.getPosition().negate(posVector), posMatrix);
        Matrix4f rotMatrix = new Matrix4f();
        SimpleMath.convert(camera.getRotation(), rotMatrix);
        Matrix4f rotPosMatrix = new Matrix4f();
        Matrix4f.mul(rotMatrix, posMatrix, rotPosMatrix);

        Matrix4f modelViewMatrix = rotPosMatrix;

        PickingRay pickingRay = pickEvent.createPickingRay(projectionMatrix, modelViewMatrix);
        wireframeBucket.add(Arrow.createLink(pickingRay.getStart(), pickingRay.getEnd()));
        List<Model> picklist = modelSet.pick(pickingRay);
        System.out.println("picking: " + picklist);
    }

    @Override
    public void render() {
        GL11.glClearColor(0.5f, 0.5f, 0.5f, 0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        skybox.render();

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        planetSet.render();
        sunSet.render();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        emitterSet.render();

        // FIXME: transparency is broken
        GL11.glDisable(GL11.GL_BLEND);
        modelSet.render();
        wireframeBucket.render();
        meshBucket.render();


        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        hud.render();
    }

    @Override
    public void update(float tpf) {
        planetSet.update(tpf);
        sunSet.update(tpf);
        emitterSet.update(tpf);
        modelSet.update(tpf);
        wireframeBucket.update(tpf);
        meshBucket.update(tpf);
        hud.update(tpf);
    }

    @Override
    public void destroy() {
        elementPicker.destroy();
        skybox.destroy();
        planetSet.destroy();
        sunSet.destroy();
        modelSet.destroy();
        wireframeBucket.destroy();
        meshBucket.destroy();
        hud.destroy();
        super.destroy();
    }

}
