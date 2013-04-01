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

        int count = 16;

        for (int i = 0; i < count ; i++) {
            Model icosphere = SceneCreator.loadModelFromFile("/models/icosphere/icosphere.obj");
            icosphere.setPosition(SceneCreator.getRandomPosition());
            icosphere.setAction(new RotateAction(icosphere));
            modelBucket.add(icosphere);
        }

        for (int i = 0; i < count ; i++) {
            Model cube = SceneCreator.loadModelFromFile("/models/cube/cube.obj");
            cube.setPosition(SceneCreator.getRandomPosition());
            cube.setAction(new RotateAction(cube));
            modelBucket.add(cube);
        }

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
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
