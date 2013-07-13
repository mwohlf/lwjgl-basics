package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.Layer.Widget;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;


public class HudImpl implements Hud {

    private final Layer layer = new LayerImpl();

    private IGraphicContext graphicContext;



    @Override
    public void setup() {
        graphicContext = new DefaultGraphicContext(ShaderRegistry.HUD_SHADER);
        graphicContext.setup();
    }

    @Override
    public void add(Widget label) {
        layer.add(label);
    }

    @Override
    public void render() {

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
    public void update(float tpf) {
        layer.update(tpf);
    }

}
