package net.wohlfart.basic.states;

import net.wohlfart.gl.action.RotateAction;
import net.wohlfart.gl.antlr4.Model;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.renderer.ModelBucket;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.ShaderRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * REVIEW:
 *
 * @author michael
 *
 */
final class LightingState extends AbstractGraphicState {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightingState.class);

    private GraphicContextManager.IGraphicContext lightingGraphicContext;
    private GraphicContextManager.IGraphicContext defaultGraphicContext;

    private final Skybox skybox = new Skybox();

    private final ModelBucket modelBucket = new ModelBucket();



    @Override
    public void setup() {
        super.setup();
        lightingGraphicContext = new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER);
        defaultGraphicContext = new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER);

        skybox.init(defaultGraphicContext, getCamera());

        modelBucket.init(lightingGraphicContext, getCamera());
        //elemBucket.add(SceneCreator.loadFromFile("/models/cube/cube.obj"));
        Model icosphere = SceneCreator.loadModelFromFile("/models/icosphere/icosphere.obj");
        icosphere.setAction(new RotateAction(icosphere));
        //Model cube = SceneCreator.loadModelFromFile("/models/cube/cube.obj");

        modelBucket.add(icosphere);
        //modelBucket.add(cube);
    }


    @Override
    public void update(float tpf) {
        modelBucket.update(tpf);
    }

    @Override
    public void render() {
        skybox.render();
        modelBucket.render();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
