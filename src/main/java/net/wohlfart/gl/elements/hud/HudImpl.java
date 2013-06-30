package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.widgets.AbstractTextComponent;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;

/**
 * this class is responsible for switching context and rendering the hud components
 *
 *
 *
 */
public class HudImpl implements Hud {

    private final LayerImpl layer = new LayerImpl();

    private IGraphicContext graphicContext;



    @Override
    public void setup() {
        graphicContext = new DefaultGraphicContext(ShaderRegistry.HUD_SHADER);
        graphicContext.setup();
    }

    @Override
    public void add(AbstractTextComponent label) {
        layer.add(label);
    }

    @Override
    public void render() {
        assert graphicContext != null : "the graphicContext is null, make sure to call the init method";

        GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);

        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextManager.INSTANCE.getPerspectiveProjMatrix());

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_BLEND);

        layer.render();
    }

    @Override
    public void destroy() {
        layer.destroy();
    }

    @Override
    public void dispose() {

    }

}
