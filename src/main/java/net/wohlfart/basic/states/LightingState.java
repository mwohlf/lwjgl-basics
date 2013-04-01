package net.wohlfart.basic.states;

import net.wohlfart.gl.elements.debug.Circle;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.renderer.RenderBucket;
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

    private final RenderBucket elemBucket = new RenderBucket();



    @Override
    public void setup() {
        super.setup();
        lightingGraphicContext = new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER);
        defaultGraphicContext = new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER);


        skybox.init(defaultGraphicContext, getCamera());

        elemBucket.init(lightingGraphicContext, getCamera());
        //elemBucket.add(SceneCreator.loadFromFile("/models/cube/cube.obj"));
        elemBucket.add(SceneCreator.loadFromFile("/models/icosphere/icosphere.obj"));
        elemBucket.add(new Circle(3f));
    }


    @Override
    public void update(float tpf) {
        // LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
    }

    @Override
    public void render() {
        skybox.render();
        elemBucket.render();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
