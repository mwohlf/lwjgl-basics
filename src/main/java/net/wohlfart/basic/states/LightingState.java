package net.wohlfart.basic.states;

import java.io.IOException;
import java.io.InputStream;

import net.wohlfart.gl.antlr4.GenericMeshLoader;
import net.wohlfart.gl.elements.debug.Circle;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.ShaderRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class LightingState extends AbstractGraphicState {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightingState.class);

    private GraphicContextManager.IGraphicContext lightingGraphicContext;

    private final RenderBucket elemBucket = new RenderBucket();


    @Override
    public void setup() {
        super.setup();
        lightingGraphicContext = new DefaultGraphicContext(ShaderRegistry.LIGHTING_SHADER);
        elemBucket.init(lightingGraphicContext, getCamera());

        try (InputStream inputStream = ClassLoader.class.getResourceAsStream("/models/cube/cube.obj");) {
            setCurrentGraphicContext(lightingGraphicContext);
            elemBucket.add(new GenericMeshLoader().getRenderable(inputStream));
            elemBucket.add(new Circle(3f));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
    }

    @Override
    public void render() {
        elemBucket.render();
    }

    @Override
    public void destroy() {

    }

}
