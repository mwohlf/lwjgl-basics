package net.wohlfart.basic.hud;

import net.wohlfart.basic.hud.Layer.Widget;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextHolder;
import net.wohlfart.gl.shader.GraphicContextHolder.IGraphicContext;
import net.wohlfart.gl.shader.ShaderRegistry;

import org.lwjgl.opengl.GL11;


public class HudImpl implements Hud {

    private final Layer layer = new LayerImpl();

    private IGraphicContext graphicContext;


    @Override
    public void setup() {
        graphicContext = new DefaultGraphicContext(ShaderRegistry.PLAIN_SHADER);
        graphicContext.setup();
    }

    @Override
    public void add(Widget label) {
        layer.add(label);
    }

    @Override
    public void render() {  // no uniform matrices needed since the hud is just 2D
        GraphicContextHolder.CONTEXT_HOLDER.setCurrentGraphicContext(graphicContext);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
