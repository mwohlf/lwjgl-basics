package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.Layer.Widget;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextHolder;
import net.wohlfart.gl.shader.GraphicContextHolder.IGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.tools.SimpleMath;


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

        GraphicContextHolder.CONTEXT_HOLDER.setCurrentGraphicContext(graphicContext);

        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextHolder.CONTEXT_HOLDER.getPerspectiveProjMatrix());

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
