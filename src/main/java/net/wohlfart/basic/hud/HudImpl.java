package net.wohlfart.basic.hud;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;
import net.wohlfart.basic.hud.Layer.Widget;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextHolder;
import net.wohlfart.gl.shader.GraphicContextHolder.IGraphicContext;
import net.wohlfart.gl.shader.PerspectiveProjectionFab;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;


public class HudImpl implements Hud {

    private final Layer layer = new LayerImpl();

    private final Matrix4f camViewMatrix = new Matrix4f();

    private IGraphicContext graphicContext;


    @Override
    public void setup() {
        camViewMatrix.load(new PerspectiveProjectionFab().create(CONTEXT_HOLDER.getSettings()));
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
        ShaderUniformHandle.CAM_TO_CLIP.set(camViewMatrix);
        //ShaderUniformHandle.CAM_TO_CLIP.set(SimpleMath.UNION_MATRIX);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

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
